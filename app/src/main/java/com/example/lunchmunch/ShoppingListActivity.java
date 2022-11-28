package com.example.lunchmunch;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Main activity for all ShoppingList functionality
 */
public class ShoppingListActivity extends AppCompatActivity implements ShoppingListAdapter.ingrPurchasedListener, AddShopIngrFragment.OnFragmentInteractionListener{

    float epsilon = Float.MIN_NORMAL;

    RecyclerView shoplistRecView;
    ArrayList<Ingredient> shoppingList;

    FirebaseFirestore db;
    CollectionReference IngrCollec;


    LinearLayout IngredientsNav, RecipesNav, MealPlanNav;


    ShoppingListAdapter shoppingListAdapter;

    Spinner sortSpinner;
    ArrayAdapter<String> sortAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppinglist_activity);

        shoppingList = new ArrayList<>();

        // updates shopping list with the needed ingredients
        // remove requiremnt for ingr list as can have meal plan with no ingr (add if inside func for ingr != null)
        if (MealPlanActivity.allMeals != null) {
            updateShoppingList();
        }

        // init firebase reference
        db = FirebaseFirestore.getInstance();
        IngrCollec = db.collection("Ingredients");

        initViews();

        shoppingListAdapter = new ShoppingListAdapter(this, shoppingList);
        shoplistRecView.setLayoutManager(new LinearLayoutManager(this));
        shoplistRecView.setAdapter(shoppingListAdapter);

        // Sorting Spinner
        sortAdapter = new ArrayAdapter<String>(ShoppingListActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sortOptionsS));
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);


        shoppingListAdapter.setIngrPurchasedListener(new ShoppingListAdapter.ingrPurchasedListener() {
            @Override
            public void ingrPurchasedBtnClicked(Ingredient ingredient, Integer ingrIdx) {
                new AddShopIngrFragment().newInstance(ingredient, ingrIdx).show(getSupportFragmentManager(), "Add_Ingr");
            }
        });


        IngredientsNav.setOnClickListener(view -> {
            startActivity(new Intent(ShoppingListActivity.this, IngredientsActivity.class));
        });

        RecipesNav.setOnClickListener(view -> {
            startActivity(new Intent(ShoppingListActivity.this, RecipeActivity.class));
        });

        MealPlanNav.setOnClickListener(view -> {
            startActivity(new Intent(ShoppingListActivity.this, MealPlanActivity.class));
        });

        // Spinner Sorting Functionality
        // sets spinner to default
        sortSpinner.setSelection(0);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String choice = sortSpinner.getSelectedItem().toString();
                Sort.ingredientSort(shoppingList, choice);
                shoppingListAdapter.notifyDataSetChanged();
                shoplistRecView.refreshDrawableState();

                if(!choice.equals("Sort By")) {
                    Toast toast = Toast.makeText(ShoppingListActivity.this, "Now sorting by " + choice, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void updateShoppingList() {
        //ArrayList<MealPlanItem> mealPlanList = MealPlanActivity.mealPlanList;

        // this hashmap allows us to get the total count needed for each ingredient in our mealplan
        // ingredient_name: ingredient

        HashMap<String, Ingredient> ingrMap = new HashMap<String, Ingredient>();
        // iterate over every mealPlanItem in the mealPlanList and get ingredients needed

        ArrayList<MealPlanItem> allItems = new ArrayList<>();
        for (ArrayList<MealPlanItem> mealPlan: MealPlanActivity.allMeals.values()) {
            for (MealPlanItem item: mealPlan) {
                allItems.add(item);
            }
        }

        for (MealPlanItem mealPlanItem : allItems) {

            if (mealPlanItem.getType() == MealPlanItemType.RECIPE) {
                // wouldnt let me put mealPlanItem.getIngredients() in for loop without changing for loop class to Object
                List<Ingredient> ingredients = mealPlanItem.getIngredients();
                for (Ingredient ingredient : ingredients) {
                    String ingrName = ingredient.getName();

                    if (ingrMap.containsKey(ingrName)) {
                        // if the ingredient already exists in the map then just add to its required count
                        Float newCount = ingredient.getCount() + ingrMap.get(ingrName).getCount();
                        ingredient.setCount(newCount);

                    }
                    // hashmap will be either updated with the new ingredient count value or will get added the newly required ingredient
                    ingrMap.put(ingrName, ingredient);
                }

            } else if (mealPlanItem.getType() == MealPlanItemType.INGREDIENT) {


                // do this as wont allow us to put mealPlanItem in ingrMap.put (as technically not Ingredient instance)
                Ingredient ingredient = new Ingredient(
                        mealPlanItem.getName(),
                        mealPlanItem.getDescription(),
                        mealPlanItem.getBestBefore(),
                        mealPlanItem.getLocation(),
                        mealPlanItem.getCount(),
                        mealPlanItem.getCost(),
                        mealPlanItem.getCategory()
                );

                String ingrName = ingredient.getName();
                //Integer ingrCount = mealPlanItem.getCount();
                //ingrMap.put(ingrName, ingrMap.getOrDefault(ingrName, ingrCount) + ingrCount);
                // if the ingr was added to ingrMap in a prev iter of this for loop (same ingr used in multiple meals)
                if (ingrMap.containsKey(ingrName)) {
                    // if the ingredient already exists in the map then just add to its required count
                    Float newCount = mealPlanItem.getCount() + ingrMap.get(ingrName).getCount();
                    mealPlanItem.setCount(newCount);

                }
                // hashmap will be either updated with the new ingredient count value or will get added the newly required ingredient
                ingrMap.put(ingrName, ingredient);

            }

        }


        // now we have a map that contains the total count of all ingredients needed
        // subtract the count of ingredients we already have from the Ingredients page
        // the remaining ingredients get added to the shopping list


        if (IngredientsActivity.ingredientsList != null) {
            for (Ingredient ingredient : IngredientsActivity.ingredientsList) {
                String ingrName = ingredient.getName();
                Float ingrCount = ingredient.getCount();
                if (ingrMap.containsKey(ingrName)) {
                    Float neededIngrCount = ingrMap.get(ingrName).getCount();
                    // if we have enough of this specific ingredient then remove it from the hash (dont add it to the shopping list)
                    // ingr.getCount() => neededIngrCount
                    Float diff = ingrCount - neededIngrCount;
                    // if ingr Count => neededIngr count


                    if (diff > 0.01 || Math.abs(diff) < epsilon) {//ingrCount.equals(neededIngrCount)) {
                        // remove specific ingr from the hashmap
                        ingrMap.remove(ingrName);

                        // could do just else here if we want (but this easier to read)
                        // else if ingr count < neededIngr count
                    } else if (diff < 0) {
                        // update ingr in map to the count needed (needed from meal plan - already have from ingredients
                        Float newCount = neededIngrCount - ingrCount;

                        Ingredient newIngr = ingrMap.get(ingrName);
                        newIngr.setCount(newCount);

                        //ingrMap.get(ingrName).setCount(newCount); // not sure if this will work test later
                        ingrMap.put(ingrName, newIngr);
                    }
                }
            }
        }

        // iterate over all the ingredients in our map (these are the ingredients we need at the counts we need)
        for (Ingredient ingredient : ingrMap.values()) {
            shoppingList.add(ingredient);
        }

    }


    private void initViews() {
        IngredientsNav = findViewById(R.id.ingredientsNav);
        RecipesNav = findViewById(R.id.recipesNav);
        MealPlanNav = findViewById(R.id.mealPlanNav);
        shoplistRecView = findViewById(R.id.shoplistRecView);
        sortSpinner = findViewById(R.id.SortOptions);
    }

    //ignore this
    @Override
    public void ingrPurchasedBtnClicked(Ingredient ingredient, Integer ingrIdx) {
        // open up popup dialog where user enters Ingredient expiry date, location, unit/price, and count (min has to be the amount required)
        //new AddShopIngrFragment().newInstance(ingredient, ingrIdx).show(getSupportFragmentManager(), "Add_Ingr");

    }

    @Override
    public void onOkPressed(Ingredient ingredient, Integer ingrIdx) {

        // first check if ingr already exists in IngredientsActivity.ingredientsList
        // if it does then get the current count of said igr then add the count we just added from shopping list
        // then upload to db
        Optional<Ingredient> optExistingIngr = IngredientsActivity.ingredientsList.stream().
                filter(i -> i.getName().equals(ingredient.getName())).
                findFirst(); // can use findFirst as we know that ingr name is its unique id so will have only one occurance
        Ingredient existingIngr = optExistingIngr.orElse(null);
        if (existingIngr != null) {
            // not sure if can just throw ingredient.getCount() inside the setCount() w/out issues (test if time)
            Float ingrCount = ingredient.getCount();
            ingredient.setCount(existingIngr.getCount() + ingrCount);
        } // otherwise igr is new and doesnt already exist in ingr storage so just upload as is


        IngrCollec.document(ingredient.getName()).set(ingredient) // .add equiv to .collec().set(..)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        // after successfully uploaded to ingr collec we can remove from the shopping list
                        System.out.println("onOK-Success");
                        //shoppingList.remove(ingredient);
                        // update array as well as this only gets updated from database when we visit ingr page so update incase user doesnt visit ingr page after checking off ingr in shop list
                        // incase ingr already exists we are just updating the count, wont throw an error if doesnt exist
                        IngredientsActivity.ingredientsList.remove(ingredient);
                        IngredientsActivity.ingredientsList.add(ingredient);


                        // Prob dont need to update shopping list manually here as can just run updateShoppingList() instead since ingrList in IngrActivity being modified

                        //overwrite the ingredient in shopping list if count is less than the amount required otherwise remove the element from the shopping list
                        Optional<Ingredient> optExistingIngr = shoppingList.stream().
                                filter(i -> i.getName().equals(ingredient.getName())).
                                findFirst(); // can use findFirst as we know that ingr name is its unique id so will have only one occurance
                        Ingredient shopIngr = optExistingIngr.orElse(null);
                        if (shopIngr != null) {
                            // if the amount of ingr we required in the shopping list is still greater then the amount the user entered then just update the ingr in the shopping list
                            // since we create a new Ingr instance in addShopIngrFragment we cant directly acess we have to access by name to delete

                            Float diff = shopIngr.getCount() - ingredient.getCount();

                            // if the count of the ingr the user entered is less than or equal to the amount required
                            if (diff > 0.01) {
                                //update the item in the shopping list as user did not get enough count to get off shopping list
                                shopIngr.setCount(diff);
                                shoppingList.set(ingrIdx, shopIngr);
                            } else {
                                shoppingList.remove(shopIngr);
                            }


                        } // otherwise remove ingr from shopping list


                        shoppingListAdapter.notifyDataSetChanged();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Fail");
                        Toast.makeText(getApplicationContext(),"Failed to upload "+ingredient.getName()+ " to database, please try again.",Toast.LENGTH_LONG).show();

                    }
                });

    }
}

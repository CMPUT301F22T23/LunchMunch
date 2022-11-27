package com.example.lunchmunch;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

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

    //Button IngredientsNav, RecipesNav, MealPlanNav, saveIngrBtn;
    RecyclerView shoplistRecView;
    ArrayList<Ingredient> shoppingList;

    FirebaseFirestore db;
    CollectionReference IngrCollec;


    LinearLayout IngredientsNav, RecipesNav, MealPlanNav;
    //ArrayList<Ingredient> shoppingList = new ArrayList<>();

    ShoppingListAdapter shoppingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppinglist_activity);

        shoppingList = new ArrayList<>();

        // updates shopping list with the needed ingredients
        // remove requiremnt for ingr list as can have meal plan with no ingr (add if inside func for ingr != null)
        if (MealPlanActivity.allMeals != null) {
            //System.out.println("NOT NULL:");
            updateShoppingList();
            //System.out.println("SL:: "+shoppingList);
        }

        // init firebase reference
        db = FirebaseFirestore.getInstance();
        IngrCollec = db.collection("Ingredients");

        initViews();

        shoppingListAdapter = new ShoppingListAdapter(this, shoppingList);
        shoplistRecView.setLayoutManager(new LinearLayoutManager(this));
        shoplistRecView.setAdapter(shoppingListAdapter);

        /*
        saveIngrBtn.setOnClickListener(view -> {
            //IngredientsActivity.updateIngrList(ShoppingListAdapter.checkedIngr);

            //
            for (Ingredient ingredient : ShoppingListAdapter.checkedIngr) {
                //String shopLIngrName = ingredient.getName();
                //boolean ingrExists = IngredientsActivity.ingredientsList.stream().map(Ingredient::getName).anyMatch(shopLIngrName::equals);
                // if the ingr exists then we need to get the count of the ingr in the ingr page and add the count we got from our shopping list
                // otherwise we are adding to db as new ingr we bought up from shopping list


                Optional<Ingredient> optExistingIngr = IngredientsActivity.ingredientsList.stream().
                        filter(i -> i.getName().equals(ingredient.getName())).
                        findFirst(); // can use findFirst as we know that ingr name is its unique id so will have only one occurance
                Ingredient existingIngr = optExistingIngr.orElse(null);
                if (existingIngr != null) {
                    // update the ingredient to have the sum of the count we already have (from existingIngr) and the count we just got from ingr
                    ingredient.setCount(ingredient.getCount() + existingIngr.getCount());
                } // otherwise we are just adding the ingr we picked up to ingr collec

                final boolean[] failedIngrUpload = {false};

                // this will either add or overwrite (if ingr already exists) to ingr collec
                // therefore do not need to worry if only need to update exiting ingr count
                IngrCollec.document(ingredient.getName()).set(ingredient) // .add equiv to .collec().set(..)
                        .addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                // after successfully uploaded to ingr collec we can remove from the shopping list
                                System.out.println("Success");
                                shoppingList.remove(ingredient);
                                // update array as well as this only gets updated from database when we visit ingr page so update incase user doesnt visit ingr page after checking off ingr in shop list
                                // incase ingr already exists we are just updating the count, wont throw an error if doesnt exist
                                IngredientsActivity.ingredientsList.remove(ingredient);
                                IngredientsActivity.ingredientsList.add(ingredient);
                                shoppingListAdapter.notifyDataSetChanged();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("Fail");
                                failedIngrUpload[0] = true;
                                //

//                        Log.w(TAG, "Error adding document", e);
                            }
                        });
                if (failedIngrUpload[0]) {
                    Toast.makeText(getApplicationContext(),"Failed to upload "+ingredient.getName()+ " to database",Toast.LENGTH_LONG).show();
                    break;
                }

            }
            // if we actually checked off ingr and pressed save checked to ingr btn
            if (ShoppingListAdapter.checkedIngr.size() > 0) {
                Toast.makeText(getApplicationContext(), "Ingredients that were checked off and deleted have been added to Ingredients page", Toast.LENGTH_LONG).show();
                ShoppingListAdapter.checkedIngr.clear();
            }

        });

        */

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
                    /*
                    Float ingrCount = ingredient.getCount();

                    // if the ingr not in the map then init with its count, otherwise if ingr already in map then just add this instance of the ingr's count to the count thats already in the map same as here( https://stackoverflow.com/a/37705877/17304003)
                    ingrMap.put(ingrName, ingrMap.getOrDefault(ingrName, ingrCount) + ingrCount);
                     */
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
                    if (ingrCount > neededIngrCount || ingrCount.equals(neededIngrCount)) {
                        // remove specific ingr from the hashmap
                        ingrMap.remove(ingrName);

                        // could do just else here if we want (but this easier to read)
                    } else if (ingrCount < neededIngrCount) {
                        // update ingr in map to the count needed (needed from meal plan - already have from ingredients
                        Float newCount = ingrMap.get(ingrName).getCount() - ingrCount;
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
                        Ingredient existingIngr = optExistingIngr.orElse(null);
                        if (existingIngr != null) {
                            // if the amount of ingr we required in the shopping list is still greater then the amount the user entered then just update the ingr in the shopping list
                            // since we create a new Ingr instance in addShopIngrFragment we cant directly acess we have to access by name to delete
                            shoppingList.remove(existingIngr);


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

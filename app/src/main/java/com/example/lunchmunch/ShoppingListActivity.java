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
import androidx.collection.CircularArray;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Date;
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
    CollectionReference MealPlanCollec;
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


        // init firebase reference
        db = FirebaseFirestore.getInstance();
        MealPlanCollec = db.collection("MealPlans");
        IngrCollec = db.collection("Ingredients");

        // fetch the ingr needed from database by subtracting ingr needed for meal plan minus the ingr already owned in the ingredients page
        dbUpdateShoppingList();

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

    private void dbUpdateShoppingList() {
        dbMealPlanIngr(new MealPlanDBCallBack() {
            @Override
            public void onCallBack(HashMap<String, Ingredient> ingrMap) {
                // now have the ingrMap after getting all ingr in MealPlan (remove ingr in ingr storage from ingrMap now)

                IngrCollec.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            // each document in the Ingredients collection is an Ingredient class object
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String ingrName = (String) document.getData().get("name");
                                Float ingrCount = Float.valueOf(0);
                                if (document.getData().get("count") instanceof Double) {
                                    ingrCount = ((Double) document.getData().get("count")).floatValue();

                                } else {
                                    ingrCount = ((Long) document.getData().get("count")).floatValue();

                                }

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

                                        ingrMap.put(ingrName, newIngr);
                                    }
                                }
                            }

                            // here we update shopping list
                            // iterate over all the ingredients in our map (these are the ingredients we need at the counts we need)
                            for (Ingredient ingredient : ingrMap.values()) {
                                shoppingList.add(ingredient);
                            }
                            shoppingListAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    private interface MealPlanDBCallBack {
        void onCallBack(HashMap<String, Ingredient> ingrMap);
    }

    private void dbMealPlanIngr(MealPlanDBCallBack mealPlanDBCallBack) {

        MealPlanCollec.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    HashMap<String, Ingredient> ingrMap = new HashMap<String, Ingredient>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // convert document objects back into Ingredient class objects
                        Ingredient ingr = null;


                        for (Object data : document.getData().values()) {

                            HashMap<String, Object> foodData = (HashMap<String, Object>) data;
                            if (foodData.get("type").equals("INGREDIENT")) {
                                String name = (String) foodData.get("name");
                                String description = (String) foodData.get("description");
                                Timestamp timestamp = (Timestamp) foodData.get("bestBefore");
                                Date bestBefore = timestamp.toDate();
                                Location location = Location.valueOf(foodData.get("location").toString().toUpperCase());
                                Float cost = Float.valueOf(0);
                                Float count = Float.valueOf(0);
                                if (foodData.get("cost") instanceof Double) {
                                    cost = ((Double) foodData.get("cost")).floatValue();

                                } else {
                                    cost = ((Long) foodData.get("cost")).floatValue();

                                }

                                if (foodData.get("count") instanceof Double) {
                                    count = ((Double) foodData.get("count")).floatValue();

                                } else {
                                    count = ((Long) foodData.get("count")).floatValue();

                                }
                                IngredientCategory category = IngredientCategory.valueOf(foodData.get("category").toString().toUpperCase());


                                ingr = new Ingredient(name, description, bestBefore, location, count, cost, category);

                                if (ingrMap.containsKey(name)) {
                                    // if the ingredient already exists in the map then just add to its required count
                                    Float newCount = ingr.getCount() + ingrMap.get(name).getCount();
                                    ingr.setCount(newCount);

                                }
                                // hashmap will be either updated with the new ingredient count value or will get added the newly required ingredient
                                ingrMap.put(name, ingr);


                            } else if (foodData.get("type").equals("RECIPE")) {
                                ArrayList<HashMap<String, Object>> ingredientsList = (ArrayList<HashMap<String, Object>>) foodData.get("ingredients");
                                for (HashMap<String, Object> ingredient : ingredientsList) {
                                    System.out.println(ingredient);

                                    String ingredientName = (String) ingredient.get("name");
                                    //ingredientNames.add(ingredientName);
                                    String description = (String) ingredient.get("description");
                                    Timestamp ingredientTimestamp = (Timestamp) ingredient.get("bestBefore");
                                    Date bestBefore = ingredientTimestamp.toDate();
                                    Location location = Location.valueOf(foodData.get("location").toString().toUpperCase());

                                    Float cost = new Float(0);
                                    Float count = new Float(0);
                                    if (foodData.get("cost") instanceof Double) {
                                        cost = ((Double) foodData.get("cost")).floatValue();

                                    } else {
                                        cost = ((Long) foodData.get("cost")).floatValue();

                                    }

                                    if (foodData.get("count") instanceof Double) {
                                        count = ((Double) foodData.get("count")).floatValue();

                                    } else {
                                        count = ((Long) foodData.get("count")).floatValue();

                                    }
                                    IngredientCategory category = IngredientCategory.valueOf(foodData.get("category").toString().toUpperCase());
                                    ingr = new Ingredient(ingredientName, description, bestBefore, location, count, cost, category);

                                    if (ingrMap.containsKey(ingredientName)) {
                                        // if the ingredient already exists in the map then just add to its required count
                                        Float newCount = ingr.getCount() + ingrMap.get(ingredientName).getCount();
                                        ingr.setCount(newCount);

                                    }
                                    // hashmap will be either updated with the new ingredient count value or will get added the newly required ingredient
                                    ingrMap.put(ingredientName, ingr);

                                }
                            }
                        }
                        // after we have retrieved all the ingr in mealplan and put them inside ingrMap run the callback
                        //mealPlanDBCallBack.onCallBack(ingrMap);
                    }
                    mealPlanDBCallBack.onCallBack(ingrMap);
                }
            }
        });
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


        IngrCollec.document(ingredient.getName()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // if the ingredient exists
                if (task.isSuccessful()) {
                    // Document found in the offline cache

                    System.out.println("task get Result: "+task);
                    System.out.println("get result: "+task.getResult());
                    System.out.println("get data: "+ task.getResult().getData());

                    DocumentSnapshot document = task.getResult();

                    // if the ingr exists in ingr storage
                    if (document.getData() != null) {

                        Float ingrCount = new Float(0); //(Float) document.getData().get("count");

                        if (document.getData().get("count") instanceof Double) {
                            ingrCount = ((Double) document.getData().get("count")).floatValue();

                        } else {
                            ingrCount = ((Long) document.getData().get("count")).floatValue();

                        }
                        ingredient.setCount(ingredient.getCount() + ingrCount);
                    } // otherwise the ingredient doesnt exist so no need to modify
                    // after we have the proper count for the ingr we can now send it to the ingredient storage
                    // and we either edit its value in place or delete it from the shopping list
                    addIngrFromShopLtoIngr(ingredient, ingrIdx);
                }
            }

        });
    }

    private void addIngrFromShopLtoIngr(Ingredient ingredient, Integer ingrIdx) {
        IngrCollec.document(ingredient.getName()).set(ingredient) // .add equiv to .collec().set(..)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        // after successfully uploaded to ingr collec we can remove from the shopping list
                        System.out.println("onOK-Success");
                        //shoppingList.remove(ingredient);

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

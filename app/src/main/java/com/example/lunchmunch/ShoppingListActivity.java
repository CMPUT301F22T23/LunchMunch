package com.example.lunchmunch;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
 * Handles the functionality for displaying the ingredients the user needs to add to ingredient storage to have enough to complete their mealplan
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

    /**
     * Runs on the initialization of the activity
     * part of the nececary methods used in AppCompatActivity class
     * @param savedInstanceState
     */

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


        /**
         * Checkbox click listener
         * when a checkbox is clicked in the recyclerview from the ShoppingListAdapter its associated Ingredient instance is sent here to then be sent to AddShopIngrFragment.java
         * AddShopIngrFragment.java takes this Ingredient data and lets the user fill in some missing info and add it into the ingredients page
         */
        shoppingListAdapter.setIngrPurchasedListener(new ShoppingListAdapter.ingrPurchasedListener() {
            @Override
            public void ingrPurchasedBtnClicked(Ingredient ingredient, Integer ingrIdx) {
                new AddShopIngrFragment().newInstance(ingredient, ingrIdx).show(getSupportFragmentManager(), "Add_Ingr");
            }
        });

        // nav button click listeners open up their associated activity page
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
            /**
             * Functionality for the sort spinner
             * on selection passes in choice as a string into the sort method which sorts the list
             * updates the view to display sorted list
             */
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
            /**
             * on nothing selected do nothing (pretty self explanatory)
             */
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    /**
     * General function for updating the shopping list using database calls
     * We first call method dbMealPlanIngr() to get a HashMap with all the ingredients needed for MealPlan
     * After the method calls back we get all the ingredients the user currently has in ingredient storage
     * We then subtract the difference to get the ingredients that the user is missing to eat their mealplan
     */
    private void dbUpdateShoppingList() {
        dbMealPlanIngr(new MealPlanDBCallBack() {

            /**
             * The callback function in action from the interface
             * Here we know that we have the ingrMap Hashmap from the MealPlan database call so we can now safely call a Ingredients db call and subtract the ingredients we have
             * @param ingrMap
             */
            @Override
            public void onCallBack(HashMap<String, Ingredient> ingrMap) {
                // now have the ingrMap after getting all ingr in MealPlan (remove ingr in ingr storage from ingrMap now)

                IngrCollec.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    /**
                     * onComplete method that will run after our call to our Ingredients collection database has completed
                     * Here we iterate over each collection in the Ingredients db and subtract its count value from the Mealplan ingredients needed if they match
                     * task is the data from the database call
                     * @param task
                     */
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

    /**
     * Interface that allows us to create a call back function for our database call to MealPlan
     */
    private interface MealPlanDBCallBack {
        /**
         * The callback method we need to implement it will hold the info that we need from the mealplan database call
         * this info is a HashMap that contains the name of each ingr as the key and the ingredient as the value
         * each of these ingredients are from the mealplan
         * @param ingrMap
         */
        void onCallBack(HashMap<String, Ingredient> ingrMap);
    }


    /**
     * Database call to MealPlan database
     * gets all the ingredients needed for mealplan, stored them in a ingrMap HashMap then runs the callback method with the ingrMap
     * @param mealPlanDBCallBack
     */
    private void dbMealPlanIngr(MealPlanDBCallBack mealPlanDBCallBack) {

        MealPlanCollec.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            /**
             * the onComplete method runs after our mealplan database call has completed its call to the database and has gotten all the nececary info
             * @param task
             */
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

                                    String ingredientName = (String) ingredient.get("name");
                                    String description = (String) ingredient.get("description");
                                    Timestamp ingredientTimestamp = (Timestamp) ingredient.get("bestBefore");
                                    Date bestBefore = ingredientTimestamp.toDate();
                                    Location location = Location.valueOf(foodData.get("location").toString().toUpperCase());

                                    Float cost = new Float(0);
                                    Float count = new Float(0);
                                    if (ingredient.get("cost") instanceof Double) {
                                        cost = ((Double) ingredient.get("cost")).floatValue();

                                    } else {
                                        cost = ((Long) ingredient.get("cost")).floatValue();

                                    }

                                    if (ingredient.get("count") instanceof Double) {
                                        count = ((Double) ingredient.get("count")).floatValue();

                                    } else {
                                        count = ((Long) ingredient.get("count")).floatValue();

                                    }

                                    IngredientCategory category = IngredientCategory.valueOf(ingredient.get("category").toString().toUpperCase());
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
                    }
                    // after we have retrieved all the ingr in mealplan and put them inside ingrMap run the callback
                    mealPlanDBCallBack.onCallBack(ingrMap);
                }
            }
        });
    }

    /**
     * initViews initializes the views of the activity
     * meaning it defines the frontend xml variables that the user sees
     * by defining the features it lets us interact with the frontend components through our code
     */
    private void initViews() {
        IngredientsNav = findViewById(R.id.ingredientsNav);
        RecipesNav = findViewById(R.id.recipesNav);
        MealPlanNav = findViewById(R.id.mealPlanNav);
        shoplistRecView = findViewById(R.id.shoplistRecView);
        sortSpinner = findViewById(R.id.SortOptions);
    }



    /**
     * Already defined above see {Checkbox click listener}
     * Just have to put this here to avoid errors
     * @param ingredient
     * @param ingrIdx
     */
    @Override
    public void ingrPurchasedBtnClicked(Ingredient ingredient, Integer ingrIdx) {
        // open up popup dialog where user enters Ingredient expiry date, location, unit/price, and count (min has to be the amount required)
    }

    /**
     * Ok/save button handler for our Add ShoppingList Ingredient Fragment (AddShopIngrFragment.java)
     * When the user presses the button they would like to add the ingredient they selected to ingredient storage
     * this method takes the info from the fragment and sends it to the ingredients page by uploading it to the ingredients collection
     * @param ingredient
     * @param ingrIdx
     */
    @Override
    public void onOkPressed(Ingredient ingredient, Integer ingrIdx) {

        IngrCollec.document(ingredient.getName()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            /**
             * Same functionality as all the other onComplete methods above
             * This one checks if the ingredient the user is trying to send to the ingredients page already exists in the ingredients collection
             * if it does exist it simple adds to the total count
             * @param task
             */
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // if the ingredient exists
                if (task.isSuccessful()) {
                    // Document found in the offline cache
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

    /**
     * Attempts to upload an ingredient to the ingredients collection
     * if the ingredient count the user entered is less than the amount required then edit the ingredient in the shopping list (at its index hence ingrIdx)
     * otherwise if the count is greater than or equal to then delete the ingredient from the shopping list and add it to the ingredients collection
     * @param ingredient
     * @param ingrIdx
     */
    private void addIngrFromShopLtoIngr(Ingredient ingredient, Integer ingrIdx) {
        IngrCollec.document(ingredient.getName()).set(ingredient) // .add equiv to .collec().set(..)
                .addOnSuccessListener(new OnSuccessListener() {
                    /**
                     * Success method for the database call
                     * if the database upload of the ingredient was successfull then remove or edit the ingredient in the shopping list
                     * @param o
                     */
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
                    /**
                     * Fail handler for the database upload of the ingredient to the ingredients collection
                     * simply just output a toast message for the user to see. Encouraging them to try again
                     * @param e
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed to upload "+ingredient.getName()+ " to database, please try again.",Toast.LENGTH_LONG).show();
                    }
                });
    }
}

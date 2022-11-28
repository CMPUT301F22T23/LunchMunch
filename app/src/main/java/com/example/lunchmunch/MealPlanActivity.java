package com.example.lunchmunch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Main activity for all MealPlan functionality
 */
public class MealPlanActivity extends AppCompatActivity implements MealPlanDateFragment.OnFragmentInteractionListener, MealPlanIngredientFragment.OnFragmentInteractionListener, MealPlanRecipeFragment.OnFragmentInteractionListener, MealPlanItemAdapter.OnAdapterInteractionListener {

    LinearLayout IngredientsNav, RecipesNav, ShoppingListNav;
    String days[] = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
    HashMap<String, ArrayList<MealPlanItem>> allMeals = new HashMap<>();
    HashMap<String, HashMap<String, MealPlanItem>> allMealsMap = new HashMap<>();
    HashMap<String, RecyclerView> recyclerViews = new HashMap<>();
    HashMap<String, ImageView> imageViews = new HashMap<>();
    HashMap<String, MealPlanItemAdapter> adapters = new HashMap<>();
    HashMap<String, MealPlanDateFragment> fragments = new HashMap<>();
    ImageView editMealPlanButton;

    FirebaseFirestore db;
    CollectionReference MealPlanCollec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealplan_activity);

        // init firebase reference
        db = FirebaseFirestore.getInstance();
        MealPlanCollec = db.collection("MealPlans");

        initViews();
        initRecyclerViews();
        initImageViews();
        initDBListener(MealPlanCollec);
        initDateFragments();

        IngredientsNav.setOnClickListener(view -> {
            startActivity(new Intent(MealPlanActivity.this, IngredientsActivity.class));
        });

        RecipesNav.setOnClickListener(view -> {
            startActivity(new Intent(MealPlanActivity.this, RecipeActivity.class));
        });

        ShoppingListNav.setOnClickListener(view -> {
            startActivity(new Intent(MealPlanActivity.this, ShoppingListActivity.class));
        });





    }

    /**
     * Initializes a hash map of all the recycler views for each day of the week for the meal plan
     * <p>
     * The keys are strings of days of the week with their corresponding recycler view value
     * @see         RecyclerView
     */

    private void initRecyclerViews() {
        recyclerViews.put("monday", findViewById(R.id.monday_meal_plan_items_list));
        recyclerViews.put("tuesday", findViewById(R.id.tuesday_meal_plan_items_list));
        recyclerViews.put("wednesday", findViewById(R.id.wednesday_meal_plan_items_list));
        recyclerViews.put("thursday", findViewById(R.id.thursday_meal_plan_items_list));
        recyclerViews.put("friday", findViewById(R.id.friday_meal_plan_items_list));
        recyclerViews.put("saturday", findViewById(R.id.saturday_meal_plan_items_list));
        recyclerViews.put("sunday", findViewById(R.id.sunday_meal_plan_items_list));

        for (String day: days) {
            RecyclerView recyclerView = recyclerViews.get(day);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

        }

    }

    /**
     * Initializes a hash map of all the image views of edit buttons for each day of the week for the meal plan
     * <p>
     * The keys are strings of days of the week with their corresponding image view value
     * @see         ImageView
     */

    private void initImageViews() {
        imageViews.put("monday", (ImageView) findViewById(R.id.monday_meal_plan_edit_pencil));
        imageViews.put("tuesday", (ImageView) findViewById(R.id.tuesday_meal_plan_edit_pencil));
        imageViews.put("wednesday", (ImageView) findViewById(R.id.wednesday_meal_plan_edit_pencil));
        imageViews.put("thursday", (ImageView) findViewById(R.id.thursday_meal_plan_edit_pencil));
        imageViews.put("friday", (ImageView) findViewById(R.id.friday_meal_plan_edit_pencil));
        imageViews.put("saturday",(ImageView)  findViewById(R.id.saturday_meal_plan_edit_pencil));
        imageViews.put("sunday", (ImageView) findViewById(R.id.sunday_meal_plan_edit_pencil));

    }

    /**
     * Initializes a hash map of all the meal plan date fragment of edit buttons for each day of the week for the meal plan
     * <p>
     * The keys are strings of days of the week with their corresponding meal plan fragment value
     * @see         MealPlanDateFragment
     */
    private void initDateFragments() {
        for (String day: days) {
            // adding or editing a new item button
            editMealPlanButton = imageViews.get(day);
            fragments.put(day, new MealPlanDateFragment());
            editMealPlanButton.setOnClickListener(view -> fragments.get(day).show(getSupportFragmentManager(), "Edit Meal Plan"));
            fragments.get(day).setDay(day);
        }

    }

    private void initViews() {
        IngredientsNav = findViewById(R.id.ingredientsNav);
        RecipesNav = findViewById(R.id.recipesNav);
        ShoppingListNav = findViewById(R.id.shoppingListNav);


    }

    /**
     * Clears the meals meap and re-adds all items after processing from the database
     * <ul>
     *     <li>this will create meal plan items from database</li>
     *     <li>Adds items to all meals arrays</li>
     *     <li>Adds items to all meals hashmaps</li>
     *     <li>Re initializes adapters for recycler views</li>
     * </ul>
     *
     *
     * @param mealPlanCollec A reference to the meal plan collection in the database
     * @see         MealPlanItem
     * @see         Recipe
     * @see         Ingredient
     */

    public void initDBListener(CollectionReference mealPlanCollec) {

        mealPlanCollec.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    // clear current list and re-add all the current ingredients
                    allMeals.clear();
                    // each document in the Ingredients collection is an Ingredient class object

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ArrayList<MealPlanItem> dataList = new ArrayList<>();
                        // convert document objects back into Ingredient class objects
                        MealPlanItem item = null;
                        System.out.println(document.getId());
                        for (Object data : document.getData().values()) {

                            HashMap<String, Object> foodData = (HashMap<String, Object>) data;
                            System.out.println("Checking Document");
                            System.out.println(foodData.get("type"));
                            if (foodData.get("type").equals("INGREDIENT")) {
                                String name = (String) foodData.get("name");
                                String description = (String) foodData.get("description");
                                Timestamp timestamp = (Timestamp) foodData.get("bestBefore");
                                Date bestBefore = timestamp.toDate();
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
                                item = new MealPlanItem(new Ingredient(name, description, bestBefore, location, count, cost, category));
                            } else if (foodData.get("type").equals("RECIPE")) {
                                String name = (String) foodData.get("name");

                                String id = (String) foodData.get("id");
                                String instructions = (String) foodData.get("instructions");
                                String mealType = (String) foodData.get("mealType");
                                String image = (String) foodData.get("image");
                                Integer servings = ((Long) foodData.get("servings")).intValue();
                                Integer prepTime = ((Long) foodData.get("prepTime")).intValue();
                                String comments = (String) foodData.get("comments");
                                ArrayList<HashMap<String, Object>> ingredientsList = (ArrayList<HashMap<String, Object>>) foodData.get("ingredients");

                                ArrayList<Ingredient> newIngredientsList = new ArrayList<Ingredient>();
                                ArrayList<String> ingredientNames = new ArrayList<String>();

                                for (HashMap<String, Object> ingredient : ingredientsList) {
                                    System.out.println(ingredient);

                                    String ingredientName = (String) ingredient.get("name");
                                    ingredientNames.add(ingredientName);
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
                                    newIngredientsList.add(new Ingredient(ingredientName, description, bestBefore, location, count, cost, category));
                                }

                                item = new MealPlanItem(new Recipe(id, name, newIngredientsList, new ArrayList<String>() , instructions, mealType, image, servings, prepTime, comments));

                            }

                            if (allMeals.get(document.getId()) == null) {
                                allMeals.put(document.getId(), new ArrayList<>());
                                allMealsMap.put(document.getId(), new HashMap<>());
                            }

                            allMealsMap.get(document.getId()).put(item.getName(), item);

                            dataList.add(item);

                            //https://stackoverflow.com/questions/66071922/how-to-wait-for-an-async-method-to-complete-before-the-return-statement-runs
//                            dbIngredients.onSuccess(ingredientsList);
                        }
                        adapters.put(document.getId(), new MealPlanItemAdapter(dataList));
                        adapters.get(document.getId()).setDay(document.getId());
                        recyclerViews.get(document.getId()).setAdapter(adapters.get(document.getId()));
                        fragments.get(document.getId()).setDataList(dataList);
                        allMeals.put(document.getId(), dataList);


                    }
                }
            }
        });
    }

    /**
     * Implements MealPlanIngredientFragment interface
     *<ul>
     *  <li>this will create meal plan items from ingredient click and adds to the database</li>
     *  <li>Adds item to all meals arrays</li>
     *  <li>Adds item to all meals hashmaps</li>
     *  <li>Re initializes adapters for recycler views</li>
     *</ul>
     *
     * @param ingredient the Ingredient to store as a Meal Plan item
     * @param day String of date to update
     * @see         MealPlanIngredientFragment
     */
    @Override
    public void onIngredientOkPressed(Ingredient ingredient, String day) {
        MealPlanItem item = new MealPlanItem(ingredient);

        if (allMeals.get(day) == null || allMealsMap.get(day) == null)  {
            allMeals.put(day, new ArrayList<>());
            allMealsMap.put(day, new HashMap<>());
        }
        allMealsMap.get(day).put(ingredient.getName(), item);
        allMeals.put(day, new ArrayList<>(allMealsMap.get(day).values()));

        adapters.put(day, new MealPlanItemAdapter(allMeals.get(day)));
        recyclerViews.get(day).setAdapter(adapters.get(day));
        fragments.get(day).setDataList(allMeals.get(day));

        // add to db
        MealPlanCollec.document(day).set(allMealsMap.get(day))
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        System.out.println("Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Fail");
                    }
                });

    }

    /**
     * Implements MealPlanIngredientFragment interface
     *<ul>
     *  <li>this will create meal plan items from recipe click and adds to the database</li>
     *  <li>Adds item to all meals arrays</li>
     *  <li>Adds item to all meals hashmaps</li>
     *  <li>Re initializes adapters for recycler views</li>
     *</ul>
     *
     * @param recipe the Recipe to store as a Meal Plan item
     * @param day String of date to update
     * @see         MealPlanIngredientFragment
     */
    @Override
    public void onRecipeOkPressed(Recipe recipe, String day) {
        MealPlanItem item = new MealPlanItem(recipe);

        if (allMeals.get(day) == null || allMealsMap.get(day) == null)  {
            allMeals.put(day, new ArrayList<>());
            allMealsMap.put(day, new HashMap<>());
        }
        allMealsMap.get(day).put(recipe.getName(), item);
        allMeals.put(day, new ArrayList<>(allMealsMap.get(day).values()));

        adapters.put(day, new MealPlanItemAdapter(allMeals.get(day)));
        recyclerViews.get(day).setAdapter(adapters.get(day));
        fragments.get(day).setDataList(allMeals.get(day));

        // add to db
        MealPlanCollec.document(day).set(allMealsMap.get(day))
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        System.out.println("Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Fail");
                    }
                });

    }

    @Override
    public void deleteItem(Integer position, String day) {
        System.out.println(allMeals);
        MealPlanItem item = allMeals.get(day).get(position);
        deleteMealPlanItem(item, day);
        allMealsMap.get(day).remove(item.getName());
        allMeals.put(day, new ArrayList<MealPlanItem>(allMealsMap.get(day).values()));
        System.out.println(allMeals.get(day));
        adapters.put(day, new MealPlanItemAdapter(allMeals.get(day)));
        adapters.get(day).setDay(day);
        recyclerViews.get(day).setAdapter(adapters.get(day));
        fragments.get(day).setDataList(allMeals.get(day));
    }

    /**
     * Allows users to delete Meal Plan Item and updates adapters accordingly
     *<ul>
     *  <li>this will create meal plan items from recipe click and adds to the database</li>
     *  <li>Adds item to all meals arrays</li>
     *  <li>Adds item to all meals hashmaps</li>
     *  <li>Re initializes adapters for recycler views</li>
     *</ul>
     *
     * @param item the Meal Plan Item to delete from database
     * @param day String of date to update
     * @see         MealPlanItem
     */
    public void deleteMealPlanItem(MealPlanItem item, String day) {


        DocumentReference ref = MealPlanCollec.document(day);
        Map<String,Object> updates = new HashMap<>();
        updates.put(item.getName(), FieldValue.delete());
        ref.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println("Success");
            }
        });
    }

}



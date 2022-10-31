package com.example.lunchmunch;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;

import com.example.lunchmunch.databinding.ActivityRecipeBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeActivity extends AppCompatActivity {

    private ActivityRecipeBinding binding;
    Button IngredientsNav, MealPlanNav, ShoppingListNav;

    ArrayList<Recipe> recipesList;
    Map<String, Recipe> recipesMap;

    FirebaseFirestore db;
    CollectionReference RecipeCollec;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // init firebase reference
        db = FirebaseFirestore.getInstance();
        RecipeCollec = db.collection("Recipes");

        initDBListener(RecipeCollec);
        initViews();

        IngredientsNav.setOnClickListener(view -> {
            System.out.println("rec_list: "+recipesList);
            //startActivity(new Intent(RecipeActivity.this, IngredientsActivity.class));
        });

        MealPlanNav.setOnClickListener(view -> {
            startActivity(new Intent(RecipeActivity.this, MealPlanActivity.class));
        });

        ShoppingListNav.setOnClickListener(view -> {
            startActivity(new Intent(RecipeActivity.this, ShoppingListActivity.class));
        });

    }

    private void initDBListener(CollectionReference recipeCollec) {

        recipeCollec.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    // clear current list and re-add all the current ingredients
                    //recipesList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Recipe recipe = document.toObject(Recipe.class);

                        /*
                        // convert document objects back into Ingredient class objects
                        for (Object data : document.getData().values()) {


                            HashMap<String, Object> recipeData = (HashMap<String, Object>) data;

                            String name = (String) recipeData.get("name");

                            // only stores ingredients food name then fetch from ingredients collection for object

                            List<String> ingredientNames = (List<String>) recipeData.get("ingredients");


                            // no idea if this will work properly
                            List<Ingredient> ingredients = getIngredients(ingredientNames, new DBIngredients() {
                                @Override
                                public List<Ingredient> onSuccess(List<Ingredient> ingredientList) {
                                    return ingredientList;
                                }
                            });


                            String instructions = (String) recipeData.get("instructions");
                            String mealType = (String) recipeData.get("mealType");
                            String image = (String) recipeData.get("image");
                            Integer servings = (Integer) recipeData.get("servings");
                            Integer prepTime = (Integer) recipeData.get("prepTime");


                            Recipe recipe = new Recipe(name, ingredients, instructions, mealType, image, servings, prepTime);

                            // add to map for unique ingredient entries
                            //recipesMap.put(name, recipe);

                            // add to arraylist for adapter
                            recipesList.add(recipe);
                        }

                         */
                        // no idea if this will work properly
                        List<String> ingredientNames = recipe.getIngredientNames();
                        List<Ingredient> ingredients = getIngredients(ingredientNames, new DBIngredients() {
                            @Override
                            public List<Ingredient> onSuccess(List<Ingredient> ingredientList) {
                                return ingredientList;
                            }
                        });

                        Recipe updatedRecipe = new Recipe(
                                recipe.getName(),
                                ingredients, //update Recipe with the list of Ingredients as Ingredients class instances
                                recipe.getIngredientNames(),
                                recipe.getInstructions(),
                                recipe.getMealType(),
                                recipe.getImage(),
                                recipe.getServings(),
                                recipe.getPrepTime()
                        );

                        recipesList.add(updatedRecipe);


                    }
                    //RecipeAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private interface DBIngredients {
        List<Ingredient> onSuccess(List<Ingredient> ingredientList);
    }

    private List<Ingredient> getIngredients(List<String> ingredientNames, DBIngredients dbIngredients ) {

        ArrayList<Ingredient> ingredientsList = new ArrayList<Ingredient>();

        //this should get List<Ingredient> without any errors
        // to make sure it is error-less we make sure that when adding an ingredient to a recipe it is in the Ingredients database first
        // also make sure that before deleting an ingredient from the database that it is not used in a recipe


        db.collection("Ingredients").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Ingredient ingredient = document.toObject(Ingredient.class);
                        /*
                        // convert document objects back into Ingredient class objects
                        for (Object data : document.getData().values()) {
                            HashMap<String, Object> foodData = (HashMap<String, Object>) data;

                            String name = (String) foodData.get("name");
                            String description = (String) foodData.get("description");
                            Timestamp timestamp = (Timestamp) foodData.get("bestBefore");
                            Date bestBefore = timestamp.toDate();
                            Location location = Location.valueOf(foodData.get("location").toString().toUpperCase());
                            Integer count = ((Long) foodData.get("count")).intValue();
                            Integer cost = ((Long) foodData.get("cost")).intValue();
                            IngredientCategory category = IngredientCategory.valueOf(foodData.get("category").toString().toUpperCase());

                            Ingredient ingredient = new Ingredient(name, description, bestBefore, location, count, cost, category);

                            ingredientsList.add(ingredient);
                            //https://stackoverflow.com/questions/66071922/how-to-wait-for-an-async-method-to-complete-before-the-return-statement-runs
                            dbIngredients.onSuccess(ingredientsList);
                        }

                         */
                        ingredientsList.add(ingredient);
                        dbIngredients.onSuccess(ingredientsList);
                    }
                } else {
                    System.out.println("get failed with " + task.getException());
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        // this will usually return before onComplete completes its call so use interface to update with onSuccess method
        return ingredientsList;
    }


    private void initViews() {
        IngredientsNav = findViewById(R.id.ingredientsNav);
        MealPlanNav = findViewById(R.id.mealPlanNav);
        ShoppingListNav = findViewById(R.id.shoppingListNav);
    }

}
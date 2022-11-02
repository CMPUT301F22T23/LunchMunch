package com.example.lunchmunch;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lunchmunch.databinding.ActivityRecipeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeActivity extends AppCompatActivity implements RecipeFragment.OnFragmentInteractionListener {

    private ActivityRecipeBinding binding;
    Button IngredientsNav, MealPlanNav, ShoppingListNav;
    FloatingActionButton AddRecipeButton;
    ArrayList<Recipe> recipesList;
    ListView recipesView;
    TextView sortText;
    Map<String, Recipe> recipesMap;
    RecipeItemAdapter RecipeAdapter;
    FirebaseFirestore db;
    CollectionReference RecipeCollec;
    RecipeFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        recipesList = new ArrayList<>();

        RecipeAdapter = new RecipeItemAdapter(this, R.layout.content_recipe, recipesList);
        recipesView.setAdapter(RecipeAdapter);
        recipesMap = new HashMap<>();
        // init firebase reference
        db = FirebaseFirestore.getInstance();
        RecipeCollec = db.collection("Recipes");

        initDBListener(RecipeCollec);

        IngredientsNav.setOnClickListener(view -> {
            startActivity(new Intent(RecipeActivity.this, IngredientsActivity.class));
        });

        MealPlanNav.setOnClickListener(view -> {
            startActivity(new Intent(RecipeActivity.this, MealPlanActivity.class));
        });

        ShoppingListNav.setOnClickListener(view -> {
            startActivity(new Intent(RecipeActivity.this, ShoppingListActivity.class));
        });

        // Add Recipe button set on click listener to add fragment
        AddRecipeButton.setOnClickListener(view -> {
           RecipeFragment recipeFragment = new RecipeFragment();
            recipeFragment.show(getSupportFragmentManager(), "Add Recipe");
        });

        recipesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe recipe = recipesList.get(i);
                Bundle bundle = new Bundle();
                bundle.putInt("position", i);
                RecipeModalFragment recipeModalFragment = new RecipeModalFragment(recipe);
                recipeModalFragment.setArguments(bundle);
                recipeModalFragment.show(getSupportFragmentManager(), "Show Recipe");
                view.refreshDrawableState();
            }
        });

        sortText.setOnClickListener(view -> {
            RecipeSortFragment recipeSortFragment = new RecipeSortFragment();
            recipeSortFragment.show(getSupportFragmentManager(), "Sort Recipes");

        });



    }



    @Override
    public void onOkPressed(Recipe recipe, Boolean isNew, int position) {
        if (isNew) {
            RecipeCollec.document().set(recipe) // .add equiv to .collec().set(..)
                    .addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            recipe.setId(RecipeCollec.document().getId());
                            recipesList.add(recipe);
                            RecipeAdapter.notifyDataSetChanged();
                            Log.d("", "DocumentSnapshot written with ID: " + recipe.getName());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Fail");
                            //Log.w(TAG, "Error adding document", e);
                        }
                    });

        } else {

            RecipeCollec.document(recipe.getId()).set(recipe)
                    .addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            recipesList.set(position, recipe);
                            RecipeAdapter.notifyDataSetChanged();
                            Log.d("", "DocumentSnapshot written with ID: " + recipe.getName());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Fail");
                            //Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
    }

    private void initDBListener(CollectionReference recipeCollec) {

        recipeCollec.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    // clear current list and re-add all the current ingredients
                    recipesList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Make sure we add our ID to the recipe class for later use
                        Recipe recipe = document.toObject(Recipe.class);
                        recipe.setId(document.getId());
                        List<String> ingredientNames = recipe.getIngredientNames();
                        try {
                            // Try to add our ingredient class if possible
                            List<Ingredient> ingredients = getIngredients(ingredientNames, new DBIngredients() {
                                @Override
                                public List<Ingredient> onSuccess(List<Ingredient> ingredientList) {
                                    return ingredientList;
                                }
                            });
                            Recipe updatedRecipe = new Recipe(
                                    recipe.getId(),
                                    recipe.getName(),
                                    ingredients, //update Recipe with the list of Ingredients as Ingredients class instances
                                    recipe.getIngredientNames(),
                                    recipe.getInstructions(),
                                    recipe.getMealType(),
                                    recipe.getImage(),
                                    recipe.getServings(),
                                    recipe.getPrepTime(),
                                    recipe.getComments()
                            );

                            recipesList.add(updatedRecipe);

                        } catch (Exception e) {
                            recipesList.add(recipe);
                        }

                    }
                    RecipeAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    public void onFragmentInteraction(Recipe recipe) {
        // add recipe to database
        RecipeCollec.document(recipe.getName()).set(recipe) // .add equiv to .collec().set(..)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Success");
                            //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        } else {
                            System.out.println("Fail");
                            //Log.w(TAG, "Error adding document", e);
                        }
                    }
                });
    }

    public void sortRecipes(String sortType, String sortDirection) {
        if (sortType.equals("Title")) {
            Collections.sort(recipesList, new Comparator<Recipe>() {
                @Override
                public int compare(Recipe recipe, Recipe t1) {
                    return recipe.getName().compareTo(t1.getName());
                }
            });
        } else if (sortType.equals("Recipe Category")) {
            Collections.sort(recipesList, new Comparator<Recipe>() {
                @Override
                public int compare(Recipe recipe, Recipe t1) {
                    return recipe.getMealType().compareTo(t1.getMealType());
                }
            });
        } else if (sortType.equals("Preparation Time")) {
            Collections.sort(recipesList, new Comparator<Recipe>() {
                @Override
                public int compare(Recipe recipe, Recipe t1) {
                    return recipe.getPrepTime().compareTo(t1.getPrepTime());
                }
            });
        } else if (sortType.equals("Number of Servings")) {
            Collections.sort(recipesList, new Comparator<Recipe>() {
                @Override
                public int compare(Recipe recipe, Recipe t1) {
                    return recipe.getServings().compareTo(t1.getServings());
                }
            });
        }
        if (sortDirection.equals("Descending")) {
            Collections.reverse(recipesList);
        }
        RecipeAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteRecipe(int position) {
        // delete recipe from database
        Recipe recipe = recipesList.get(position);
        RecipeCollec.document(recipe.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Success");
                            //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        } else {
                            System.out.println("Fail");
                            //Log.w(TAG, "Error adding document", e);
                        }
                    }
                });
        recipesList.remove(position);
        RecipeAdapter.notifyDataSetChanged();
    }

    private interface DBIngredients {
        List<Ingredient> onSuccess(List<Ingredient> ingredientList);
    }

    private List<Ingredient> getIngredients(List<String> ingredientNames, DBIngredients dbIngredients ) {

        // delete this for sharedPreferences pull I think
        ArrayList<Ingredient> ingredientsList = new ArrayList<Ingredient>();

        //this should get List<Ingredient> without any errors
        // to make sure it is error-less we make sure that when adding an ingredient to a recipe it is in the Ingredients database first
        // also make sure that before deleting an ingredient from the database that it is not used in a recipe


        db.collection("Ingredients").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Ingredient ingredient = document.toObject(Ingredient.class);

                        // convert document objects back into Ingredient class objects
                        Ingredient ingredient = null;
                        for (Object data : document.getData().values()) {
                            // Check if data is not an instance of map if so then continue

                            if (!(data instanceof Map)) {
                                continue;
                            }

                            HashMap<String, Object> foodData = (HashMap<String, Object>) data;

                            String name = (String) foodData.get("name");
                            String description = (String) foodData.get("description");
                            Timestamp timestamp = (Timestamp) foodData.get("bestBefore");
                            Date bestBefore = timestamp.toDate();
                            Location location = Location.valueOf(foodData.get("location").toString().toUpperCase());
                            Integer count = ((Long) foodData.get("count")).intValue();
                            Integer cost = ((Long) foodData.get("cost")).intValue();
                            IngredientCategory category = IngredientCategory.valueOf(foodData.get("category").toString().toUpperCase());
                            ingredient = new Ingredient(name, description, bestBefore, location, count, cost, category);

                            ingredientsList.add(ingredient);
                            //https://stackoverflow.com/questions/66071922/how-to-wait-for-an-async-method-to-complete-before-the-return-statement-runs
                            dbIngredients.onSuccess(ingredientsList);
                        }
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
        AddRecipeButton = findViewById(R.id.addRecipeButton);
        recipesView = findViewById(R.id.recipeListView);
        sortText = findViewById(R.id.recipeSortText);

    }

}
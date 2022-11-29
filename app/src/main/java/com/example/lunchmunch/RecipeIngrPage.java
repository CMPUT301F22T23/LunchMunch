package com.example.lunchmunch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * This is the page where the user can add ingredients to the recipe they are creating
 */

public class RecipeIngrPage extends AppCompatActivity implements IngredientItemFragment.OnFragmentInteractionListener {

    FoodItemAdapter existingIngrAdapter;
    static ArrayList<Ingredient> ingredientsList;
    ArrayList<Ingredient> temp;
    ListView ingredientsListView;
    FloatingActionButton addIngredients;
    FloatingActionButton save;
    FloatingActionButton cancel;
    IngredientItemFragment fragment;
    IngredientItemFragment fragment2;

    ArrayList<String> foodNames;
    Integer itemPosition;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        // Displays the initial ingredients to be added into the recipe
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_ingredients_view);
        initViews();

        // Ingredients Lists
        Recipe recipe = (Recipe) getIntent().getSerializableExtra("Recipe");
        // sets the ingredient list to empty if the recipe is new
        // else set it to the list of ingredients belonging to recipe instance
        if (recipe != null) {
            ingredientsList = (ArrayList<Ingredient>) recipe.getIngredients();
        } else {
            ingredientsList = new ArrayList<>();
        }


        //ingredientsList = new ArrayList<Ingredient>();
        existingIngrAdapter = new FoodItemAdapter(this, R.layout.content_ingredients, ingredientsList);
        ingredientsListView.setAdapter(existingIngrAdapter);

        // fragments for new and existing ingredients
        fragment = new IngredientItemFragment();
        fragment2 = new IngredientItemFragment();

        foodNames = new ArrayList<String>(); // used to store unique ingredient names

        // pulls up fragment to add ingredients
        addIngredients.setOnClickListener(view -> {
            fragment.show(getSupportFragmentManager(), "Add Ingredient");
            addIngredients.setVisibility(View.VISIBLE);
        });


        ingredientsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // pulls up fragment and populates it with selected ingredient attributes
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemPosition = i;
                Bundle args = new Bundle();
                args.putParcelable("currentIngredient", ingredientsList.get(itemPosition));
                args.putInt("currentIngredientPosition", itemPosition);
                System.out.println("item position: " + itemPosition);
                System.out.println("ingredient name: " + ingredientsList.get(itemPosition).getName());
                fragment2.setArguments(args);
                fragment2.show(getSupportFragmentManager(), "Add_Ingredient");
                view.refreshDrawableState();
            }
        });







        save.setOnClickListener(new View.OnClickListener() {
            @Override
            //Returns to the recipe fragment and returns the list of added ingredients
            public void onClick(View view) {
                assert recipe != null;
                recipe.setIngredientsClass(ingredientsList);
                Intent result = new Intent();
                result.putExtra("Recipe", recipe);
                setResult(Activity.RESULT_OK, result);
                finish();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            // cancels the add ingredients to the recipe
            public void onClick(View view) {
                finish();
            }
        });

    }


    /**
     *
     * @param ingredient Is the added/edited ingredient and puts it into the ingredients list for the page
     * @param position The position of the added/edited ingredient
     */
    @Override
    public void onOkPressed(Ingredient ingredient, int position) {

        // update existing ingredient
        if(position != -1 ){
            ingredientsList.set(position, ingredient);
            existingIngrAdapter.notifyDataSetChanged();
            return;
        }

        // if the ingredient is new then add it to the list
        if(!foodNames.contains(ingredient.getName())){
            foodNames.add(ingredient.getName());
            ingredientsList.add(ingredient);
            existingIngrAdapter.notifyDataSetChanged();
        }


    }

    /**
     * Handles deleting the ingredient
     */
    @Override
    public void deleteIngredient() {
        if (itemPosition == null) {
            return;
        }
        String name = ingredientsList.get(itemPosition).getName();

        Log.d("ITEM POSITION", "Position is: " + String.valueOf(itemPosition));
        if (foodNames.contains(name)) {
            foodNames.remove(name);
            ingredientsList.remove(ingredientsList.get(itemPosition));
            existingIngrAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Initializes buttons and views on the page
     */
    public void initViews(){

        ingredientsListView = (ListView) findViewById(R.id.ingredient_list);
        addIngredients = findViewById(R.id.addIngredientsButton);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        addIngredients = findViewById(R.id.addIngredientsButton);

    }
}

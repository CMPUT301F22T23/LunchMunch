package com.example.lunchmunch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.base.MoreObjects;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.internal.InternalTokenProvider;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* stuff left:
- add button in recipeIngr adds to listview/list
- save button sets the recipe ingredients list to list of ingredients from the recipe ingr page
- textview in recipe fragment displays names (and maybe count) of ingredients in the recipe list
- reformat buttons on recipeingr page
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
        System.out.println(recipe);
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

        addIngredients.setOnClickListener(view -> fragment.show(getSupportFragmentManager(), "Add Ingredients"));

        ingredientsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemPosition = i;
                Bundle args = new Bundle();
                args.putParcelable("currentIngredient", ingredientsList.get(itemPosition));
                args.putInt("currentIngredientPosition", itemPosition);
                fragment2.setArguments(args);
                fragment2.show(getSupportFragmentManager(), "Add_Ingredient");
                view.refreshDrawableState();
            }
        });







        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert recipe != null;
                recipe.setIngredientsClass(ingredientsList);
                Intent result = new Intent();
                result.putExtra("Recipe", recipe);
                setResult(Activity.RESULT_OK, result);
                System.out.println("list: " + recipe.getIngredients());

                finish();

            }
    });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }




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


    @Override
    public void deleteIngredient() {
        if (itemPosition == null) {
            System.out.println(ingredientsList);
            return;
        }
        String name = ingredientsList.get(itemPosition).getName();

        Log.d("ITEM POSITION", "Position is: " + String.valueOf(itemPosition));
        if (foodNames.contains(name)) {
            System.out.println("poo " + ingredientsList);
            foodNames.remove(name);
            ingredientsList.remove(ingredientsList.get(itemPosition));
            existingIngrAdapter.notifyDataSetChanged();
        } else {
            ingredientsList.clear();
            System.out.println("nothing in list " + ingredientsList);
            existingIngrAdapter.notifyDataSetChanged();
            return;
        }
    }

    public void initViews(){

        ingredientsListView = (ListView) findViewById(R.id.ingredient_list);
        addIngredients = findViewById(R.id.addIngredientsButton);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        addIngredients = findViewById(R.id.addIngredientsButton);

    }
}

package com.example.lunchmunch;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/* stuff left:
- add button in recipeIngr adds to listview/list
- save button sets the recipe ingredients list to list of ingredients from the recipe ingr page
- textview in recipe fragment displays names (and maybe count) of ingredients in the recipe list
- reformat buttons on recipeingr page
 */

public class RecipeIngrPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FoodItemAdapter existingIngr;
    static ArrayList<Ingredient> ingredients;
    ListView ingredientsList;
    FloatingActionButton addIngredients;
    ImageButton save;
    IngredientItemFragment fragment;
    IngredientItemFragment fragment2;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // displays the initial ingredients to be added into the recipe
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_ingredients_view);
        initViews();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // should set recipe list from recipe fragment to the ingredients list on the page
                finish();
            }
    });




    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // pull up ingredient fragment with ingredient info

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void initViews(){

        ingredientsList = findViewById(R.id.ingredient_list);
        addIngredients = findViewById(R.id.addIngredientsButton);
        save = findViewById(R.id.save);

    }
}

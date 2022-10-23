package com.example.lunchmunch;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button ingredientsButton;
    private Button recipesButton;
    private Button mealButton;
    private Button shoppingButton;

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ingredientsButton = (Button) findViewById(R.id.ingredients);
        recipesButton = (Button) findViewById(R.id.recipes);
        mealButton = (Button) findViewById(R.id.meal_plan);
        shoppingButton = (Button) findViewById(R.id.shopping_list);

        ingredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIngredients();
            }
        });


        recipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecipes();
            }
        });

        /*

        mealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMeal();
            }
        });

        shoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShopping();
            }
        });
        */
    }

    public void openIngredients() {
        Intent intent = new Intent(this, IngredientsActivity.class);
        startActivity(intent);
    }

    public void openRecipes() {
        Intent intent = new Intent(this, RecipeActivity.class);
        startActivity(intent);
    }

}
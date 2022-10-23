package com.example.lunchmunch;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button ingredientsButton;
    private Button recipesButton;
    private Button mealButton;
    private Button shoppingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setContentView(R.layout.content_main);
        super.onCreate(savedInstanceState);


        initViews();

        ingredientsButton = findViewById(R.id.ingredients);
        recipesButton = findViewById(R.id.recipes);
        mealButton = findViewById(R.id.meal_plan);
        shoppingButton = findViewById(R.id.shopping_list);

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

    }

    public void openIngredients() {
        Intent intent = new Intent(this, IngredientsActivity.class);
        startActivity(intent);
    }

    public void openRecipes() {
        Intent intent = new Intent(this, RecipeActivity.class);
        startActivity(intent);
    }

    public void openMeal() {
        Intent intent = new Intent(this, MealPlanActivity.class);
        startActivity(intent);
    }

    public void openShopping() {
        Intent intent = new Intent(this, ShoppingListActivity.class);
        startActivity(intent);
    }

    private void initViews() {
        ingredientsButton = findViewById(R.id.ingredients);
        recipesButton = findViewById(R.id.recipes);
        mealButton = findViewById(R.id.meal_plan);
        shoppingButton = findViewById(R.id.shopping_list);

    }

}
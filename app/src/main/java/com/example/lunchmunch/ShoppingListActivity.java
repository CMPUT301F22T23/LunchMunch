package com.example.lunchmunch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ShoppingListActivity extends AppCompatActivity {

    Button IngredientsNav, RecipesNav, MealPlanNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intiViews();

        IngredientsNav.setOnClickListener(view -> {
            startActivity(new Intent(ShoppingListActivity.this, IngredientsActivity.class));
        });

        RecipesNav.setOnClickListener(view -> {
            startActivity(new Intent(ShoppingListActivity.this, RecipesActivity.class));
        });

        MealPlanNav.setOnClickListener(view -> {
            startActivity(new Intent(ShoppingListActivity.this, MealPlanActivity.class));
        });


    }

    private void intiViews() {
        IngredientsNav = findViewById(R.id.ingredientsNav);
        RecipesNav = findViewById(R.id.recipesNav);
        MealPlanNav = findViewById(R.id.mealPlanNav);
    }
}

package com.example.lunchmunch;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class IngredientsActivity extends AppCompatActivity {

    Button RecipesNav, MealPlanNav, ShoppingListNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        intiViews();

        RecipesNav.setOnClickListener(view -> {
            startActivity(new Intent(IngredientsActivity.this, RecipesActivity.class));
        });

        MealPlanNav.setOnClickListener(view -> {
            startActivity(new Intent(IngredientsActivity.this, MealPlanActivity.class));
        });

        ShoppingListNav.setOnClickListener(view -> {
            startActivity(new Intent(IngredientsActivity.this, ShoppingListActivity.class));
        });


    }

    private void intiViews() {
        RecipesNav = findViewById(R.id.recipesNav);
        MealPlanNav = findViewById(R.id.mealPlanNav);
        ShoppingListNav = findViewById(R.id.shoppingListNav);
    }

}


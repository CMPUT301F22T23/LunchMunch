package com.example.lunchmunch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MealPlanActivity extends AppCompatActivity {

    Button IngredientsNav, RecipesNav, ShoppingListNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intiViews();

        IngredientsNav.setOnClickListener(view -> {
            startActivity(new Intent(MealPlanActivity.this, IngredientsActivity.class));
        });

        RecipesNav.setOnClickListener(view -> {
            startActivity(new Intent(MealPlanActivity.this, IngredientsActivity.class));
        });

        ShoppingListNav.setOnClickListener(view -> {
            startActivity(new Intent(MealPlanActivity.this, ShoppingListActivity.class));
        });


    }

    private void intiViews() {
        IngredientsNav = findViewById(R.id.ingredientsNav);
        RecipesNav = findViewById(R.id.recipesNav);
        ShoppingListNav = findViewById(R.id.shoppingListNav);
    }

}



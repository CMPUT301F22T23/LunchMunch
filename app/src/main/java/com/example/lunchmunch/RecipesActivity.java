package com.example.lunchmunch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RecipesActivity extends AppCompatActivity {

    Button IngredientsNav, MealPlanNav, ShoppingListNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intiViews();

        IngredientsNav.setOnClickListener(view -> {
            startActivity(new Intent(RecipesActivity.this, IngredientsActivity.class));
        });

        MealPlanNav.setOnClickListener(view -> {
            startActivity(new Intent(RecipesActivity.this, MealPlanActivity.class));
        });

        ShoppingListNav.setOnClickListener(view -> {
            startActivity(new Intent(RecipesActivity.this, ShoppingListActivity.class));
        });


    }

    private void intiViews() {
        IngredientsNav = findViewById(R.id.ingredientsNav);
        MealPlanNav = findViewById(R.id.mealPlanNav);
        ShoppingListNav = findViewById(R.id.shoppingListNav);
    }

}

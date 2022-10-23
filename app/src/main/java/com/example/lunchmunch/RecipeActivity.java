package com.example.lunchmunch;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.lunchmunch.databinding.ActivityRecipeBinding;

public class RecipeActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityRecipeBinding binding;
    Button IngredientsNav, MealPlanNav, ShoppingListNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        initViews();

        IngredientsNav.setOnClickListener(view -> {
            startActivity(new Intent(RecipeActivity.this, IngredientsActivity.class));
        });

        MealPlanNav.setOnClickListener(view -> {
            startActivity(new Intent(RecipeActivity.this, MealPlanActivity.class));
        });

        ShoppingListNav.setOnClickListener(view -> {
            startActivity(new Intent(RecipeActivity.this, ShoppingListActivity.class));
        });
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_recipe);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_recipe);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }






    private void initViews() {
        IngredientsNav = findViewById(R.id.ingredientsNav);
        MealPlanNav = findViewById(R.id.mealPlanNav);
        ShoppingListNav = findViewById(R.id.shoppingListNav);
    }

}
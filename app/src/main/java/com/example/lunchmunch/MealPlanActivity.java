package com.example.lunchmunch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Main activity for all MealPlan functionality
 */
public class MealPlanActivity extends AppCompatActivity implements MealPlanDateFragment.OnFragmentInteractionListener {

    Button IngredientsNav, RecipesNav, ShoppingListNav;
    MealPlanItemAdapter adapter;
    static ArrayList<MealPlanItem> mealPlanList = new ArrayList<>();
    MealPlanDateFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealplan_activity);

        intiViews();

        IngredientsNav.setOnClickListener(view -> {
            startActivity(new Intent(MealPlanActivity.this, IngredientsActivity.class));
        });

        RecipesNav.setOnClickListener(view -> {
            startActivity(new Intent(MealPlanActivity.this, RecipeActivity.class));
        });

        ShoppingListNav.setOnClickListener(view -> {
            startActivity(new Intent(MealPlanActivity.this, ShoppingListActivity.class));
        });

        RecyclerView recyclerView = findViewById(R.id.monday_meal_plan_items_list);
        ArrayList<String> ingredients = new ArrayList<String>();
        //Recipe recipe = new Recipe("name", ingredients, "instructions", "mealType", "image", 0, 0, "comments");
        //mealPlanList.add(new MealPlanItem(recipe));
        Ingredient banana =  new Ingredient("banana", "yellow fruit", new Date(), Location.FREEZER, 1,2,IngredientCategory.MEAT);
        mealPlanList.add(new MealPlanItem(banana));
        adapter = new MealPlanItemAdapter(mealPlanList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        // adding or editing a new item button
        final ImageView editMealPlanButton = findViewById(R.id.monday_meal_plan_edit_pencil);
        fragment = new MealPlanDateFragment();
        editMealPlanButton.setOnClickListener(view -> fragment.show(getSupportFragmentManager(), "Edit Meal Plan"));
        fragment.setDataList(mealPlanList);


    }

    private void intiViews() {
        IngredientsNav = findViewById(R.id.ingredientsNav);
        RecipesNav = findViewById(R.id.recipesNav);
        ShoppingListNav = findViewById(R.id.shoppingListNav);

    }


}



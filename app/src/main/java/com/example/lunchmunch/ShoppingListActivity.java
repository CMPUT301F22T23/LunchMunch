package com.example.lunchmunch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 * Main activity for all ShoppingList functionality
 */
public class ShoppingListActivity extends AppCompatActivity {

    Button IngredientsNav, RecipesNav, MealPlanNav;
    ArrayList<Ingredient> shoppingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppinglist_activity);

        // updates shopping list with the needed ingredients
        getNeededIngrs();

        initViews();

        IngredientsNav.setOnClickListener(view -> {
            startActivity(new Intent(ShoppingListActivity.this, IngredientsActivity.class));
        });

        RecipesNav.setOnClickListener(view -> {
            startActivity(new Intent(ShoppingListActivity.this, RecipeActivity.class));
        });

        MealPlanNav.setOnClickListener(view -> {
            startActivity(new Intent(ShoppingListActivity.this, MealPlanActivity.class));
        });


    }

    private void getNeededIngrs() {
        //ArrayList<MealPlanItem> mealPlanList = MealPlanActivity.mealPlanList;

        // this hashmap allows us to get the total count needed for each ingredient in our mealplan
        // ingredient_name: ingredient
        HashMap<String, Integer> ingrMap = new HashMap<String, Integer>();

        // iterate over every mealPlanItem in the mealPlanList and get ingredients needed
        for (MealPlanItem mealPlanItem : MealPlanActivity.mealPlanList) {

            if (mealPlanItem.getType() == MealPlanItemType.RECIPE) {
                // wouldnt let me put mealPlanItem.getIngredients() in for loop without changing for loop class to Object
                List<Ingredient> ingredients = mealPlanItem.getIngredients();
                for(Ingredient ingredient : ingredients) {
                    String ingrName = ingredient.getName();
                    Integer ingrCount = ingredient.getCount();
                    // if the ingr not in the map then init with its count, otherwise if ingr already in map then just add this instance of the ingr's count to the count thats already in the map same as here( https://stackoverflow.com/a/37705877/17304003)
                    ingrMap.put(ingrName, ingrMap.getOrDefault(ingrName, ingrCount) + ingrCount);
                }

            } else if (mealPlanItem.getType() == MealPlanItemType.INGREDIENT) {
                String ingrName = mealPlanItem.getName();
                Integer ingrCount = mealPlanItem.getCount();
                ingrMap.put(ingrName, ingrMap.getOrDefault(ingrName, ingrCount) + ingrCount);
            }
        }

        // now we have a map that contains the total count of all ingredients needed
        // subtract the count of ingredients we already have from the Ingredients page
        // the remaining ingredients get added to the shopping list

        for (Ingredient ingredient : IngredientsActivity.ingredientsList) {
            String ingrName = ingredient.getName();
            Integer ingrCount = ingredient.getCount();
            if (ingrMap.containsKey(ingrName)) {
                Integer neededIngrCount = ingrMap.get(ingrName);
                // if we have enough of this specific ingredient then remove it from the hash (dont add it to the shopping list)
                // ingr.getCount() => neededIngrCount
                if (ingrCount > neededIngrCount || ingrCount.equals(neededIngrCount)) {
                    // remove specific ingr from the hashmap
                    ingrMap.remove(ingrName);

                    // could do just else here if we want (but this easier to read)
                } else if (ingrCount < neededIngrCount){
                    // update ingr in map to the count needed (needed from meal plan - already have from ingredients
                    ingrMap.put(ingrName, ingrMap.get(ingrName) - ingrCount);
                }
            }
        }

        //now the remaining values in the ingrMap are the ingredients we need in the shopping list
        System.out.println("dif"+ingrMap);

    }

    private void initViews() {
        IngredientsNav = findViewById(R.id.ingredientsNav);
        RecipesNav = findViewById(R.id.recipesNav);
        MealPlanNav = findViewById(R.id.mealPlanNav);
    }
}

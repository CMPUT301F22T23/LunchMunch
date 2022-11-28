package com.example.lunchmunch;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

/**
 * Sorts recipes list and ingredients list based on spinner input from activity, spinners are from Ingredients Activity, Recipe Activity, and Shopping List Activity
 */
public class Sort {

    /**
     *
     * @param dataList the list of ingredients that is passed in and sorted
     * @param choice the type of sort to be done, this is inputted from the spinner in IngredientsActivity and ShoppingList Activity
     * @return None, the collections.sort returns a comparison in which the positions of objects are changed
     */
    public static void ingredientSort(ArrayList<Ingredient> dataList, String choice){

        if (choice.equals("Description")){
            Collections.sort(dataList, new Comparator<Ingredient>() {
                @Override
                public int compare(Ingredient ingredient, Ingredient ingredient2) {
                    return ingredient.getDescription().toLowerCase(Locale.ROOT).compareTo(ingredient2.getDescription().toLowerCase(Locale.ROOT));
                }
            });

        } else if (choice.equals("Best Before")){
            Collections.sort(dataList, new Comparator<Ingredient>() {
                @Override
                public int compare(Ingredient ingredient, Ingredient ingredient2) {
                    return ingredient.getBestBefore().compareTo(ingredient2.getBestBefore());
                }
            });

        } else if (choice.equals("Location")){
            Collections.sort(dataList, new Comparator<Ingredient>() {
                @Override
                public int compare(Ingredient ingredient, Ingredient ingredient2) {
                    return ingredient.getLocation().toString().compareTo(ingredient2.getLocation().toString());
                }
            });

        } else if (choice.equals("Category")){
            Collections.sort(dataList, new Comparator<Ingredient>() {
                @Override
                public int compare(Ingredient ingredient, Ingredient ingredient2) {
                    return ingredient.getCategory().toString().compareTo(ingredient2.getCategory().toString());
                }
            });
        }

    }

    /**
     *
     * @param recipesList the list of recipes to be passed in for sorting
     * @param sortType the type of sort to be done, this is taken from the spinner in the RecipeActivity
     */
    public static void recipeSort(ArrayList<Recipe> recipesList, String sortType){
        if (sortType.equals("Title")) {
            Collections.sort(recipesList, new Comparator<Recipe>() {
                @Override
                public int compare(Recipe recipe, Recipe t1) {
                    return recipe.getName().toLowerCase(Locale.ROOT).compareTo(t1.getName().toLowerCase(Locale.ROOT));
                }
            });
        } else if (sortType.equals("Category")) {
            Collections.sort(recipesList, new Comparator<Recipe>() {
                @Override
                public int compare(Recipe recipe, Recipe t1) {
                    return recipe.getMealType().toLowerCase(Locale.ROOT).compareTo(t1.getMealType().toLowerCase(Locale.ROOT));
                }
            });
        } else if (sortType.equals("Prep Time")) {
            Collections.sort(recipesList, new Comparator<Recipe>() {
                @Override
                public int compare(Recipe recipe, Recipe t1) {
                    return recipe.getPrepTime().compareTo(t1.getPrepTime());
                }
            });
        } else if (sortType.equals("# of Servings")) {
            Collections.sort(recipesList, new Comparator<Recipe>() {
                @Override
                public int compare(Recipe recipe, Recipe t1) {
                    return recipe.getServings().compareTo(t1.getServings());
                }
            });
        }

    }
}

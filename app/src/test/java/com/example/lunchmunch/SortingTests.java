package com.example.lunchmunch;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Tests to see if the sorting methods properly sort the recipe list and ingredients list in their respective activities
 */

public class SortingTests {
    // initialize lists
    ArrayList<Ingredient> ingrList = new ArrayList<Ingredient>();
    ArrayList<String> descriptions = new ArrayList<String>();
    ArrayList<Date> dates = new ArrayList<Date>();
    ArrayList<Location> locations = new ArrayList<Location>();
    ArrayList<IngredientCategory> categories = new ArrayList<IngredientCategory>();

    ArrayList<Recipe> recipesList = new ArrayList<Recipe>();
    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<Integer> prepTimes = new ArrayList<Integer>();
    ArrayList<Integer> numServings = new ArrayList<Integer>();
    ArrayList<String> recipeCategories = new ArrayList<String>();



    public void initIngr(){
        /**
         * Initializes mock ingredients and populates the mock ingredient list to be used for testing
         */

        // initialize mock data with different values
        IngredientCategory category = IngredientCategory.DAIRY;
        Location location = Location.FRIDGE;

        Date expirationDate = new Date(2022, 12, 5, 0,0);
        Ingredient ingredient = new Ingredient("name", "description", expirationDate, location, 1.0F, 12.0F, category);
        ingrList.add(ingredient);

        category = IngredientCategory.MEAT;
        location = Location.FREEZER;
        expirationDate = new Date(2023, 1, 3, 0,0);
        ingredient = new Ingredient("cheese", "cheesy", expirationDate, location, 4.0F, 14.0F, category);
        ingrList.add(ingredient);

        category = IngredientCategory.GRAIN;
        location = Location.PANTRY;
        expirationDate = new Date(2023, 4, 12, 0,0);
        ingredient = new Ingredient("Bread", "bready", expirationDate, location, 6.0F, 9.0F, category);
        ingrList.add(ingredient);

        category = IngredientCategory.VEGETABLE;
        location = Location.PANTRY;
        expirationDate = new Date(2023, 5, 30, 0,0);
        ingredient = new Ingredient("Peas", "pea", expirationDate, location, 5.0F, 3.0F, category);
        ingrList.add(ingredient);

        category = IngredientCategory.FRUIT;
        location = Location.FRIDGE;
        expirationDate = new Date(2022, 12, 4, 0,0);
        ingredient = new Ingredient("apple", "juicy", expirationDate, location, 4.0F, 14.0F, category);
        ingrList.add(ingredient);

    }

    public void initRecipes(){
        /**
         * Initializes mock recipes and populates the mock recipes list to be used for testing
         */
        List<Ingredient> blankIngredients = new ArrayList<Ingredient>();
        List<String> blankNames = new ArrayList<String>();

        Recipe recipe = new Recipe("Recipe", blankIngredients, blankNames, "","Breakfast","",1,10,"");
        recipesList.add(recipe);

        recipe = new Recipe("Onion Soup", blankIngredients, blankNames, "","Lunch","",7,30,"");
        recipesList.add(recipe);

        recipe = new Recipe("French Toast", blankIngredients, blankNames, "","Snack","",3,25,"");
        recipesList.add(recipe);

        recipe = new Recipe("Steak", blankIngredients, blankNames, "","Dinner","",2,60,"");
        recipesList.add(recipe);

    }

    @Test
    public void IngredientsSortTest(){

        /**
         * Populates arraylists with values in order to compare to ingredients list to check if sorting has indeed been done properly
         */

        // Initializes mock data to test with
        initIngr();

        // Sets string arraylist to descriptions in alphabetical order
        descriptions.add("bready");
        descriptions.add("cheesy");
        descriptions.add("description");
        descriptions.add("juicy");
        descriptions.add("pea");

        // Sets date arraylist to dates in order
        dates.add(ingrList.get(4).getBestBefore());
        dates.add(ingrList.get(0).getBestBefore());
        dates.add(ingrList.get(1).getBestBefore());
        dates.add(ingrList.get(2).getBestBefore());
        dates.add(ingrList.get(3).getBestBefore());

        // Sets location arraylist to locations in order
        locations.add(ingrList.get(1).getLocation());
        locations.add(ingrList.get(0).getLocation());
        locations.add(ingrList.get(4).getLocation());
        locations.add(ingrList.get(3).getLocation());
        locations.add(ingrList.get(2).getLocation());

        // Sets categories arraylist to categories in alphabetical order
        categories.add(ingrList.get(0).getCategory());
        categories.add(ingrList.get(4).getCategory());
        categories.add(ingrList.get(2).getCategory());
        categories.add(ingrList.get(1).getCategory());
        categories.add(ingrList.get(3).getCategory());


        // Sort by description test
        Sort.ingredientSort(ingrList, "Description");
        for (int i = 0; i < ingrList.size(); i++) {
            assertEquals(ingrList.get(i).getDescription(), descriptions.get(i));
        }

        // Sort by Best Before test
        Sort.ingredientSort(ingrList, "Best Before");
        for (int i = 0; i < ingrList.size(); i++) {
            assertEquals(ingrList.get(i).getBestBefore(), dates.get(i));
        }

        // Sort by Location test
        Sort.ingredientSort(ingrList, "Location");
        for (int i = 0; i < ingrList.size(); i++) {
            assertEquals(ingrList.get(i).getLocation(), locations.get(i));
        }

        // Sort by Category test
        Sort.ingredientSort(ingrList, "Category");
        for (int i = 0; i < ingrList.size(); i++) {
            assertEquals(ingrList.get(i).getCategory(), categories.get(i));
        }


    }

    @Test
    public void RecipesSortTest(){
        /**
         * Populates arraylists with values in order to compare to recipes list to check if sorting has indeed been done properly
         */

        initRecipes();

        // Sets the titles arraylist to Titles in alphabetical order
        titles.add(recipesList.get(2).getName());
        titles.add(recipesList.get(1).getName());
        titles.add(recipesList.get(0).getName());
        titles.add(recipesList.get(3).getName());
        System.out.println("Titles " + titles);

        // Sets the prepTime arraylist to prep time in ascending order
        prepTimes.add(recipesList.get(0).getPrepTime());
        prepTimes.add(recipesList.get(2).getPrepTime());
        prepTimes.add(recipesList.get(1).getPrepTime());
        prepTimes.add(recipesList.get(3).getPrepTime());

        // Sets the numServings arraylist to number of servings in ascending order
        numServings.add(recipesList.get(0).getServings());
        numServings.add(recipesList.get(3).getServings());
        numServings.add(recipesList.get(2).getServings());
        numServings.add(recipesList.get(1).getServings());

        // Sets the recipeCategories arraylist to recipe categories in alphabetical order
        recipeCategories.add(recipesList.get(0).getMealType());
        recipeCategories.add(recipesList.get(3).getMealType());
        recipeCategories.add(recipesList.get(1).getMealType());
        recipeCategories.add(recipesList.get(2).getMealType());

        // Sort by Title test
        Sort.recipeSort(recipesList,"Title");
        for (int i = 0; i < recipesList.size(); i++) {
            assertEquals(recipesList.get(i).getName(),titles.get(i));
        }

        // Sort by Prep Time test
        Sort.recipeSort(recipesList,"Prep Time");
        for (int i = 0; i < recipesList.size(); i++) {
            assertEquals(recipesList.get(i).getPrepTime(), prepTimes.get(i) );
        }

        // Sort by # of Servings test
        Sort.recipeSort(recipesList,"# of Servings");
        for (int i = 0; i < recipesList.size(); i++) {
            assertEquals(recipesList.get(i).getServings(), numServings.get(i) );
        }

        // Sort by Category test
        Sort.recipeSort(recipesList,"Category");
        for (int i = 0; i < recipesList.size(); i++) {
            assertEquals(recipesList.get(i).getMealType(), recipeCategories.get(i));
        }


    }



}

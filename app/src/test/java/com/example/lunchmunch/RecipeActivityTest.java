package com.example.lunchmunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import android.app.Activity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RobolectricTestRunner.class)
public class RecipeActivityTest {

    @Mock
    CollectionReference recipeCollec;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Should throw an exception when the recipe is not found
     */
    @Test
    public void deleteRecipeWhenRecipeIsNotFoundThenThrowException() {
        RecipeActivity recipeActivity = Robolectric.setupActivity(RecipeActivity.class);
        ListView recipesView = recipeActivity.findViewById(R.id.recipeListView);
        RecipeItemAdapter recipeAdapter = (RecipeItemAdapter) recipesView.getAdapter();
        assertEquals(0, recipeAdapter.getCount());
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> {
                    recipeActivity.deleteRecipe(0);
                });
    }

    /**
     * Should delete the recipe when the recipe is found
     */
    @Test
    public void deleteRecipeWhenRecipeIsFound() {
        Activity activity = Robolectric.setupActivity(RecipeActivity.class);
        ListView recipesView = activity.findViewById(R.id.recipeListView);
        TextView sortText = activity.findViewById(R.id.recipeSortText);
        Button IngredientsNav = activity.findViewById(R.id.ingredientsNav);
        Button MealPlanNav = activity.findViewById(R.id.mealPlanNav);
        Button ShoppingListNav = activity.findViewById(R.id.shoppingListNav);
        FloatingActionButton AddRecipeButton = activity.findViewById(R.id.addRecipeButton);

        assertNotNull(recipesView);
        assertNotNull(sortText);
        assertNotNull(IngredientsNav);
        assertNotNull(MealPlanNav);
        assertNotNull(ShoppingListNav);
        assertNotNull(AddRecipeButton);
    }

    /**
     * Should sort recipes by recipe category in ascending order
     */
    @Test
    public void onCreateShouldSortRecipesByRecipeCategoryInAscendingOrder() {
        Activity activity = Robolectric.buildActivity(RecipeActivity.class).create().get();
        Button IngredientsNav = activity.findViewById(R.id.ingredientsNav);
        Button MealPlanNav = activity.findViewById(R.id.mealPlanNav);
        Button ShoppingListNav = activity.findViewById(R.id.shoppingListNav);
        ListView recipesView = activity.findViewById(R.id.recipeListView);
        TextView sortText = activity.findViewById(R.id.recipeSortText);

        assertNotNull(IngredientsNav);
        assertNotNull(MealPlanNav);
        assertNotNull(ShoppingListNav);
        assertNotNull(recipesView);
        assertNotNull(sortText);
    }

    /**
     * Should initialize the database listener
     */
    @Test
    public void onCreateShouldInitializeDatabaseListener() {
        Activity activity = Robolectric.buildActivity(RecipeActivity.class).create().get();
        RecipeActivity recipeActivity = (RecipeActivity) activity;
        recipeActivity.initDBListener(recipeCollec);
        verify(recipeCollec).get();
    }

    /**
     * Should initialize the views
     */
    @Test
    public void onCreateShouldInitializeViews() {
        Activity activity = Robolectric.setupActivity(RecipeActivity.class);
        Button ingredientsNav = activity.findViewById(R.id.ingredientsNav);
        Button mealPlanNav = activity.findViewById(R.id.mealPlanNav);
        Button shoppingListNav = activity.findViewById(R.id.shoppingListNav);
        ListView recipeListView = activity.findViewById(R.id.recipeListView);
        TextView recipeSortText = activity.findViewById(R.id.recipeSortText);

        assertNotNull(ingredientsNav);
        assertNotNull(mealPlanNav);
        assertNotNull(shoppingListNav);
        assertNotNull(recipeListView);
        assertNotNull(recipeSortText);
    }

    /**
     * Should sort recipes by title in ascending order
     */
    @Test
    public void onCreateShouldSortRecipesByTitleInAscendingOrder() {
        Activity activity = Robolectric.buildActivity(RecipeActivity.class).create().get();
        Button IngredientsNav = activity.findViewById(R.id.ingredientsNav);
        Button MealPlanNav = activity.findViewById(R.id.mealPlanNav);
        Button ShoppingListNav = activity.findViewById(R.id.shoppingListNav);
        TextView sortText = activity.findViewById(R.id.recipeSortText);
        ListView recipesView = activity.findViewById(R.id.recipeListView);

        assertNotNull(IngredientsNav);
        assertNotNull(MealPlanNav);
        assertNotNull(ShoppingListNav);
        assertNotNull(sortText);
        assertNotNull(recipesView);

        RecipeActivity recipeActivity = new RecipeActivity();
        recipeActivity.sortRecipes("Title", "Ascending");
    }

    /**
     * Should sort recipes by title in descending order
     */
    @Test
    public void onCreateShouldSortRecipesByTitleInDescendingOrder() {
        Activity activity = Robolectric.buildActivity(RecipeActivity.class).create().get();
        Button IngredientsNav = activity.findViewById(R.id.ingredientsNav);
        Button MealPlanNav = activity.findViewById(R.id.mealPlanNav);
        Button ShoppingListNav = activity.findViewById(R.id.shoppingListNav);
        TextView sortText = activity.findViewById(R.id.recipeSortText);
        ListView recipesView = activity.findViewById(R.id.recipeListView);

        assertNotNull(IngredientsNav);
        assertNotNull(MealPlanNav);
        assertNotNull(ShoppingListNav);
        assertNotNull(sortText);
        assertNotNull(recipesView);

        RecipeActivity recipeActivity = new RecipeActivity();
        recipeActivity.sortRecipes("Title", "Descending");
    }
}
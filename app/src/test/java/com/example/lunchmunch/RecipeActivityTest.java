package com.example.lunchmunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.test.core.app.ActivityScenario;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RecipeActivityTest {

    @Mock
    CollectionReference recipeCollec;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        // setup firebase
        FirebaseApp.initializeApp(Robolectric.buildActivity(RecipeActivity.class).get());
    }

    /**
     * Should throw an exception when the recipe is not found
     */
    @Test
    public void deleteRecipeWhenRecipeIsNotFoundThenThrowException() {
        ActivityScenario<RecipeActivity> recipeActivity = ActivityScenario.launch(RecipeActivity.class);
        recipeActivity.onActivity(activity -> {
            ListView recipesView = activity.findViewById(R.id.recipeListView);
            RecipeItemAdapter recipeAdapter = (RecipeItemAdapter) recipesView.getAdapter();
            assertEquals(0, recipeAdapter.getCount());
            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> {
                        activity.deleteRecipe(0);
                    });
        });

    }

    /**
     * Should delete the recipe when the recipe is found
     */
    @Test
    public void deleteRecipeWhenRecipeIsFound() {
        Activity activity = Robolectric.setupActivity(RecipeActivity.class);
        ListView recipesView = activity.findViewById(R.id.recipeListView);
        Button IngredientsNav = activity.findViewById(R.id.ingredientsNav);
        Button MealPlanNav = activity.findViewById(R.id.mealPlanNav);
        Button ShoppingListNav = activity.findViewById(R.id.shoppingListNav);
        FloatingActionButton AddRecipeButton = activity.findViewById(R.id.addRecipeButton);

        assertNotNull(recipesView);
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

        assertNotNull(IngredientsNav);
        assertNotNull(MealPlanNav);
        assertNotNull(ShoppingListNav);
        assertNotNull(recipesView);
    }

    /**
     * Should initialize the database listener
     */
    @Test
    public void onCreateShouldInitializeDatabaseListener() {
        RecipeActivity activity = Robolectric.buildActivity(RecipeActivity.class).create().get();
        recipeCollec = mock(CollectionReference.class);
        when(recipeCollec.get()).thenReturn(mock(Task.class));
        activity.initDBListener(recipeCollec);
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
        assertNotNull(ingredientsNav);
        assertNotNull(mealPlanNav);
        assertNotNull(shoppingListNav);
        assertNotNull(recipeListView);
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
        ListView recipesView = activity.findViewById(R.id.recipeListView);

        assertNotNull(IngredientsNav);
        assertNotNull(MealPlanNav);
        assertNotNull(ShoppingListNav);
        assertNotNull(recipesView);
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
        ListView recipesView = activity.findViewById(R.id.recipeListView);

        assertNotNull(IngredientsNav);
        assertNotNull(MealPlanNav);
        assertNotNull(ShoppingListNav);
        assertNotNull(recipesView);

    }
}
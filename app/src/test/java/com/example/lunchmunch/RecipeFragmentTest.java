package com.example.lunchmunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Test;

public class RecipeFragmentTest {

    /**
     * Should set the mealtype to the selected item
     */
    @Test
    public void onItemSelectedShouldSetMealTypeToSelectedItem() {
        RecipeFragment recipeFragment = new RecipeFragment();
        recipeFragment.onCreate(new Bundle());
        recipeFragment.onCreateView(LayoutInflater.from(null), null, null);
        recipeFragment.onViewCreated(null, null);
        recipeFragment.onItemSelected(null, null, 1, 0);
        assertEquals("Breakfast", recipeFragment.mealType);
    }

    /**
     * Should update the recipe when the fragment is not null
     */
    @Test
    public void onDismissWhenFragmentIsNotNullThenUpdateRecipe() {
        RecipeFragment recipeFragment = new RecipeFragment();
        recipeFragment.onDismiss(null);
    }

    /**
     * Should throw an exception when the context is not an instance of
     * onfragmentinteractionlistener
     */
    @Test
    public void
    onAttachWhenContextIsNotAnInstanceOfOnFragmentInteractionListenerThenThrowException() {
        RecipeFragment recipeFragment = new RecipeFragment();
        try {
            recipeFragment.onAttach(new View(null));
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("android.view.View@null must implement listener", e.getMessage());
        }
    }

    /**
     * Should return the index of the meal type in the list
     */
    @Test
    public void getMealTypeIndexShouldReturnTheIndexOfTheMealTypeInTheList() {
        RecipeFragment recipeFragment = new RecipeFragment();
        Bundle bundle = new Bundle();
        recipeFragment.setArguments(bundle);
        int index = recipeFragment.getMealTypeIndex("Breakfast");
        assertEquals(0, index);
    }

    /**
     * Should set the recipe name when the recipe is not null
     */
    @Test
    public void onCreateDialogWhenRecipeIsNotNullThenSetRecipeName() {
        Recipe recipe = new Recipe("test", null, "test", "test", "test", 1, 1, "test");
        RecipeFragment recipeFragment = new RecipeFragment(recipe);
        Bundle bundle = new Bundle();
        recipeFragment.setArguments(bundle);
        recipeFragment.onCreateDialog(bundle);
        assertEquals("test", recipeFragment.recipeName.getText().toString());
    }

    /**
     * Should set the recipe instructions when the recipe is not null
     */
    @Test
    public void onCreateDialogWhenRecipeIsNotNullThenSetRecipeInstructions() {
        Recipe recipe = new Recipe("test", null, "test", "test", "test", 1, 1, "test");
        RecipeFragment recipeFragment = new RecipeFragment(recipe);
        Bundle bundle = new Bundle();
        recipeFragment.setArguments(bundle);
        recipeFragment.onCreateDialog(bundle);
        assertEquals("test", recipeFragment.recipeInstructions.getText().toString());
    }

    /**
     * Should set the meal type when the recipe is not null
     */
    @Test
    public void onCreateDialogWhenRecipeIsNotNullThenSetMealType() {
        Recipe recipe =
                new Recipe("name", null, "instructions", "mealType", "image", 1, 1, "comments");
        RecipeFragment recipeFragment = new RecipeFragment(recipe);
        Bundle bundle = new Bundle();
        recipeFragment.setArguments(bundle);
        recipeFragment.onCreateDialog(bundle);
        assertEquals("mealType", recipeFragment.mealType);
    }

    /**
     * Should set the servings when the recipe is not null
     */
    @Test
    public void onCreateDialogWhenRecipeIsNotNullThenSetServings() {
        Recipe recipe =
                new Recipe("name", null, "instructions", "mealType", "image", 1, 1, "comments");
        RecipeFragment recipeFragment = new RecipeFragment(recipe);
        Bundle bundle = new Bundle();
        recipeFragment.setArguments(bundle);
        recipeFragment.onCreateDialog(bundle);
        assertEquals(1, recipeFragment.servings.getText().toString());
    }

    /**
     * Should set the prep time when the recipe is not null
     */
    @Test
    public void onCreateDialogWhenRecipeIsNotNullThenSetPrepTime() {
        Recipe recipe =
                new Recipe("name", null, "instructions", "mealType", "image", 1, 2, "comments");
        RecipeFragment recipeFragment = new RecipeFragment(recipe);
        Bundle bundle = new Bundle();
        recipeFragment.setArguments(bundle);
        recipeFragment.onCreateDialog(bundle);
        assertEquals(2, recipeFragment.prepTime.getText().toString());
    }

    /**
     * Should inflate the layout
     */
    @Test
    public void onCreateViewShouldInflateTheLayout() {
        LayoutInflater inflater = mock(LayoutInflater.class);
        ViewGroup container = mock(ViewGroup.class);
        Bundle savedInstanceState = mock(Bundle.class);
        RecipeFragment recipeFragment = new RecipeFragment();
        recipeFragment.onCreateView(inflater, container, savedInstanceState);
        verify(inflater).inflate(R.layout.recipe_item_dialog, container, false);
    }

    /**
     * Should return the view
     */
    @Test
    public void onCreateViewShouldReturnTheView() {
        RecipeFragment recipeFragment = new RecipeFragment();
        LayoutInflater inflater = mock(LayoutInflater.class);
        ViewGroup container = mock(ViewGroup.class);
        Bundle savedInstanceState = mock(Bundle.class);

        View view = recipeFragment.onCreateView(inflater, container, savedInstanceState);

        assertNotNull(view);
    }
}
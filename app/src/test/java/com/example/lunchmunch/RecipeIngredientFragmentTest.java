package com.example.lunchmunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.os.Bundle;
import android.view.View;

import org.junit.Test;

public class RecipeIngredientFragmentTest {

    /**
     * Should set the expiration date to null when the user does not select a date
     */
    @Test
    public void initDatePickerWhenUserDoesNotSelectDateThenSetExpirationDateToNull() {
        RecipeIngredientFragment recipeIngredientFragment =
                new RecipeIngredientFragment(new Recipe(), new FoodItemAdapter(null, 0, null));
        recipeIngredientFragment.initDatePicker();
        assertNull(recipeIngredientFragment.expirationDate);
    }

    /**
     * Should set the expiration date when the user selects a date
     */
    @Test
    public void initDatePickerWhenUserSelectsDateThenSetExpirationDate() {
        RecipeIngredientFragment recipeIngredientFragment =
                new RecipeIngredientFragment(new Recipe(), new FoodItemAdapter(null, 0, null));
        Bundle bundle = new Bundle();
        recipeIngredientFragment.setArguments(bundle);
        recipeIngredientFragment.initDatePicker();
    }

    /**
     * Should add the ingredient to the recipe when the currentingredientposition is -1
     */
    @Test
    public void
    onCreateDialogWhenCurrentIngredientPositionIsMinusOneThenAddTheIngredientToTheRecipe() {
        Recipe recipe = mock(Recipe.class);
        FoodItemAdapter foodItemAdapter = mock(FoodItemAdapter.class);
        RecipeIngredientFragment recipeIngredientFragment =
                new RecipeIngredientFragment(recipe, foodItemAdapter);
        Bundle bundle = new Bundle();
        bundle.putInt("currentIngredientPosition", -1);
        recipeIngredientFragment.setArguments(bundle);
        recipeIngredientFragment.onCreateDialog(null);
        verify(recipe, times(1)).getIngredients();
    }

    /**
     * Should set the current ingredient when the currentingredientposition is not -1
     */
    @Test
    public void onCreateDialogWhenCurrentIngredientPositionIsNotMinusOneThenSetCurrentIngredient() {
        Recipe recipe = mock(Recipe.class);
        FoodItemAdapter foodItemAdapter = mock(FoodItemAdapter.class);
        RecipeIngredientFragment recipeIngredientFragment =
                new RecipeIngredientFragment(recipe, foodItemAdapter);
        Bundle bundle = mock(Bundle.class);
        when(bundle.getInt("currentIngredientPosition", -1)).thenReturn(0);
        recipeIngredientFragment.setArguments(bundle);
        recipeIngredientFragment.onCreateDialog(null);
        verify(recipe, times(1)).getIngredients();
    }

    @Test
    public void getUserInputWhenUserEntersInputThenReturnIngredient() {
        Recipe recipe = mock(Recipe.class);
        FoodItemAdapter foodItemAdapter = mock(FoodItemAdapter.class);
        RecipeIngredientFragment recipeIngredientFragment =
                new RecipeIngredientFragment(recipe, foodItemAdapter);
        Bundle bundle = mock(Bundle.class);
        when(bundle.getInt("currentIngredientPosition", -1)).thenReturn(0);
        recipeIngredientFragment.setArguments(bundle);
        recipeIngredientFragment.onCreateDialog(null);
        recipeIngredientFragment.getUserInput();
    }

    @Test
    public void getListeneronAttachWhenContextIsNotAnInstanceOfOnFragmentInteractionListenerThenThrowException() {
        RecipeIngredientFragment recipeIngredientFragment = new RecipeIngredientFragment(
                new Recipe(), new FoodItemAdapter(null, 0, null));
        try {
            recipeIngredientFragment.onAttach(new View(null));
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("android.view.View@null must implement listener", e.getMessage());
        }
    }
}
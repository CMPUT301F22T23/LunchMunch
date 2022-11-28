package com.example.lunchmunch;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import android.app.Dialog;
import android.os.Bundle;

import org.junit.Test;


public class MealPlanIngredientFragmentTest {

    /**
     * Should return the Alert Dialog
     */
    @Test
    public void onCreateDialogShouldReturnTheDialog() {
        MealPlanIngredientFragment mealPlanIngredientFragment = mock(MealPlanIngredientFragment.class);
        Bundle savedInstanceState = mock(Bundle.class);

        Dialog dialog = mealPlanIngredientFragment.onCreateDialog(savedInstanceState);
        assertNull(dialog);

    }

    /**
     * On create dialog should init food item adapter
     */
    @Test
    public void onCreateDialogShouldInitAdapter() {
        MealPlanIngredientFragment mealPlanIngredientFragment = mock(MealPlanIngredientFragment.class);
        Bundle savedInstanceState = mock(Bundle.class);

        Dialog dialog = mealPlanIngredientFragment.onCreateDialog(savedInstanceState);

        assertNull(mealPlanIngredientFragment.adapter);

    }

    /**
     * Test set day
     */
    @Test
    public void shouldSetDay() {
        MealPlanDateFragment mealPlanDateFragment = mock(MealPlanDateFragment.class);
        mealPlanDateFragment.setDay("day");

    }
}

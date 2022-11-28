package com.example.lunchmunch;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import android.app.Dialog;
import android.os.Bundle;

import org.junit.Test;


public class MealPlanRecipeFragmentTest {

    /**
     * Should return the Alert Dialog
     */
    @Test
    public void onCreateDialogShouldReturnTheDialog() {
        MealPlanRecipeFragment mealPlanRecipeFragment = mock(MealPlanRecipeFragment.class);
        Bundle savedInstanceState = mock(Bundle.class);

        Dialog dialog = mealPlanRecipeFragment.onCreateDialog(savedInstanceState);
        assertNull(dialog);

    }

    /**
     * On create dialog should init food item adapter
     */
    @Test
    public void onCreateDialogShouldInitAdapter() {
        MealPlanRecipeFragment mealPlanRecipeFragment = mock(MealPlanRecipeFragment.class);
        Bundle savedInstanceState = mock(Bundle.class);

        Dialog dialog = mealPlanRecipeFragment.onCreateDialog(savedInstanceState);

        assertNull(mealPlanRecipeFragment.adapter);

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

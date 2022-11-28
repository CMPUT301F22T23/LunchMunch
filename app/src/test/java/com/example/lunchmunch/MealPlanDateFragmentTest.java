package com.example.lunchmunch;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import org.junit.Test;

public class MealPlanDateFragmentTest {



    /**
     * Should return the Alert Dialog
     */
    @Test
    public void onCreateDialogShouldReturnTheDialog() {
        MealPlanDateFragment mealPlanDateFragment = mock(MealPlanDateFragment.class);
        Bundle savedInstanceState = mock(Bundle.class);

        Dialog dialog = mealPlanDateFragment.onCreateDialog(savedInstanceState);
        assertNull(dialog);

    }

    /**
     * On create dialog should init recipe fragment
     */
    @Test
    public void onCreateDialogShouldInitRecipeFragment() {
        MealPlanDateFragment mealPlanDateFragment = mock(MealPlanDateFragment.class);
        Bundle savedInstanceState = mock(Bundle.class);

        Dialog dialog = mealPlanDateFragment.onCreateDialog(savedInstanceState);

        assertNull(mealPlanDateFragment.recipeFragment);

    }

    /**
     * On create dialog should init ingredient fragment
     */
    @Test
    public void onCreateDialogShouldInitIngredientFragment() {
        MealPlanDateFragment mealPlanDateFragment = mock(MealPlanDateFragment.class);
        Bundle savedInstanceState = mock(Bundle.class);

        Dialog dialog = mealPlanDateFragment.onCreateDialog(savedInstanceState);

        assertNull(mealPlanDateFragment.ingredientFragment);

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


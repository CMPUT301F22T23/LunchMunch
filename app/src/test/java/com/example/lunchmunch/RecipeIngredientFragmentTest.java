package com.example.lunchmunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
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
}





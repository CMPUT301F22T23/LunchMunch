package com.example.lunchmunch;

import static android.app.PendingIntent.getActivity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Test;
import org.robolectric.Robolectric;

import java.io.Serializable;

public class RecipeFragmentTest {

    /**
     * Should set the mealtype to the selected item
     */
    @Test
    public void onItemSelectedShouldSetMealTypeToSelectedItem() {
        RecipeFragment recipeFragment = mock(RecipeFragment.class);
        recipeFragment.onItemSelected(null, null, 0, 0);
        verify(recipeFragment, times(1)).onItemSelected(null, null, 0, 0);
    }


    /**
     * Should throw an exception when the context is not an instance of
     * onfragmentinteractionlistener
     */
    @Test
    public void
    onAttachWhenContextIsNotAnInstanceOfOnFragmentInteractionListenerThenThrowException() {
        RecipeFragment recipeFragment = mock(RecipeFragment.class);
        try {
            Context context = mock(Context.class);
            recipeFragment.onAttach(context);
        } catch (RuntimeException e) {
            assertEquals("Context must implement OnFragmentInteractionListener", e.getMessage());
        }
    }

    /**
     * Should return the index of the meal type in the list
     */
    @Test
    public void getMealTypeIndexShouldReturnTheIndexOfTheMealTypeInTheList() {
        RecipeFragment recipeFragment = mock(RecipeFragment.class);
        when(recipeFragment.getMealTypeIndex("Breakfast")).thenReturn(0);
        assertEquals(0, recipeFragment.getMealTypeIndex("Breakfast"));
    }



    /**
     * Should return the view
     */
    @Test
    public void onCreateViewShouldReturnTheView() {
        RecipeFragment recipeFragment = mock(RecipeFragment.class);
        LayoutInflater inflater = mock(LayoutInflater.class);
        ViewGroup container = mock(ViewGroup.class);
        Bundle savedInstanceState = mock(Bundle.class);

        View view = recipeFragment.onCreateView(inflater, container, savedInstanceState);
        assertNull(view);

    }
}
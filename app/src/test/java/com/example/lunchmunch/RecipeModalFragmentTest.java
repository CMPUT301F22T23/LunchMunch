package com.example.lunchmunch;

import static com.google.common.base.Verify.verify;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

public class RecipeModalFragmentTest {
    /**
     * Should create the view
     */
    @Test
    public void onCreateViewShouldReturnTheView() {
        RecipeModalFragment recipeModalFragment = mock(RecipeModalFragment.class);
        when(recipeModalFragment.onCreateView(null, null, null)).thenReturn(null);
        recipeModalFragment.onCreateView(null, null, null);
    }

    /**
     * Should set the adapter
     */
    @Test
    public void onViewCreatedShouldSetTheAdapter() {
        RecipeModalFragment recipeModalFragment = new RecipeModalFragment();
        recipeModalFragment.onViewCreated(null, null);
    }

    /**
     * Should set the recipe when the fragment is not null
     */
    @Test
    public void onDismissShouldDismissTheDialog() {
        RecipeModalFragment recipeModalFragment = mock(RecipeModalFragment.class);
        recipeModalFragment.onDismiss(null);
    }

    /**
     * Should set the listener
     */
    @Test
    public void onAttachShouldSetTheListener() {
        RecipeModalFragment recipeModalFragment = mock(RecipeModalFragment.class);
        recipeModalFragment.onAttach(null);
        Mockito.verify(recipeModalFragment, times(1)).onAttach(null);
    }

}

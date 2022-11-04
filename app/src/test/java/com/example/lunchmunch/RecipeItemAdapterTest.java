package com.example.lunchmunch;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.view.View;

import org.junit.Test;

public class RecipeItemAdapterTest {
    @Test
    public void getViewShouldReturnTheView() {
        RecipeItemAdapter recipeItemAdapter = mock(RecipeItemAdapter.class);
        View convertView = mock(View.class);
        RecipeItemAdapter.ViewHolder viewHolder = mock(RecipeItemAdapter.ViewHolder.class);
        when(recipeItemAdapter.getView(0, convertView, null)).thenReturn(convertView);
        View view = recipeItemAdapter.getView(0, convertView, null);
        assertNotNull(view);
    }

}

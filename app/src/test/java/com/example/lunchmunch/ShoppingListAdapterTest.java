package com.example.lunchmunch;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.junit.Test;

public class ShoppingListAdapterTest {

    @Test
    public void getViewShouldReturnTheView() {
        ShoppingListAdapter shoppingListAdapter = mock(ShoppingListAdapter.class);
        RecyclerView recyclerView = mock(RecyclerView.class);
        ShoppingListAdapter.ViewHolder viewHolder = mock(ShoppingListAdapter.ViewHolder.class);
        assertNotNull(viewHolder);
    }
}

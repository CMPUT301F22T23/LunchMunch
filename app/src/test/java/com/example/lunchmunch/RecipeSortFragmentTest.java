package com.example.lunchmunch;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.junit.Test;

public class RecipeSortFragmentTest {
    @Test
    public void onCreateViewShouldReturnTheView() {
        RecipeSortFragment recipeSortFragment = mock(RecipeSortFragment.class);
        LayoutInflater inflater = mock(LayoutInflater.class);
        ViewGroup container = mock(ViewGroup.class);
        Bundle savedInstanceState = mock(Bundle.class);
        recipeSortFragment.onCreateView(inflater, container, savedInstanceState);
    }

    @Test
    public void onViewCreatedShouldSetTheAdapter() {
        RecipeSortFragment recipeSortFragment = new RecipeSortFragment();
        recipeSortFragment.onViewCreated(null, null);
    }
}

package com.example.lunchmunch;

import static org.junit.Assert.assertNotNull;

import android.app.Activity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;

public class ShoppingListActivityTest {

    @Mock
    CollectionReference IngrCollec;

    @Mock
    CollectionReference MealPlanCollec;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        // setup firebase
        FirebaseApp.initializeApp(Robolectric.buildActivity(ShoppingListActivity.class).get());
    }

    /**
     * Should sort recipes by recipe category in ascending order
     */
    @Test
    public void testInitViews() {
        Activity activity = Robolectric.buildActivity(ShoppingListActivity.class).create().get();
        Button IngredientsNav = activity.findViewById(R.id.ingredientsNav);
        Button RecipesNav = activity.findViewById(R.id.recipesNav);
        Button MealPlanNav = activity.findViewById(R.id.mealPlanNav);
        RecyclerView shoplistRecView = activity.findViewById(R.id.shoplistRecView);
        Spinner sortSpinner = activity.findViewById(R.id.SortOptions);

        assertNotNull(IngredientsNav);
        assertNotNull(RecipesNav);
        assertNotNull(MealPlanNav);
        assertNotNull(shoplistRecView);
        assertNotNull(sortSpinner);
    }






}

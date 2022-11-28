package com.example.lunchmunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class MealPlanActivityTest {

    @Mock
    CollectionReference mealPlanCollec;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        // setup firebase
        FirebaseApp.initializeApp(Robolectric.buildActivity(MealPlanActivity.class).get());
    }

    /**
     * Should initialize the database listener
     */
    @Test
    public void onCreateShouldInitializeDatabaseListener() {
        MealPlanActivity activity = Robolectric.buildActivity(MealPlanActivity.class).create().get();
        mealPlanCollec = mock(CollectionReference.class);
        when(mealPlanCollec.get()).thenReturn(mock(Task.class));
        activity.initDBListener(mealPlanCollec);
        verify(mealPlanCollec).get();
    }

    /**
     * Should initialize the recycler views for each date of Meal Planning
     */
    @Test
    public void onCreateShouldInitializeRecyclerViews() {
        Activity activity = Robolectric.setupActivity(MealPlanActivity.class);
        RecyclerView mondayRecyclerView = activity.findViewById(R.id.monday_meal_plan_items_list);
        RecyclerView tuesdayRecyclerView = activity.findViewById(R.id.tuesday_meal_plan_items_list);
        RecyclerView wednesdayRecyclerView = activity.findViewById(R.id.wednesday_meal_plan_items_list);
        RecyclerView thursdayRecyclerView = activity.findViewById(R.id.thursday_meal_plan_items_list);
        RecyclerView fridayRecyclerView = activity.findViewById(R.id.friday_meal_plan_items_list);
        RecyclerView saturdayRecyclerView = activity.findViewById(R.id.saturday_meal_plan_items_list);
        RecyclerView sundayRecyclerView = activity.findViewById(R.id.sunday_meal_plan_items_list);

        assertNotNull(mondayRecyclerView);
        assertNotNull(tuesdayRecyclerView);
        assertNotNull(wednesdayRecyclerView);
        assertNotNull(thursdayRecyclerView);
        assertNotNull(fridayRecyclerView);
        assertNotNull(saturdayRecyclerView);
        assertNotNull(sundayRecyclerView);

    }

    /**
     * Should initialize the image views for editing each date of Meal Planning
     */
    @Test
    public void onCreateShouldInitializeEditImageViews() {
        Activity activity = Robolectric.setupActivity(MealPlanActivity.class);
        ImageView mondayRecyclerView = activity.findViewById(R.id.monday_meal_plan_edit_pencil);
        ImageView tuesdayRecyclerView = activity.findViewById(R.id.tuesday_meal_plan_edit_pencil);
        ImageView wednesdayRecyclerView = activity.findViewById(R.id.wednesday_meal_plan_edit_pencil);
        ImageView thursdayRecyclerView = activity.findViewById(R.id.thursday_meal_plan_edit_pencil);
        ImageView fridayRecyclerView = activity.findViewById(R.id.friday_meal_plan_edit_pencil);
        ImageView saturdayRecyclerView = activity.findViewById(R.id.saturday_meal_plan_edit_pencil);
        ImageView sundayRecyclerView = activity.findViewById(R.id.sunday_meal_plan_edit_pencil);

        assertNotNull(mondayRecyclerView);
        assertNotNull(tuesdayRecyclerView);
        assertNotNull(wednesdayRecyclerView);
        assertNotNull(thursdayRecyclerView);
        assertNotNull(fridayRecyclerView);
        assertNotNull(saturdayRecyclerView);
        assertNotNull(sundayRecyclerView);
    }

}
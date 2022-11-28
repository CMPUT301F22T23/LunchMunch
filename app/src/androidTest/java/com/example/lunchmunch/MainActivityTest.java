package com.example.lunchmunch;
import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


/**
 * Test class for MainActivity. All the UI tests are written here. Robotium test framework is
 used
 */
public class MainActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);


    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void testRedirToIngrPage() throws Exception {
        // if any of the asserts below fail this method will throw and exception with the message of the failed line

        // if not in mainactivity output wront activity message
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        // click on the ingredients navigation button
        solo.clickOnButton("Ingredients");
        // checl if we are actually on the ingredients page
        solo.assertCurrentActivity("Wrong Activity (should be Ingredients page)", IngredientsActivity.class);

    }

    @Test
    public void testRedirToRecipesPage() throws Exception {
        // if any of the asserts below fail this method will throw and exception with the message of the failed line

        // if not in ingredients output wront activity message
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        // click on the ingredients navigation button
        solo.clickOnButton("Recipes");
        // checl if we are actually on the ingredients page
        solo.assertCurrentActivity("Wrong Activity (should be Recipes page)", RecipeActivity.class);

    }

    @Test
    public void testRedirToMealPlanPage() throws Exception {
        // if any of the asserts below fail this method will throw and exception with the message of the failed line

        // if not in ingredients output wront activity message
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        // click on the ingredients navigation button
        solo.clickOnButton("Meal Plan");
        // checl if we are actually on the ingredients page
        solo.assertCurrentActivity("Wrong Activity (should be Meal Plan page)", MealPlanActivity.class);

    }

    @Test
    public void testRedirToShoppingListPage() throws Exception {
        // if any of the asserts below fail this method will throw and exception with the message of the failed line

        // if not in ingredients output wront activity message
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        // click on the ingredients navigation button
        solo.clickOnButton("Shopping List");
        // checl if we are actually on the ingredients page
        solo.assertCurrentActivity("Wrong Activity (should be Shopping List page)", ShoppingListActivity.class);

    }

    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}




package com.example.lunchmunch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IngredientTest {
    ArrayList<Ingredient> ingredientList;
    HashMap<String, Ingredient> foodMap;
    /**
     * In order to start each test with a clean map and array, we use this teardown method to clear
     * any previous data after each unit test
     */
    @AfterEach
    private void tearDown() {
        foodMap.clear();
        ingredientList.clear();
    }

    private ArrayList<Ingredient> mockIngredientList() {
        // create an ingredient list
        ingredientList = new ArrayList<>();
        ingredientList.add(mockIngredient("Brioche", "Sweetened bread, made from encriched dough"));
        foodMap.put();
        return ingredientList;
    }

    private HashMap<String, Ingredient> mockIngredientMap() {
        // create a hash map for storing unique ingredients
        foodMap = new HashMap<>();
        foodMap.put();
    }

    private Ingredient mockIngredient(String name, String description) {
        // create a date instance
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = dateFormat.parse("2022-12-31");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //create Location instance
        Location location = Location.PANTRY;

        //create IngredientCategory instance
        IngredientCategory category = IngredientCategory.GRAIN;

        return new Ingredient(
                name,
                description,
                date,
                location,
                4,
                1,
                category
                );
    }

    @Test
    void testAddIngredient() {
        ArrayList<Ingredient> ingredientList = mockIngredientList();
        assertEquals(1, ingredientList.size());
        // create additional mock ingredient to add to the array
        Ingredient newIngredient = mockIngredient("Croissant", "Flaky, layered french bread");
        // add ingredient to both array and map
        ingredientList.add(newIngredient);
        foodMap.put(newIngredient.getName(), newIngredient);
        assertEquals(2, ingredientList.size());
        assertTrue(ingredientList.contains(newIngredient));
    }


}

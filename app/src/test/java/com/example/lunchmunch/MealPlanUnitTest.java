package com.example.lunchmunch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class MealPlanUnitTest {

    // Test that Meal Plan constructor works on a Recipe Item
    @Test
    public void testMealPlanItemRecipeConstructor() {
        ArrayList<String> ingredients = new ArrayList<String>();
        Recipe recipe = new Recipe("name", ingredients, "instructions", "mealType", "image", 0, 0, "comments");
        MealPlanItem mealPlanItem = new MealPlanItem(recipe);

        assertEquals("name", mealPlanItem.getName());
        assertEquals(ingredients, mealPlanItem.getIngredientNames());
        assertEquals("instructions", mealPlanItem.getInstructions());
        assertEquals("mealType", mealPlanItem.getMealType());
        assertEquals("image", mealPlanItem.getImage());
        assertEquals(new Integer(0), mealPlanItem.getServings());
        assertEquals(new Integer(0), mealPlanItem.getPrepTime());
        assertEquals("comments", mealPlanItem.getComments());

    }

    // Test that Meal Plan constructor works on an Ingredient Item
    @Test
    public void testMealPlanItemIngredientConstructor() {
        Ingredient ingredient = new Ingredient("name","description", new Date(1000000000), Location.FREEZER,  new Float(1), new Float(1), IngredientCategory.MEAT);
        MealPlanItem mealPlanItem = new MealPlanItem(ingredient);

        assertEquals("name", mealPlanItem.getName());
        assertEquals("description", mealPlanItem.getDescription());
        assertEquals(new Date(1000000000), mealPlanItem.getBestBefore());
        assertEquals(Location.FREEZER, mealPlanItem.getLocation());
        assertEquals(new Float(1), mealPlanItem.getCount());
        assertEquals(new Float(1), mealPlanItem.getCost());
        assertEquals(IngredientCategory.MEAT, mealPlanItem.getCategory());

    }


    // Test that the setters and getters work for Ingredient Meal Plan Items
    @Test
    public void testMealPlanIngredientSetters() {
        Ingredient ingredient = new Ingredient("name","description", new Date(1000000000), Location.FREEZER,  new Float(1), new Float(1), IngredientCategory.MEAT);
        MealPlanItem mealPlanItem = new MealPlanItem(ingredient);

        mealPlanItem.setName("newName");
        mealPlanItem.setDescription("newDescription");
        mealPlanItem.setBestBefore(new Date(2000000000));
        mealPlanItem.setLocation(Location.PANTRY);
        mealPlanItem.setCount(new Float(2));
        mealPlanItem.setCost(new Float(3));

        assertEquals("newName", mealPlanItem.getName());
        assertEquals("newDescription", mealPlanItem.getDescription());
        assertEquals(new Date(2000000000), mealPlanItem.getBestBefore());
        assertEquals(Location.PANTRY, mealPlanItem.getLocation());
        assertEquals(new Float(2), mealPlanItem.getCount());
        assertEquals(new Float(3), mealPlanItem.getCost());
    }

    // Test that the setters and getters work for Recipe Meal Plan Items
    @Test
    public void testMealPlanRecipeSetters() {
        ArrayList<String> ingredients = new ArrayList<String>();
        Recipe recipe = new Recipe("name", ingredients, "instructions", "mealType", "image", 0, 0, "comments");
        MealPlanItem mealPlanItem = new MealPlanItem(recipe);

        mealPlanItem.setName("newName");
        mealPlanItem.setInstructions("newInstructions");
        mealPlanItem.setMealType("newMealType");
        ArrayList<Ingredient> newIngredients = new ArrayList<Ingredient>();
        mealPlanItem.setIngredients(newIngredients);
        mealPlanItem.setImage("newImage");
        mealPlanItem.setServings(10);
        mealPlanItem.setPrepTime(10);
        mealPlanItem.setComments("newComments");
        assertEquals("newName", mealPlanItem.getName());
        assertEquals(newIngredients, mealPlanItem.getIngredients());
        assertEquals("newInstructions", mealPlanItem.getInstructions());
        assertEquals("newMealType", mealPlanItem.getMealType());
        assertEquals("newImage", mealPlanItem.getImage());
        assertEquals(new Integer(10), mealPlanItem.getServings());
        assertEquals(new Integer(10), mealPlanItem.getPrepTime());
        assertEquals("newComments", mealPlanItem.getComments());
    }

}

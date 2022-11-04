package com.example.lunchmunch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;

// Write unit tests for the recipe class
public class RecipeUnitTest {
    // Test that the recipe constructor works
    @Test
    public void testRecipeConstructor() {
        ArrayList<String> ingredients = new ArrayList<String>();
        Recipe recipe = new Recipe("name", ingredients, "instructions", "mealType", "image", 0, 0, "comments");
        assertEquals("name", recipe.getName());
        assertEquals(ingredients, recipe.getIngredientNames());
        assertEquals("instructions", recipe.getInstructions());
        assertEquals("mealType", recipe.getMealType());
        assertEquals("image", recipe.getImage());
        assertEquals(new Integer(0), recipe.getServings());
        assertEquals(new Integer(0), recipe.getPrepTime());
        assertEquals("comments", recipe.getComments());

    }

    // Test that the recipe setters and getters work
    @Test
    public void testRecipeSetters() {
        ArrayList<String> ingredients = new ArrayList<String>();
        Recipe recipe = new Recipe("name", ingredients, "instructions", "mealType", "image", 0, 0, "comments");
        recipe.setName("newName");
        recipe.setInstructions("newInstructions");
        recipe.setMealType("newMealType");
        ArrayList<Ingredient> newIngredients = new ArrayList<Ingredient>();
        recipe.setIngredientsClass(newIngredients);
        recipe.setImage("newImage");
        recipe.setServings(10);
        recipe.setPrepTime(10);
        recipe.setComments("newComments");
        assertEquals("newName", recipe.getName());
        assertEquals(newIngredients, recipe.getIngredients());
        assertEquals("newInstructions", recipe.getInstructions());
        assertEquals("newMealType", recipe.getMealType());
        assertEquals("newImage", recipe.getImage());
        assertEquals(new Integer(10), recipe.getServings());
        assertEquals(new Integer(10), recipe.getPrepTime());
        assertEquals("newComments", recipe.getComments());
    }


}

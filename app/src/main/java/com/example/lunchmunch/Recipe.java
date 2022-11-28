package com.example.lunchmunch;

import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageView;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;
/**
 * This class defines a Recipe
 */
public class Recipe implements Serializable {
    private String name;
    private List<Ingredient> ingredients;
    private List<String> ingredientNames;
    private String instructions;
    private String mealType;
    private Bitmap imageBitMap;
    private String comments;
    private Integer servings;
    private Integer prepTime;
    private String id;
    private Button addImage;
    private ImageView previewImage;
    //create empty constructor for database purposes
    public Recipe(){}





    // so able to store recipe in database without list of full ingredients objects (just store their names instead)
    public Recipe(String name, List<String> ingredientNames, String instructions, String mealType, Bitmap imageBitMap, Integer servings, Integer prepTime, String comments) {
        this.name = name;
        this.ingredientNames = ingredientNames;
        this.instructions = instructions;
        this.mealType = mealType;
        this.imageBitMap = imageBitMap;
        this.servings = servings;
        this.prepTime = prepTime;
        this.comments = comments;
    }


    public Recipe(String name, List<Ingredient> ingredients, List<String> ingredientNames, String instructions, String mealType, Bitmap imageBitMap, Integer servings, Integer prepTime, String comments) {
        this.name = name;
        this.ingredients = ingredients;
        this.ingredientNames = ingredientNames;
        this.instructions = instructions;
        this.mealType = mealType;
        this.imageBitMap = imageBitMap;
        this.servings = servings;
        this.prepTime = prepTime;
        this.comments = comments;
    }

    public Recipe(String id, String name, List<Ingredient> ingredients, List<String> ingredientNames, String instructions, String mealType, Bitmap imageBitMap, Integer servings, Integer prepTime, String comments) {
        this.name = name;
        this.ingredients = ingredients;
        this.ingredientNames = ingredientNames;
        this.instructions = instructions;
        this.mealType = mealType;
        this.imageBitMap = imageBitMap;
        this.servings = servings;
        this.prepTime = prepTime;
        this.comments = comments;
        this.id = id;
    }

    protected Recipe(Parcel in) {
        name = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        ingredientNames = in.createStringArrayList();
        instructions = in.readString();
        mealType = in.readString();
        imageBitMap = null;
        comments = in.readString();
        if (in.readByte() == 0) {
            servings = null;
        } else {
            servings = in.readInt();
        }
        if (in.readByte() == 0) {
            prepTime = null;
        } else {
            prepTime = in.readInt();
        }
        id = in.readString();
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredientsClass(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    public List<String> getIngredientNames() {
        return ingredientNames;
    }

    public void setIngredientNames(List<String> ingredientNames) {
        this.ingredientNames = ingredientNames;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public Bitmap getImage() {
        return imageBitMap;
    }

    public void setImage(Bitmap imageBitMap) {
        this.imageBitMap = imageBitMap;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getId() {return id; }

    public void setId(String id) { this.id = id;}

    public void scaleRecipe(Integer servings) {
        if (this.servings == 1 && servings < 0) {
            return;
        }
        Float scale = (servings.floatValue() + this.servings.floatValue()) / this.servings.floatValue();

        for (Ingredient ingredient : ingredients) {
            Float newCost = ingredient.getCost() * scale;
            // round to 2 decimal places
            newCost = (float) Math.round(newCost * 100) / 100;
            ingredient.setCost(newCost);
            Float newCount = ingredient.getCount() * scale;
            // round to 2 decimal places
            newCount = (float) Math.round(newCount * 100) / 100;
            ingredient.setCount(newCount);
            System.out.println(servings);
            System.out.println(this.servings);
            System.out.println(ingredient.getCount());
            System.out.println(ingredient.getCost());
        }
        this.servings = servings + this.servings;
    }
}
package com.example.lunchmunch;

import java.util.Date;
import java.util.List;

enum MealPlanItemType {
    RECIPE("recipe"),
    INGREDIENT("ingredient");

    private String text;

    MealPlanItemType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    // TODO: String checker implement as needed
}

public class MealPlanItem<T> {

    // For either
    private String name = "";
    private MealPlanItemType type = MealPlanItemType.RECIPE;

    // For recipe items

    private List<Ingredient> ingredients;
    private List<String> ingredientNames;
    private String instructions = "";
    private String mealType = "";
    private String image = "";
    private String comments = "";
    private Integer servings = -1;
    private Integer prepTime = -1;
    private String id = "";

    // For ingredient items
    private String description = "";
    private Date bestBefore = new Date();
    private Location location = Location.FREEZER;
    private Float count = Float.valueOf(-1);
    private Float cost = Float.valueOf(-1);
    private IngredientCategory category = IngredientCategory.MEAT;

    public static MealPlanItem<Recipe> newInstance(Recipe value) {
        return new MealPlanItem<>(value);
    }

    public static MealPlanItem<Ingredient> newInstance(Ingredient value) {
        return new MealPlanItem<>(value);
    }



    // hide constructor so you have to use factory methods
    public MealPlanItem(T value) {
        if (value instanceof Recipe) {
            this.name = ((Recipe) value).getName();
            this.ingredients = ((Recipe) value).getIngredients();
            this.ingredientNames = ((Recipe) value).getIngredientNames();
            this.instructions = ((Recipe) value).getInstructions();
            this.mealType = ((Recipe) value).getMealType();
            this.image = ((Recipe) value).getImage();
            this.comments = ((Recipe) value).getComments();
            this.servings = ((Recipe) value).getServings();
            this.prepTime = ((Recipe) value).getPrepTime();
            this.id = ((Recipe) value).getId();
            this.type = MealPlanItemType.RECIPE;
        }
        if (value instanceof Ingredient) {
            this.name = ((Ingredient) value).getName();
            this.description = ((Ingredient) value).getDescription();
            this.bestBefore = ((Ingredient) value).getBestBefore();
            this.location = ((Ingredient) value).getLocation();
            this.count = ((Ingredient) value).getCount();
            this.cost = ((Ingredient) value).getCost();
            this.category = ((Ingredient) value).getCategory();
            this.type = MealPlanItemType.INGREDIENT;
        }
    }

    // Getters and Setters
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(Date bestBefore) {
        this.bestBefore = bestBefore;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Float getCount() {
        return count;
    }

    public void setCount(Float count) {
        this.count = count;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public IngredientCategory getCategory() {
        return category;
    }

    public void setCategory(IngredientCategory category) {
        this.category = category;
    }

    public MealPlanItemType getType() {
        return this.type;
    }

    public void setType(MealPlanItemType type) {
        this.type = type;
    }
}

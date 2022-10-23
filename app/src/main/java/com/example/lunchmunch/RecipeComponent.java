package com.example.lunchmunch;

public class RecipeComponent {
    private String name;
    private String description;
    // Leave ingredients as string for now
    private String ingredients;
    private String instructions;
    private String image;
    private Integer servings;

    public RecipeComponent(String name, String description, String ingredients, String instructions, String image, Integer servings) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.image = image;
        this.servings = servings;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getImage() {
        return image;
    }

    public Integer getServings() {
        return servings;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

}

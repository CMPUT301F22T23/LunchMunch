package com.example.lunchmunch;

/**
This enum defines Locations where Ingredients can be
 @see {@link com.example.lunchmunch.Ingredient}
 */
public enum Location {
    PANTRY("pantry"),
    FREEZER("freezer"),
    FRIDGE("fridge");
    private String value;

    Location(String location) {
        this.value = location.toLowerCase();
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

}

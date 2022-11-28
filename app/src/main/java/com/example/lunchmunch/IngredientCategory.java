package com.example.lunchmunch;

/**
 This enum defines Categories what Ingredients can be
 see {@link com.example.lunchmunch.Ingredient}
 **/
public enum IngredientCategory {
    VEGETABLE("vegetable"),
    FRUIT("fruit"),
    DAIRY("dairy"),
    MEAT("meat"),
    SEAFOOD("seafood"),
    GRAIN("grain"),
    OTHER("other");

    private String value;

    IngredientCategory(String category) {
        this.value = category.toLowerCase();
    }




    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}

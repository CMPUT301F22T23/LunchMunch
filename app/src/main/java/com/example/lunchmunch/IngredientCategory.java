package com.example.lunchmunch;

import java.util.ArrayList;

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

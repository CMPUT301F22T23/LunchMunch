package com.example.lunchmunch;

/**
 * ●	description (textual, up to 30 characters)
 * ●	best before date (presented in yyyy-mm-dd format)
 * ●	location (choice of pantry, freezer, or fridge)
 * ●	count (positive integer)
 * ●	unit cost (in dollars rounded up, positive integer)
 */
public enum Location {
    PANTRY("Pantry"),
    FREEZER("Freezer"),
    FRIDGE("Fridge");
    private String value;

    Location(String location) {
        this.value = location;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

}

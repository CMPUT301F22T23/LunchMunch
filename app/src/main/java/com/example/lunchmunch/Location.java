package com.example.lunchmunch;

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

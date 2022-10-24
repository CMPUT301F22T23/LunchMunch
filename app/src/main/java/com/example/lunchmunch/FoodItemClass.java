package com.example.lunchmunch;


import com.example.lunchmunch.Location;

import java.math.RoundingMode;
import java.util.Date;

public class FoodItemClass {
    private String name;
    private String description;
    private Date bestBefore;
    private Location location;
    private Integer count;
    private Integer cost;

    public FoodItemClass(String name, String description, Date bestBefore, Location location, Integer count, Integer cost) {
        this.name = name;
        this.description = description;
        this.bestBefore = bestBefore;
        this.location = location;
        this.count = count;
        this.cost = cost;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getBestBefore() {
        return this.bestBefore;
    }

    public void setBestBefore(Date date) {
        this.bestBefore = date;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Integer getCost() {
        return this.cost;
    }

    public void setCost(Float cost) {
        this.cost = Math.round(cost);

    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString(){
        return this.getName(); //Just an example ;)
    }

}

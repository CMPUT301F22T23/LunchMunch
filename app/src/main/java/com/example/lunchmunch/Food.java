package com.example.lunchmunch;

import java.util.Date;

public class Food {

    private String desc, location;
    private Date expDate;
    private Number cost, count;

    // this needs to be here for loading class in firestore db
    public Food() { }

    public Food(String desc, String location, Date expDate, Number cost, Number count) {
        this.desc = desc;
        this.location = location;
        this.expDate = expDate;
        this.cost = cost;
        this.count = count;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public Number getCost() {
        return cost;
    }

    public void setCost(Number cost) {
        this.cost = cost;
    }

    public Number getCount() {
        return count;
    }

    public void setCount(Number count) {
        this.count = count;
    }
}

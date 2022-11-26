package com.example.lunchmunch;


import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

/**
 * This class defines an Ingredient
 */
public class Ingredient implements Parcelable {
    private String name;
    private String description;
    private Date bestBefore;
    private Location location;
    private Integer count;
    private Integer cost;
    private IngredientCategory category;

    public Ingredient(String name, String description, Date bestBefore, Location location, Integer count, Integer cost, IngredientCategory category){
        this.name = name;
        this.description = description;
        this.bestBefore = bestBefore;
        this.location = location;
        this.count = count;
        this.cost = cost;
        this.category = category;
    }

    // For database
    public Ingredient(){
    }


    protected Ingredient(Parcel in) {
        name = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            count = null;
        } else {
            count = in.readInt();
        }
        if (in.readByte() == 0) {
            cost = null;
        } else {
            cost = in.readInt();
        }
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public static Integer getCategoryImage(IngredientCategory category) {
        switch (category) {
            case FRUIT: return R.drawable.fruit;
            case VEGETABLE: return R.drawable.vegetable;
            case DAIRY: return R.drawable.dairy;
            case MEAT: return R.drawable.meat;
            case GRAIN: return R.drawable.grain;
            case SEAFOOD: return R.drawable.seafood;
            default: return R.drawable.other;
        }
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

//    public void setCost(Float cost) {
//        this.cost = Math.round(cost);
//
//    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public IngredientCategory getCategory() { return this.category; }

    public void setCategory(IngredientCategory category) { this.category = category; }


    @Override
    public String toString(){
        return this.getName(); //Just an example ;)
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Make Ingredient parcel readable to pass through fragments
     * @param parcel
     *     parcel to read
     * @param i
     *     position
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        if (count == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(count);
        }
        if (cost == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(cost);
        }
        if(bestBefore != null){
            parcel.writeSerializable(bestBefore);
        } else {
            parcel.writeSerializable((byte)0);
        }
    }
}

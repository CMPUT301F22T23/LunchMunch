package com.example.lunchmunch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ShoppingListAdapter extends ArrayAdapter<Ingredient> {

    private ArrayList<Ingredient> shoppingList;
    Context context;

    public ShoppingListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Ingredient> objects) {
        super(context, resource, objects);
        this.shoppingList = objects;
        this.context = context;
    }

    private static class ViewHolder {
        TextView tvDescription;
        ImageView tvAmount;
        TextView tvUnit;
        TextView tvIngredientCategory;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get data item for this position
        Ingredient shopItem = getItem(position);
        //Check if an existing view is being used, if not inflate the view
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout)
        }
    }
}

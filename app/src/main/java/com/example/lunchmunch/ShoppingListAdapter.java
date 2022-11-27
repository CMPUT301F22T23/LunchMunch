package com.example.lunchmunch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

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
        TextView tvAmount;
        TextView tvUnit;
        TextView tvIngredientCategory;
        ImageView ivIngredientCategory;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get data item for this position
        Ingredient shopItem = getItem(position);
        //Check if an existing view is being used, if not inflate the view
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.shopping_list_content, parent, false);
            //assignment view to variables
            viewHolder.tvAmount = (TextView) convertView.findViewById(R.id.sl_item_cost);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.sl_item_description);
            viewHolder.tvUnit = (TextView) convertView.findViewById(R.id.sl_item_unit);
            viewHolder.tvIngredientCategory = (TextView) convertView.findViewById(R.id.sl_item_category);
            viewHolder.ivIngredientCategory = (ImageView) convertView.findViewById(R.id.sl_category_image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (shopItem.getName() != null) {
            viewHolder.tvDescription.setText(shopItem.getDescription());
//            Glide.with(getContext()).load(foodItem.getImage()).apply(RequestOptions.circleCropTransform()).into(viewHolder.tvImage);
            viewHolder.tvIngredientCategory.setText(shopItem.getCategory().toString());
            viewHolder.tvAmount.setText("$" + Float.toString(shopItem.getCost()));
            viewHolder.tvUnit.setText(Float.toString(shopItem.getCount()) + " units" );
        }

        return convertView;
    }
}

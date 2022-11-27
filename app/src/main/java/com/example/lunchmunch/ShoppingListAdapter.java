package com.example.lunchmunch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private ArrayList<Ingredient> dataList;
    Context context;

    static ArrayList<Ingredient> checkedIngr = new ArrayList<>();



    /*private static class ViewHolder {
        TextView tvName;
        TextView tvCount;
        TextView tvCost;
        TextView tvCategory;
        CheckBox cbPurchased;
    }*/


    public ShoppingListAdapter(@NonNull Context context, @NonNull ArrayList<Ingredient> objects) {
        //super(context, resource, objects);
        this.dataList = objects;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_content,null);
        RecyclerView.ViewHolder holder = new ViewHolder(v);
        return (ViewHolder) holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = dataList.get(position);

        holder.tvName.setText(ingredient.getName());
        holder.tvCost.setText(String.valueOf(ingredient.getCost()));
        holder.tvCount.setText(String.valueOf(ingredient.getCount()));
        holder.tvCategory.setText(String.valueOf(ingredient.getCategory()));

        // if the checkbox was clicked
        holder.cbPurchased.setOnClickListener(view -> {
            // if was clicked and read as checked then user checked ingr so add to checked Ingredients
            if (holder.cbPurchased.isChecked()) {
                checkedIngr.add(ingredient);

            } else { // was clicked and not checked meaning user unchecked it so remove from checked Ingredients list
                checkedIngr.remove(ingredient);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        } else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvCount;
        TextView tvCost;
        TextView tvCategory;
        CheckBox cbPurchased;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name_label);
            tvCount = itemView.findViewById(R.id.count_label);
            tvCost = itemView.findViewById(R.id.cost_label);
            tvCategory = itemView.findViewById(R.id.category_label);
            cbPurchased = itemView.findViewById(R.id.shopCheckBox);

        }


    }

    /**
     * Removes data from Shopping list and updates adapter
     * @param position Remove an item from list at position and update
     */
    public void remove(Integer position) {
        if (position < dataList.size()) {
            dataList.remove(position);
            notifyDataSetChanged();
        }

    }

    /**
     * Adds data to Ingredient list and updates adapter
     * @param data List of ingredients to add to list and update adapter
     */
    public void updateList(ArrayList<Ingredient> data) {
        dataList.clear();
        dataList.addAll(data);
        notifyDataSetChanged();
    }
    /**
     * Finds a view given the position of the food item
     * @param position    We will get the food item at this position
     * @param convertView What we are returning
     * @param parent      The parent of the current view
     * @return convertView
     */
    /*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Ingredient ingredient = dataList.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ShoppingListAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ShoppingListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.shopping_list_content, parent, false);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name_label);
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.count_label);
            viewHolder.tvCost = (TextView) convertView.findViewById(R.id.cost_label);
            viewHolder.tvCategory = (TextView) convertView.findViewById(R.id.category_label);
            viewHolder.cbPurchased = (CheckBox) convertView.findViewById(R.id.shopCheckBox);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ShoppingListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.tvName.setText(ingredient.getName());
        viewHolder.tvCost.setText(String.valueOf(ingredient.getCost()));
        viewHolder.tvCount.setText(String.valueOf(ingredient.getCount()));
        viewHolder.tvCategory.setText(String.valueOf(ingredient.getCategory()));

        // if the checkbox was clicked
        viewHolder.cbPurchased.setOnClickListener(view -> {
            // if was clicked and read as checked then user checked ingr so add to checked Ingredients
            if (viewHolder.cbPurchased.isChecked()) {
                checkedIngr.add(ingredient);

            } else { // was clicked and not checked meaning user unchecked it so remove from checked Ingredients list
                checkedIngr.remove(ingredient);
            }
        });


        return convertView;
    }

     */
}



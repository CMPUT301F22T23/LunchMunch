package com.example.lunchmunch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MealPlanItemAdapter extends RecyclerView.Adapter<MealPlanItemAdapter.ViewHolder> {

    private ArrayList<MealPlanItem> dataList;

    @Override
    public int getItemViewType(int position) {
        if (position >= dataList.size()) { return -1; }
        else if (dataList.get(position).getType() == MealPlanItemType.RECIPE) {
            return 0;
        }
        else if (dataList.get(position).getType() == MealPlanItemType.INGREDIENT) {
            return 1;
        }
        return -1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, time, servings, category, cost, unit;
        public ViewHolder(View view, int viewType) {
            super(view);

            if (viewType == 0) {
                image = view.findViewById(R.id.meal_plan_recipe_item_image);
                name = view.findViewById(R.id.sl_item_name);
                time = view.findViewById(R.id.sl_item_unit);
                servings = view.findViewById(R.id.sl_item_cost);
                category = view.findViewById(R.id.sl_item_category);
            }
            else if (viewType == 1) {
                image = view.findViewById(R.id.meal_plan_ingredient_item_image);
                name = view.findViewById(R.id.meal_plan_ingredient_item_name);
                unit = view.findViewById(R.id.meal_plan_ingredient_item_unit);
                cost = view.findViewById(R.id.meal_plan_ingredient_item_cost);
            }

        }
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    public MealPlanItemAdapter(ArrayList<MealPlanItem> recipeList) {
        this.dataList = recipeList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        ViewHolder viewHolder;
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.meal_plan_list_item_recipe_content, parent, false);
            viewHolder = new ViewHolder(itemView, 0);
        }
        else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.meal_plan_list_item_ingredient_content, parent, false);
            viewHolder = new ViewHolder(itemView, 1);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealPlanItem item = dataList.get(position);
        if (item.getType() == MealPlanItemType.RECIPE) {
            holder.name.setText(item.getName());
            holder.servings.setText(item.getServings().toString());
            holder.category.setText(item.getMealType());
            holder.time.setText(item.getPrepTime().toString());
        }
        else {
            holder.name.setText(item.getName());
            holder.unit.setText(item.getCount().toString());
            holder.cost.setText(item.getCost().toString());
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}

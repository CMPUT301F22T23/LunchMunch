package com.example.lunchmunch;

import android.content.Context;
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
    private String day;
    private Boolean isTrashVisible = false;

    private OnAdapterInteractionListener listener;

    public interface OnAdapterInteractionListener {
        void deleteItem(Integer position, String day);
    }


    public void setDay(String day) {
        this.day = day;
    }

    public String getDay(String day) {
        return this.day;
    }

    public void setIsTrashVisible(Boolean visibility) {
        this.isTrashVisible = visibility;
    }


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
        ImageView image, trash;
        TextView name, time, servings, category, cost, unit;
        public ViewHolder(View view, int viewType) {
            super(view);

            if (viewType == 0) {
                image = view.findViewById(R.id.meal_plan_recipe_item_image);
                name = view.findViewById(R.id.meal_plan_recipe_item_name);
                time = view.findViewById(R.id.meal_plan_recipe_item_unit);
                servings = view.findViewById(R.id.meal_plan_recipe_item_cost);
                category = view.findViewById(R.id.meal_plan_recipe_item_category);
                trash = view.findViewById(R.id.meal_plan_item_delete2);
            }
            else if (viewType == 1) {
                image = view.findViewById(R.id.meal_plan_ingredient_item_image);
                name = view.findViewById(R.id.meal_plan_ingredient_item_name);
                unit = view.findViewById(R.id.meal_plan_ingredient_item_unit);
                cost = view.findViewById(R.id.meal_plan_ingredient_item_cost);
                trash = view.findViewById(R.id.meal_plan_item_delete);
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

        // Create view for recipe items
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.meal_plan_list_item_recipe_content, parent, false);
            viewHolder = new ViewHolder(itemView, 0);
        }

        // Create view for ingredient items
        else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.meal_plan_list_item_ingredient_content, parent, false);
            viewHolder = new ViewHolder(itemView, 1);
        }
        if (parent.getContext() instanceof OnAdapterInteractionListener) {
            listener = (OnAdapterInteractionListener) parent.getContext();
        }
        else {
            throw new RuntimeException("must implement listener");
        }



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealPlanItem item = dataList.get(position);
        if (item.getType() == MealPlanItemType.RECIPE) {
            holder.name.setText(item.getName());
            holder.servings.setText(item.getServings().toString() + " " + "servings");
            holder.category.setText(item.getMealType());
            holder.time.setText(item.getPrepTime().toString() + " " + "minutes");
        }
        else {
            holder.name.setText(item.getName());
            String unitNum = "";

            if (item.getCount() > 1)  { unitNum = "units"; }
            else  { unitNum = "unit"; }

            holder.unit.setText(item.getCount().toString() + " " + unitNum);
            holder.cost.setText(String.format("$%.2f", Double.valueOf(item.getCost())));
            holder.image.setImageResource(Ingredient.getCategoryImage(item.getCategory()));
        }

        if (!isTrashVisible) {
            holder.trash.setVisibility(View.GONE);
            return;
        }
        holder.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDeleteItem(holder);
            }
        });

    }

    private void callDeleteItem(ViewHolder holder) {
        listener.deleteItem(holder.getAbsoluteAdapterPosition(), day);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}

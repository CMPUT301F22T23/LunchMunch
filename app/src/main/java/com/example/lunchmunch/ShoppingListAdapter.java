package com.example.lunchmunch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private ArrayList<Ingredient> dataList;
    Context context;

    private ingrPurchasedListener mIngrPurchasedListener;

    public void setIngrPurchasedListener(ingrPurchasedListener iPL) {
        mIngrPurchasedListener = iPL;
    }

    public interface ingrPurchasedListener {
        void ingrPurchasedBtnClicked(Ingredient ingredient, Integer ingrIdx);
    }


    public ShoppingListAdapter(@NonNull Context context, @NonNull ArrayList<Ingredient> objects) {
        //super(context, resource, objects);
        this.dataList = objects;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_content, null);
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
        holder.tvDescription.setText(ingredient.getDescription());

        // init to always be unchecked (for the case when user clicks on listview to only edit quant)
        holder.ingrPurchasedBtn.setChecked(false);

        //if (holder.ingrPurchasedBtn.isChecked()) {

        //}

        holder.ingrPurchasedBtn.setOnClickListener(view -> {
            mIngrPurchasedListener.ingrPurchasedBtnClicked(ingredient, position);
        });

    }

    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        } else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvCount;
        TextView tvCost;
        TextView tvCategory;
        TextView tvDescription;
        CheckBox ingrPurchasedBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name_label);
            tvCount = itemView.findViewById(R.id.count_label);
            tvCost = itemView.findViewById(R.id.cost_label);
            tvCategory = itemView.findViewById(R.id.category_label);
            tvDescription = itemView.findViewById(R.id.description_label);
            ingrPurchasedBtn = itemView.findViewById(R.id.ingrPurchasedBtn);
        }
    }
}


package com.example.lunchmunch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class FoodItemAdapter extends ArrayAdapter<Ingredient> {

    private ArrayList<Ingredient> dataList;
    Context context;

    public void updateList(ArrayList<Ingredient> data) {
        dataList.clear();
        dataList.addAll(data);
        System.out.println(dataList.get(0).getName());
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvCount;
        TextView tvCost;
    }


    public FoodItemAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Ingredient> objects) {
        super(context, resource, objects);
        this.dataList = objects;
        this.context = context;

    }

    public void remove(Integer position) {
        if (position < dataList.size()) {
            dataList.remove(position);
            notifyDataSetChanged();
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Ingredient foodItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.ingredient_list_content, parent, false);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name_label);
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.count_label);
            viewHolder.tvCost = (TextView) convertView.findViewById(R.id.cost_label);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.tvName.setText(foodItem.getName());
        viewHolder.tvCost.setText(String.valueOf(foodItem.getCost()));
        viewHolder.tvCount.setText(String.valueOf(foodItem.getCount()));

        return convertView;
    }
}

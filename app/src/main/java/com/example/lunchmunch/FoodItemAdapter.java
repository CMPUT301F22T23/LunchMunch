package com.example.lunchmunch;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;


import java.util.ArrayList;
import java.util.List;

public class FoodItemAdapter extends ArrayAdapter<FoodItemClass> {

    private ArrayList<FoodItemClass> dataList;
    Context context;


//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        System.out.println("youtmom");
//        Intent intent = new Intent(context, AddFoodItem.class);
//        intent.putExtra("POSITION", i);
//        context.startActivity(intent);
//
//    }


    public void updateList(ArrayList<FoodItemClass> data) {
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


    public FoodItemAdapter(@NonNull Context context, int resource, @NonNull ArrayList<FoodItemClass> objects) {
        super(context, resource, objects);
        this.dataList = objects;
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FoodItemClass foodItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.ingredient_list_content, parent, false);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.count);
            viewHolder.tvCost = (TextView) convertView.findViewById(R.id.cost);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
        viewHolder.tvName.setText(foodItem.getName());
        viewHolder.tvCost.setText(String.valueOf(foodItem.getCost()));
        viewHolder.tvCount.setText(String.valueOf(foodItem.getCount()));
        // Return the completed view to render on screen
        return convertView;
    }
}

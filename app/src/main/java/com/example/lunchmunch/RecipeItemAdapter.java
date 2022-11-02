package com.example.lunchmunch;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.net.URI;
import java.util.ArrayList;

public class RecipeItemAdapter extends ArrayAdapter<Recipe> {

    private ArrayList<Recipe> dataList;
    Context context;

    public void updateList(ArrayList<Recipe> data) {
        dataList.clear();
        dataList.addAll(data);
        System.out.println(dataList.get(0).getName());
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView tvName;
        ImageView tvImage;
        TextView tvServings;
        TextView tvPrepTime;
        TextView tvType;
    }


    public RecipeItemAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Recipe> objects) {
        super(context, resource, objects);
        this.dataList = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Recipe foodItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.recipe_list_content, parent, false);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.r_name);
            viewHolder.tvImage = (ImageView) convertView.findViewById(R.id.r_image);
            viewHolder.tvServings = (TextView) convertView.findViewById(R.id.r_servings);
            viewHolder.tvPrepTime = (TextView) convertView.findViewById(R.id.r_prep_time);
            viewHolder.tvType = (TextView) convertView.findViewById(R.id.r_type);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (foodItem.getName() != null) {
            viewHolder.tvName.setText(foodItem.getName());
            Glide.with(getContext()).load(foodItem.getImage()).apply(RequestOptions.circleCropTransform()).into(viewHolder.tvImage);
            viewHolder.tvServings.setText(Integer.toString(foodItem.getServings()) + " servings");
            viewHolder.tvPrepTime.setText(Integer.toString(foodItem.getPrepTime()) + " minutes");
            viewHolder.tvType.setText(foodItem.getMealType());
        }
        // Return the completed view to render on screen
        return convertView;
    }
}

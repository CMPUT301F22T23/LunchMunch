package com.example.lunchmunch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
/*
* First fragment to edit Meal Plan details for a certain date
* */
public class MealPlanDateFragment extends DialogFragment implements AdapterView.OnItemSelectedListener, MealPlanItemAdapter.OnAdapterInteractionListener {
    View view;
    MealPlanItemAdapter adapter;
    public OnFragmentInteractionListener listener;
    public MealPlanItemAdapter.OnAdapterInteractionListener adapterInteractionListener;

    RecyclerView recyclerView;
    TextView titleTextView;
    ArrayList<MealPlanItem> dataList;
    MealPlanIngredientFragment  ingredientFragment;
    MealPlanRecipeFragment recipeFragment;
    String day;



    @Override
    public void deleteItem(Integer position, String day) {

    }

    public interface OnFragmentInteractionListener {
    }

    /**
     * Setter for specific day this fragment is associated with
     */

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay() {
        return this.day;
    }

    /**
     * Sets the list of MealPlanItems for the fragment and recyclerView adapter
     * @param dataList A list of MealPlanItems
     * @see MealPlanItem
     * @see MealPlanItemAdapter
     */
    public void setDataList(ArrayList<MealPlanItem> dataList) {
        this.dataList = dataList;
        if (recyclerView != null) {
            adapter = new MealPlanItemAdapter(dataList);
            adapter.setIsTrashVisible(true);
            adapter.setDay(day);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context + "must implement listener");
        }

        if (context instanceof MealPlanItemAdapter.OnAdapterInteractionListener) {
            adapterInteractionListener = (MealPlanItemAdapter.OnAdapterInteractionListener) context;
        }
        else {
            throw new RuntimeException(context + "must implement listener");
        }
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.meal_plan_fragment, null);

        // Set Recycler View
        recyclerView = view.findViewById(R.id.fragment_meal_plan_items);
        adapter = new MealPlanItemAdapter(dataList);
        adapter.setDay(day);
        adapter.setIsTrashVisible(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        // Set Fragment Title
        titleTextView = view.findViewById(R.id.meal_plan_fragment_title);
        titleTextView.setText(day.substring(0, 1).toUpperCase() + day.substring(1));



        final AlertDialog alert = new AlertDialog.Builder(getContext())
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", null)
                .create();



        // Open Ingredient Fragment Button
        final TextView addFromIngredientsButton = view.findViewById(R.id.add_from_ingredients);
        ingredientFragment = new MealPlanIngredientFragment();
        addFromIngredientsButton.setOnClickListener(view ->
                ingredientFragment.show(getParentFragmentManager(), "Add From Ingredients"));

        // Pass in date to ingredient fragment
        ingredientFragment.setDay(day);

        // Open Recipe Fragment Button
        final TextView addFromRecipesButton = view.findViewById(R.id.add_from_recipes);
        recipeFragment = new MealPlanRecipeFragment();
        addFromRecipesButton.setOnClickListener(view ->
                recipeFragment.show(getParentFragmentManager(), "Add From Recipes"));

        // Pass in date to recipe fragment
        recipeFragment.setDay(day);

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button cancelBtn = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
                cancelBtn.setTextColor(Color.BLACK);
                cancelBtn.setOnClickListener(view -> {
                    alert.dismiss();
                });

                // using inside onShow allows us to close the dialog when we want (even if user pressed positive button (in this case would have invalid inputs))
                Button saveBtn = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                saveBtn.setTextColor(Color.BLACK);
                saveBtn.setOnClickListener(view -> {
                    alert.dismiss();
                });


            }
        });

        return alert;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}

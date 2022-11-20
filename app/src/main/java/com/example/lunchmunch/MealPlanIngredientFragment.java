package com.example.lunchmunch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Date;

public class MealPlanIngredientFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    View view;
    private MealPlanDateFragment.OnFragmentInteractionListener listener;
    FoodItemAdapter adapter;
    ArrayList<Ingredient> dataList;

    public interface OnFragmentInteractionListener {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MealPlanDateFragment.OnFragmentInteractionListener) {
            listener = (MealPlanDateFragment.OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context + "must implement listener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.meal_plan_add_ingredient_fragment, null);

        ListView mealPlanIngredientListView = view.findViewById(R.id.ingredient_list);
        dataList = new ArrayList<>();
        dataList.add(new Ingredient("banana", "yellow fruit", new Date(), Location.FREEZER, 1,2,IngredientCategory.MEAT));
        adapter = new FoodItemAdapter(getContext(),R.layout.meal_plan_add_ingredient_fragment, dataList);
        mealPlanIngredientListView.setAdapter(adapter);


        final AlertDialog alert = new AlertDialog.Builder(getContext())
                .setView(view)
                .setNegativeButton("Delete", null)
                .setPositiveButton("Save", null)
                .create();


        alert.show();

        return alert;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

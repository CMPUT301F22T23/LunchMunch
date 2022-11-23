package com.example.lunchmunch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MealPlanDateFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    View view;
    MealPlanItemAdapter adapter;
    ArrayList<MealPlanItem> dataList;

    MealPlanIngredientFragment  ingredientFragment;

    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
    }

    public void setDataList(ArrayList<MealPlanItem> dataList) {
        this.dataList = dataList;
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
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.meal_plan_fragment, null);

        RecyclerView recyclerView = view.findViewById(R.id.fragment_meal_plan_items);
        adapter = new MealPlanItemAdapter(dataList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        // Open Ingredient Fragment Button
        final TextView addFromIngredientsButton = view.findViewById(R.id.add_from_ingredients);
        ingredientFragment = new MealPlanIngredientFragment();
        addFromIngredientsButton.setOnClickListener(view ->
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        // Can't figure out how to do this -> what id?
                        .replace(R.id.meal_plan_fragment_frame, ingredientFragment, "Ingredient Fragment")
                        .addToBackStack(null)
                        .commit());

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

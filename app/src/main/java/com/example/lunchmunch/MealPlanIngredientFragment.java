package com.example.lunchmunch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Date;
/*
Second fragment to select ingredients for Meal plan
* */

public class MealPlanIngredientFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    View view;

    FirebaseFirestore db;
    CollectionReference IngrCollec;

    private OnFragmentInteractionListener listener;
    FoodItemAdapter adapter;
    ArrayList<Ingredient> dataList;
    Integer selectedItem = -1;
    String day;

    /**
     * Interface for interacting with Ingredient fragment to work with the database
     * Implemented in MealPlanActivity
     * @see         MealPlanActivity
     */

    public interface OnFragmentInteractionListener {
        void onIngredientOkPressed(Ingredient ingredient, String day);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MealPlanDateFragment.OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context + "must implement listener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = getLayoutInflater().inflate(R.layout.meal_plan_add_ingredient_fragment, null);

        // init firebase reference
        db = FirebaseFirestore.getInstance();
        IngrCollec = db.collection("Ingredients");

        initDBListener(IngrCollec);

        ListView mealPlanIngredientListView = view.findViewById(R.id.ingredient_list);
        adapter = new FoodItemAdapter(getContext(),R.layout.meal_plan_add_ingredient_fragment, dataList);
        mealPlanIngredientListView.setAdapter(adapter);

        mealPlanIngredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (selectedItem != -1) {
                    adapterView.getChildAt(selectedItem).setBackgroundColor(Color.WHITE);
                }
                if (selectedItem == i) {
                    adapterView.getChildAt(selectedItem).setBackgroundColor(Color.WHITE);
                    selectedItem = -1;
                } else{
                    selectedItem = i;
                    adapterView.getChildAt(i).setBackgroundColor(Color.GRAY);
                }


            }
        });


        final AlertDialog alert = new AlertDialog.Builder(getContext())
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", null)
                .create();


        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button cancelBtn = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
                cancelBtn.setTextColor(Color.BLACK);
                cancelBtn.setOnClickListener(view -> {
                    selectedItem = -1;
                    alert.dismiss();
                });

                // using inside onShow allows us to close the dialog when we want (even if user pressed positive button (in this case would have invalid inputs))
                Button saveBtn = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                saveBtn.setTextColor(Color.BLACK);
                saveBtn.setOnClickListener(view -> {
                    if (selectedItem > -1 && selectedItem < dataList.size()) {
                        listener.onIngredientOkPressed(dataList.get(selectedItem), getDay());
                    }
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

    /**
     * Gets the list of ingredients from Ingredient DB collection
     * <ul>
     *     <li>initializes Ingredient listview and adapter</li>
     *     <li>Clears data list of items and re-adds</li>
     * </ul>
     *
     *
     * @param ingrCollec A reference to the Ingredeint collection in the database
     * @see         Ingredient
     */

    public void initDBListener(CollectionReference ingrCollec) {
        dataList = new ArrayList<>();

        ingrCollec.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    // clear current list and re-add all the current ingredients
                    dataList.clear();
                    // each document in the Ingredients collection is an Ingredient class object
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Timestamp timestamp = (Timestamp) document.getData().get("bestBefore");
                        Date bestBefore;
                        if (timestamp == null) {
                            bestBefore = null;
                        }else{
                            bestBefore = timestamp.toDate();
                        }

                        Float cost = new Float(0);
                        Float count = new Float(0);
                        if (document.getData().get("cost") instanceof Double) {
                            cost = ((Double) document.getData().get("cost")).floatValue();

                        } else {
                            cost = ((Long) document.getData().get("cost")).floatValue();

                        }

                        if (document.getData().get("count") instanceof Double) {
                            count = ((Double) document.getData().get("count")).floatValue();

                        } else {
                            count = ((Long) document.getData().get("count")).floatValue();

                        }

                        Ingredient ingredient = new Ingredient(
                                (String) document.getData().get("name"),
                                (String) document.getData().get("description"),
                                bestBefore,
                                Location.valueOf(document.getData().get("location").toString().toUpperCase()),
                                count,
                                cost,
                                IngredientCategory.valueOf(document.getData().get("category").toString().toUpperCase())
                        );

                        dataList.add(ingredient);

                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}

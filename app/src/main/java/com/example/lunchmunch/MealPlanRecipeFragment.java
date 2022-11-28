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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MealPlanRecipeFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    View view;
    FirebaseFirestore db;
    CollectionReference RecipeCollec;
    private OnFragmentInteractionListener listener;
    RecipeItemAdapter adapter;

    ArrayList<Recipe> dataList = new ArrayList<>();
    Integer selectedItem = -1;
    String day;

    public interface OnFragmentInteractionListener {
        void onRecipeOkPressed(Recipe recipe, String day);
    }

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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.meal_plan_add_recipe_fragment, null);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
        // init db for recipes
        db = FirebaseFirestore.getInstance();
        RecipeCollec = db.collection("Recipes");

        initDBListener(RecipeCollec);

        ListView mealPlanIngredientListView = view.findViewById(R.id.recipeListView);
        adapter = new RecipeItemAdapter(getContext(), R.layout.meal_plan_add_recipe_fragment, dataList);
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
                    try {
                        adapterView.getChildAt(selectedItem).setBackgroundColor(Color.LTGRAY);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                        listener.onRecipeOkPressed(dataList.get(selectedItem), getDay());
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

    void initDBListener(CollectionReference recipeCollec) {

        recipeCollec.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    // clear current list and re-add all the current ingredients
                    dataList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Make sure we add our ID to the recipe class for later use
                        Recipe recipe = document.toObject(Recipe.class);
                        recipe.setId(document.getId());
                        List<String> ingredientNames = recipe.getIngredientNames();
                        List<Ingredient> ingredients;
                        ingredients = recipe.getIngredients();
                        if (ingredients == null) {
                            ingredients = new ArrayList<>();
                        }
                        Recipe updatedRecipe = new Recipe(
                                recipe.getId(),
                                recipe.getName(),
                                ingredients, //update Recipe with the list of Ingredients as Ingredients class instances
                                recipe.getIngredientNames(),
                                recipe.getInstructions(),
                                recipe.getMealType(),
                                recipe.getImage(),
                                recipe.getServings(),
                                recipe.getPrepTime(),
                                recipe.getComments(),
                                recipe.getCxt()
                        );

                        dataList.add(updatedRecipe);



                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}

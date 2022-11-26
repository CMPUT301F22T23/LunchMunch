package com.example.lunchmunch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


import com.example.lunchmunch.databinding.RecipeFragmentBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fragment for adding/editing Recipe functionality
 */
public class RecipeFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    private View view;
    private RecipeFragmentBinding binding;
    private OnFragmentInteractionListener listener;
    String mealType = "";
    EditText recipeName;
    EditText recipeInstructions;
    private EditText recipeImage;
    EditText servings;
    EditText prepTime;
    private EditText comments;
    private Spinner spinner;
    private Recipe recipe;

    private TextView ingredientsList;
    ImageButton editIngredient;


    public RecipeFragment() {
    }


    public RecipeFragment(Recipe recipe) {
        this.recipe = recipe;
    }


    // Interaction with fragment
    public interface OnFragmentInteractionListener {
        void onOkPressed(Recipe recipe, Boolean isNew, int position);

        void deleteRecipe(int position);

    }

    ;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = RecipeFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_item_dialog, null);
        recipeName = view.findViewById(R.id.recipeName);
        recipeInstructions = view.findViewById(R.id.recipeInstructions);
        recipeImage = view.findViewById(R.id.recipeImage);
        servings = view.findViewById(R.id.servings);
        prepTime = view.findViewById(R.id.prepTime);
        comments = view.findViewById(R.id.comments);
        spinner = (Spinner) view.findViewById(R.id.mealType);
        
        ingredientsList = view.findViewById(R.id.ingredient_list);
        editIngredient = view.findViewById(R.id.editIngredientsList);



        if (recipe != null) {
            recipeName.setText(recipe.getName());
            recipeInstructions.setText(recipe.getInstructions());
            recipeImage.setText(recipe.getImage());
            servings.setText(Integer.toString(recipe.getServings()));
            prepTime.setText(Integer.toString(recipe.getPrepTime()));
            comments.setText(recipe.getComments());
            mealType = recipe.getMealType();
            spinner.setSelection(getMealTypeIndex(mealType));
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.meal_type, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        editIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), RecipeIngrPage.class);
                startActivity(intent);
            }
        });


        boolean isNew = recipe == null;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AddRecipeCustomAlertDialog);
        builder.setView(view)
                .setTitle("Add Recipe")
                .setPositiveButton("OK", (dialog, id) -> {
                    int servs = 0;
                    int prep = 0;

                    try {
                        servs = Integer.parseInt(servings.getText().toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        prep = Integer.parseInt(prepTime.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (recipe == null) {
                        List<String> ingredients = new ArrayList<String>();
                        recipe = new Recipe(recipeName.getText().toString(), ingredients, recipeInstructions.getText().toString(), mealType, recipeImage.getText().toString(), servs, prep, comments.getText().toString());
                    }
                    {
                        recipe.setName(recipeName.getText().toString());
                        recipe.setInstructions(recipeInstructions.getText().toString());
                        recipe.setImage(recipeImage.getText().toString());
                        recipe.setServings(servs);
                        recipe.setPrepTime(prep);
                        recipe.setComments(comments.getText().toString());
                        recipe.setMealType(mealType);
                    }

                    if (listener != null) {
                        if (isNew) {
                            listener.onOkPressed(recipe, true, -1);
                        } else {
                            listener.onOkPressed(recipe, false, getArguments().getInt("position"));
                        }
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    // User cancelled the dialog
                });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(a -> {
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            //positive.setBackgroundResource(R.drawable.ic_save);
            positive.setTextColor(Color.BLACK);
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            //negative.setBackgroundResource(R.drawable.ic_delete);
            negative.setTextColor(Color.BLACK);

        });
        return dialog;
    }

    int getMealTypeIndex(String mealType) {
        List<String> mealTypes = Arrays.asList(getResources().getStringArray(R.array.meal_type));
        return mealTypes.indexOf(mealType);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeFragment.OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement listener");
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        FragmentActivity activity = getActivity();
        //Find the recipe modal fragment
        assert activity != null;
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag("Show Recipe");
        // Remove the fragment and start a new one with the changed recipe

        if (fragment != null) {
            RecipeModalFragment recipeModalFragment = (RecipeModalFragment) fragment;
            recipeModalFragment.recipe = recipe;
            recipeModalFragment.updateRecipe();
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mealType = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
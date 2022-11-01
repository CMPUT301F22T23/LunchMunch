package com.example.lunchmunch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.lunchmunch.databinding.RecipeFragmentBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    private View view;
    private RecipeFragmentBinding binding;
    private OnFragmentInteractionListener listener;
    EditText ingredientList;
    EditText recipeName;
    EditText recipeInstructions;
    EditText recipeImage;
    EditText servings;
    EditText prepTime;
    EditText comments;



    // Interaction with fragment
    public interface OnFragmentInteractionListener {
        void onOkPressed(Recipe recipe);
        void deleteRecipe();
    };

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
        ingredientList = view.findViewById(R.id.ingredientList);
        recipeName = view.findViewById(R.id.recipeName);
        recipeInstructions = view.findViewById(R.id.recipeInstructions);
        recipeImage = view.findViewById(R.id.recipeImage);
        servings = view.findViewById(R.id.servings);
        prepTime = view.findViewById(R.id.prepTime);
        comments = view.findViewById(R.id.comments);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AddRecipeCustomAlertDialog);
        builder.setView(view)
                .setPositiveButton("OK", (dialog, id) -> {
                    if (listener != null) {
                        List<String> ingredients = new ArrayList<>(Arrays.asList(ingredientList.getText().toString().split(",")));
                        Integer servs = Integer.parseInt(servings.getText().toString());
                        Integer prep = Integer.parseInt(prepTime.getText().toString());
                        listener.onOkPressed(new Recipe(recipeName.getText().toString(), ingredients, recipeInstructions.getText().toString(),"", recipeImage.getText().toString(), servs, prep, comments.getText().toString()));
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    // User cancelled the dialog
                })
                .setNeutralButton("Delete", (dialog, id) -> {
                    if (listener != null) {
                        listener.deleteRecipe();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(a -> {
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setBackgroundResource(R.drawable.ic_save);
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setBackgroundResource(R.drawable.ic_delete);
        });
        return dialog;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
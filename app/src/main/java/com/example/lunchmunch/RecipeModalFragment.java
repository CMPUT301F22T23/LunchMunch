package com.example.lunchmunch;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class RecipeModalFragment extends BottomSheetDialogFragment {
    Recipe recipe;
    TextView recipeName;
    TextView recipeInstructions;
    TextView recipeTime;
    TextView servings;
    ImageView recipeImage;



    
    public RecipeModalFragment() {
        // Required empty public constructor
    }

    public RecipeModalFragment(Recipe recipe) {
        // Required empty public constructor
        this.recipe = recipe;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.recipe_modal_bottom, container, false);

        recipeName = view.findViewById(R.id.recipeTitle);
        recipeInstructions = view.findViewById(R.id.recipeInstructionModal);
        recipeTime = view.findViewById(R.id.recipeTime);
        servings = view.findViewById(R.id.recipeServings);
        recipeImage = view.findViewById(R.id.recipeImageModal);

        if (recipe != null) {
            recipeName.setText(recipe.getName());
            recipeInstructions.setText(recipe.getInstructions());
            recipeTime.setText(Integer.toString(recipe.getPrepTime()));
            servings.setText(Integer.toString(recipe.getServings()));
            Glide.with(this).load(recipe.getImage()).into(recipeImage);
        }

        return view;

    }
}

package com.example.lunchmunch;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
/**
 * Fragment for adding/editing RecipeModal functionality
 */
public class RecipeModalFragment extends BottomSheetDialogFragment {
    Recipe recipe;
    TextView recipeName;
    TextView recipeInstructions;
    TextView recipeTime;
    TextView servings;
    TextView mealType;
    TextView recipeComments;
    CardView recipeImage;
    ImageView editRecipe;
    ImageView deleteRecipe;
    TextView recipeInstructionHeader;
    TextView recipeCommentsHeader;
    ImageView recipeAddIngredient;
    ListView recipeIngredients;
    FoodItemAdapter recipeIngredientsAdapter;


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
        mealType = view.findViewById(R.id.recipeMeal);
        editRecipe = view.findViewById(R.id.editRecipe);
        deleteRecipe = view.findViewById(R.id.deleteRecipe);
        recipeComments = view.findViewById(R.id.recipeComments);
        recipeAddIngredient = view.findViewById(R.id.recipeAddIngredient);
        recipeCommentsHeader = view.findViewById(R.id.recipeCommentHeader);
        recipeInstructionHeader = view.findViewById(R.id.recipeInstructionHeader);
        recipeIngredients = view.findViewById(R.id.recipeIngredients);
        // Add our ingredients to our list view
        System.out.println(recipe);
        // print recipe type
        System.out.println(recipe.getClass());
        recipeIngredientsAdapter = new FoodItemAdapter(getContext(), R.layout.recipe_modal_bottom, (ArrayList<Ingredient>) recipe.getIngredients());
        recipeIngredients.setAdapter(recipeIngredientsAdapter);


        recipeAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeIngredientFragment recipeIngredientFragment = new RecipeIngredientFragment(recipe, recipeIngredientsAdapter);
                Bundle bundle = new Bundle();
                assert getArguments() != null;
                bundle.putInt("position", getArguments().getInt("position"));
                recipeIngredientFragment.setArguments(bundle);
                recipeIngredientFragment.show(getChildFragmentManager(), "RecipeIngredientFragment");
            }
        });

        if (recipe != null) {
            recipeName.setText(recipe.getName());
            recipeInstructions.setText(recipe.getInstructions());
            recipeTime.setText(String.format("%s minutes", Integer.toString(recipe.getPrepTime())));
            servings.setText(String.format("%s servings", Integer.toString(recipe.getServings())));
            mealType.setText(recipe.getMealType());
            recipeComments.setText(recipe.getComments());

            if (recipe.getComments().equals("")) {
                recipeComments.setVisibility(View.GONE);
                recipeCommentsHeader.setVisibility(View.GONE);
            } else {
                recipeComments.setVisibility(View.VISIBLE);
                recipeCommentsHeader.setVisibility(View.VISIBLE);
            }
            if (recipe.getInstructions().equals("")) {
                recipeInstructions.setVisibility(View.GONE);
                recipeInstructionHeader.setVisibility(View.GONE);
            } else {
                recipeInstructions.setVisibility(View.VISIBLE);
                recipeInstructionHeader.setVisibility(View.VISIBLE);
            }

            if (recipe.getImage() != null) {
                Glide.with(requireContext()).load(recipe.getImage()).into((ImageView) recipeImage.findViewById(R.id.recipeImageItem));
            }
            }

        deleteRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current activity
                Context context = getContext();
                RecipeActivity activity = (RecipeActivity) context;
                assert getArguments() != null;
                assert activity != null;
                activity.deleteRecipe(getArguments().getInt("position"));
                dismiss();
            }
        });


        editRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeFragment recipeFrag = new RecipeFragment(recipe);
                // Pass our arguments across fragments

                System.out.println("edit args " + getArguments());
                recipeFrag.setArguments(getArguments());

                recipeFrag.show(getParentFragmentManager(), "RecipeFragment");
            }
        });

        recipeIngredients.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecipeIngredientFragment recipeIngredientFragment = new RecipeIngredientFragment(recipe, recipeIngredientsAdapter);
                Bundle bundle = new Bundle();
                assert getArguments() != null;
                bundle.putInt("position", getArguments().getInt("position"));
                bundle.putInt("currentIngredientPosition", position);
                // Add the array adapter
                recipeIngredientFragment.setArguments(bundle);
                recipeIngredientFragment.show(getChildFragmentManager(), "RecipeIngredientFragment");
            }
        });

        return view;

    }

    public void updateRecipe() {
        recipeName.setText(recipe.getName());
        recipeInstructions.setText(recipe.getInstructions());
        recipeTime.setText(Integer.toString(recipe.getPrepTime()) + " minutes");
        servings.setText(Integer.toString(recipe.getServings()) + " servings");
        mealType.setText(recipe.getMealType());
        recipeComments.setText(recipe.getComments());

        if (recipe.getComments().equals("")) {
            recipeComments.setVisibility(View.GONE);
            recipeCommentsHeader.setVisibility(View.GONE);
        } else{
            recipeComments.setVisibility(View.VISIBLE);
            recipeCommentsHeader.setVisibility(View.VISIBLE);
        }


        if (recipe.getInstructions().equals("")) {
            recipeInstructions.setVisibility(View.GONE);
            recipeInstructionHeader.setVisibility(View.GONE);
        } else {
            recipeInstructions.setVisibility(View.VISIBLE);
            recipeInstructionHeader.setVisibility(View.VISIBLE);
        }

        if (recipe.getImage() != null) {
            Glide.with(requireContext()).load(recipe.getImage()).into((ImageView) recipeImage.findViewById(R.id.recipeImageItem));
        }


    }

}

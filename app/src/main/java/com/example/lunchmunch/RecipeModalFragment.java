package com.example.lunchmunch;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * Fragment for adding/editing RecipeModal functionality
 */
public class RecipeModalFragment extends DialogFragment implements AdapterView.OnItemClickListener {
    Recipe recipe;
    private RecipeFragment.OnFragmentInteractionListener listener;
    TextView recipeName;
    TextView recipeInstructions;
    TextView recipeTime;
    TextView servings;
    TextView mealType;
    TextView recipeComments;
    CardView recipeImage;
    ImageView editRecipe;
    ImageView deleteRecipe;
    ImageView recipeScalePlus;
    ImageView recipeScaleMinus;
    TextView recipeInstructionHeader;
    TextView recipeCommentsHeader;
    ImageView recipeAddIngredient;
    ImageView recipeCancel;
    ListView recipeIngredients;
    FoodItemAdapter recipeIngredientsAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,
                R.style.FullScreenDialog);
    }


    public RecipeModalFragment() {
        // Required empty public constructor
    }

    public RecipeModalFragment(Recipe recipe) {
        // Required empty public constructor
        this.recipe = recipe;
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
        recipeScalePlus = view.findViewById(R.id.recipeScalePlus);
        recipeScaleMinus = view.findViewById(R.id.recipeScaleMinus);
        // Add our ingredients to our list view
        if (recipe != null) {
            recipeIngredientsAdapter = new FoodItemAdapter(getContext(), R.layout.recipe_modal_bottom, (ArrayList<Ingredient>) recipe.getIngredients());
            recipeIngredients.setAdapter(recipeIngredientsAdapter);
        }



        recipeCancel = view.findViewById(R.id.cancelRecipe);
        recipeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

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

        recipeScaleMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current activity
                recipe.scaleRecipe(-1);
                updateRecipe();
                recipeIngredientsAdapter.notifyDataSetChanged();
            }
        });

        recipeScalePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current activity
                recipe.scaleRecipe(1);
                updateRecipe();
                recipeIngredientsAdapter.notifyDataSetChanged();

                if (listener != null) {
                    try {
                        listener.onOkPressed(recipe, false, getArguments().getInt("position"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


        editRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeFragment recipeFrag = new RecipeFragment(recipe, recipeIngredientsAdapter);
                // Pass our arguments across fragments
                assert getArguments() != null;
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



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }



    @Override
    public void onDetach() {
        super.onDetach();

    }
    /**
     * Update the recipe component from data found in the modal.
     */
    public void updateRecipe() {
        if (recipe == null)
            return;
        recipeName.setText(recipe.getName());
        recipeInstructions.setText(recipe.getInstructions());
        recipeTime.setText(Integer.toString(recipe.getPrepTime()) + " minutes");
        servings.setText(Integer.toString(recipe.getServings()) + " servings");
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeFragment.OnFragmentInteractionListener) {
            listener = (RecipeFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement listener");
        }
    }
}

package com.example.lunchmunch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;


import com.example.lunchmunch.databinding.RecipeFragmentBinding;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.grpc.internal.JsonUtil;

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
    EditText servings;
    EditText prepTime;
    private EditText comments;
    private Spinner spinner;

    TextView ingredientNamesList;
    ImageButton editIngredient;

    // these are used as a way to pass the ingredient list attribute from the recipe object being created to the next activity
    ArrayList<Ingredient> listToPass;
    ArrayList<Ingredient> blankIngredients = new ArrayList<Ingredient>();
    List<String> blankNames;
    ArrayList<Recipe> recipesList;
    private FoodItemAdapter foodItemAdapter;
    private Recipe recipe;

    public RecipeFragment() {
    }


    public RecipeFragment(Recipe recipe) {
        this.recipe = recipe;
    }
    public RecipeFragment(Recipe recipe, FoodItemAdapter recipeIngredientsAdapter) {
        this.recipe = recipe;
        foodItemAdapter = recipeIngredientsAdapter;
    }


    // Interaction with fragment
    public interface OnFragmentInteractionListener {
        void onOkPressed(Recipe recipe, Boolean isNew, int position) throws IOException, JSONException;
        void deleteRecipe(int position);
    }



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

        servings = view.findViewById(R.id.servings);
        prepTime = view.findViewById(R.id.prepTime);
        comments = view.findViewById(R.id.comments);
        spinner = (Spinner) view.findViewById(R.id.mealType);
        ingredientNamesList = view.findViewById(R.id.ingredientsList);
        editIngredient = view.findViewById(R.id.editIngredientsList);
        blankNames = new ArrayList<String>();



        Bundle bundle = getArguments();

        recipesList = (ArrayList<Recipe>) bundle.getSerializable("recipesList");
        if(bundle.getSerializable("position") == null){
            recipe = new Recipe("", blankIngredients, blankNames, "","","",0,0,"");
        } else {

        }



        if (recipe != null) {
            recipeName.setText(recipe.getName());
            recipeInstructions.setText(recipe.getInstructions());
            servings.setText(Integer.toString(recipe.getServings()));
            prepTime.setText(Integer.toString(recipe.getPrepTime()));
            comments.setText(recipe.getComments());
            mealType = recipe.getMealType();
            spinner.setSelection(getMealTypeIndex(mealType));
            if (!recipe.getName().isEmpty()) {
                editIngredient.setVisibility(View.GONE);
                ingredientNamesList.setVisibility(View.GONE);
            }
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.meal_type, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Intent intent = new Intent(getActivity().getApplicationContext(), RecipeIngrPage.class);



        ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        recipe = (Recipe) data.getSerializableExtra("Recipe");
                        // Remove recipefragments from supportFragmentManager except for the first one
                        // This is to prevent the user from being able to go back to the recipe fragment
                        // after they have already created a recipe

                        if (getParentFragmentManager().getFragments().size() > 4) {
                            Fragment fragment = getParentFragmentManager().findFragmentByTag("RecipeFragment");
                            assert fragment != null;
                            getParentFragmentManager().beginTransaction().remove(fragment).commit();
                            getParentFragmentManager().popBackStack();
                        }
                        // This will not work however if you look at the println of recipe.getIngredients() it will show the correct list :)
                        System.out.println("Ingredients " + recipe.getIngredients());
                        String names = "";
                        for (int i = 0; i < recipe.getIngredients().size(); i++) {
                            names += recipe.getIngredients().get(i).getName() + " ";
                        }

                        ingredientNamesList.setText(names);



                    }
                });

        editIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                intent.putExtra("Recipe", recipe);
                startForResult.launch(intent);

            }
        });


        //boolean isNew = recipe == null;

        boolean isNew = false;

        if(recipesList.contains(recipe) == false){
            isNew = true;
        } else{
            isNew = false;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AddRecipeCustomAlertDialog);
        boolean finalIsNew = isNew;
        builder.setView(view)
                .setTitle("Add/Edit Recipe")
                .setPositiveButton("OK", (dialog, id) -> {


                            //Do nothing here because we override this button later to change the close behaviour.
                            //However, we still need this because on older versions of Android unless we
                            //pass a handler the button doesn't get instantiated
                            int servs = 1;
                            int prep = 1;

                            String recipeNameString = recipeName.getText().toString();
                            if (recipeNameString.isEmpty()) {
                                recipeNameString = "Recipe Name";
                            }

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
                                recipe = new Recipe(recipeNameString, ingredients, recipeInstructions.getText().toString(), mealType, "", servs, prep, comments.getText().toString());
                            }
                            {
                                recipe.setName(recipeNameString);
                                recipe.setInstructions(recipeInstructions.getText().toString());
                                recipe.setServings(servs);
                                recipe.setPrepTime(prep);
                                recipe.setComments(comments.getText().toString());
                                recipe.setMealType(mealType);
                            }
                            System.out.println("Listener "+ listener);
                            if (listener != null) {
                                if (finalIsNew) {
                                    try {
                                        listener.onOkPressed(recipe, true, -1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    try {
                                        listener.onOkPressed(recipe, false, getArguments().getInt("position"));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                if (foodItemAdapter != null) {
                                    System.out.println("Adapter " + adapter);
                                    foodItemAdapter.notifyDataSetChanged();

                                }
                            }


                            dialog.dismiss();

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
            if (recipe != null) {
                String names = "";
                for (int i = 0; i < recipe.getIngredients().size(); i++) {
                    if (i == recipe.getIngredients().size() - 1) {
                        names += recipe.getIngredients().get(i).getName();
                    } else {
                        names += recipe.getIngredients().get(i).getName() + ", ";
                    }
                }
                ingredientNamesList.setText(names);
            }
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
        if (activity != null) {
            Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag("Recipe Modal");
            if (fragment != null) {
                RecipeModalFragment recipeModalFragment = (RecipeModalFragment) fragment;
                recipeModalFragment.recipe = recipe;
                recipeModalFragment.updateRecipe();
            }
        }

        // Remove the fragment and start a new one with the changed recipe



    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mealType = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
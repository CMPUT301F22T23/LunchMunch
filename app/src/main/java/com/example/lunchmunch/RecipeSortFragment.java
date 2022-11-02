package com.example.lunchmunch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

public class RecipeSortFragment extends DialogFragment {
    private Spinner spinner;
    private Spinner spinnerDirection;
    private String sortType = "";
    private String sortDirection = "";
    private Button sortButton;

    public RecipeSortFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.recipe_sort, container, false);
        spinner = view.findViewById(R.id.sortRecipeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.recipes_sort, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDirection = view.findViewById(R.id.sortRecipeDir);
        ArrayAdapter<CharSequence> adapterDirection = ArrayAdapter.createFromResource(getContext(),
                R.array.recipes_sort_direction, android.R.layout.simple_spinner_item);
        adapterDirection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinnerDirection.setAdapter(adapterDirection);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sortType = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        sortType = "";
                    }
                }
        );

        spinnerDirection.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sortDirection = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        sortDirection = "";
                    }
                }
        );
        sortButton = view.findViewById(R.id.sortRecipesButton);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get current activity
                RecipeActivity activity = (RecipeActivity) getActivity();
                // call the method in the activity
                activity.sortRecipes(sortType, sortDirection);
                dismiss();
            }
        });
        return view;
    }



}

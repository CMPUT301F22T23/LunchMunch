package com.example.lunchmunch;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
/**
 * Fragment for adding/editing RecipeIngredient functionality
 */
public class RecipeIngredientFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private View view;
    DatePickerDialog datePickerDialog;
    private RecipeFragment.OnFragmentInteractionListener listener;
    ArrayAdapter<CharSequence> adapter;
    Spinner ingredientSpinner;
    Spinner locationSpinner;
    EditText ingredientExpiry;
    EditText ingredientName;
    EditText ingredientAmount;
    EditText ingredientPrice;
    EditText ingredientDescription;

    private String name;
    private String description;
    private IngredientCategory category;
    private Location location;
    private Integer price;
    private Integer amount;
    Date expirationDate;
    private Recipe recipe;
    private FoodItemAdapter foodItemAdapter;




    public RecipeIngredientFragment(Recipe recipe, FoodItemAdapter recipeIngredientsAdapter) {
        this.recipe = recipe;
        this.foodItemAdapter = recipeIngredientsAdapter;
    }
    /**
     * Sets information on existing recipe for editing
     * @param savedInstanceState The ingredient we are setting information on
     */

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = getLayoutInflater().inflate(R.layout.ingredient_item_fragment, null);

        // ingredient category spinner
        ingredientSpinner = (Spinner) view.findViewById(R.id.ingredient_category);
        adapter = (ArrayAdapter<CharSequence>) ArrayAdapter.createFromResource(getContext(),
                R.array.ingredient_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredientSpinner.setAdapter(adapter);
        ingredientSpinner.setOnItemSelectedListener(this);

        // ingredient location spinner
        locationSpinner = (Spinner) view.findViewById(R.id.ingredient_location);
        adapter = (ArrayAdapter<CharSequence>) ArrayAdapter.createFromResource(getContext(),
                R.array.ingredient_locations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);
        locationSpinner.setOnItemSelectedListener(this);

        // date picker dialog
        initDatePicker();
        ingredientExpiry = view.findViewById(R.id.ingredient_expiry);
        ingredientExpiry.setOnClickListener(e -> { datePickerDialog.show(); });

        // user inputted name
        ingredientName = view.findViewById(R.id.ingredient_name);

        // user inputted description
        ingredientDescription = view.findViewById(R.id.ingredient_description);

        // user inputted price
        ingredientPrice = view.findViewById(R.id.ingredient_price);

        // user inputted amount
        ingredientAmount = view.findViewById(R.id.ingredient_amount);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AddIngredientCustomAlertDialog);
        AlertDialog alert = builder
                .setView(view)
                .setTitle(getContext().getResources().getString(R.string.add_edit_ingredient_title))

                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        assert getArguments() != null;
                        Integer position = getArguments().getInt("position");
                        Integer ingPosition = getArguments().getInt("currentIngredientPosition", -1);
                        if (ingPosition != -1) {
                            recipe.getIngredients().remove(ingPosition.intValue());
                            System.out.println(recipe.getIngredients().size());
                            System.out.println(ingPosition);
                            listener.onOkPressed(recipe, false, position);
                            foodItemAdapter.notifyDataSetChanged();


                        }

                    }



                })

                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getUserInput();
                        Ingredient ingredient = new Ingredient(name, description, expirationDate, location, price, amount, category);
                        assert getArguments() != null;
                        Integer ingPosition = getArguments().getInt("currentIngredientPosition", -1);

                        if (ingPosition == -1) {
                            recipe.getIngredients().add(ingredient);
                        } else {
                            recipe.getIngredients().set(ingPosition, ingredient);
                        }
                        int position = getArguments().getInt("position");
                        listener.onOkPressed(recipe, false, position);
                        foodItemAdapter.notifyDataSetChanged();
                    }
                }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        alert.setOnShowListener(a -> {
            Button positive = alert.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setTextColor(Color.BLACK);
            Button negative = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setTextColor(Color.BLACK);
            Button neutral = alert.getButton(AlertDialog.BUTTON_NEUTRAL);
            neutral.setTextColor(Color.BLACK);
        });

        alert.show();

        Bundle bundle = this.getArguments();
        System.out.println("bundle: " + bundle);
        if (bundle.getInt("currentIngredientPosition", -1) != -1) {
            Ingredient currentIngredient = recipe.getIngredients().get(bundle.getInt("currentIngredientPosition"));
            setCurrentIngredient(currentIngredient);
        }

        return alert;
    }


    public void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                // get use inputted date
                expirationDate = new GregorianCalendar(year, month, day).getTime();
                String fullDate = expirationDate.toString();
                String [] displayDate = fullDate.split(" ");

                ingredientExpiry.setText(displayDate[1] + " " +displayDate[2] + " " +  displayDate[5]);

            }
        };

        Calendar cal = Calendar.getInstance();

        if (expirationDate != null) { cal.setTime(expirationDate); }
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);

    }

    private void setCurrentIngredient(Ingredient currentIngredient) {
        // ingredient category spinner
        String[] categoryValues = getResources().getStringArray(R.array.ingredient_categories);
        String currentCategory = currentIngredient.getCategory().toString().toLowerCase();
        for (int i = 0; i < categoryValues.length; i++) {
            if(currentCategory.equalsIgnoreCase(categoryValues[i])) {
                ingredientSpinner.setSelection(i);

            }
        }
        // ingredient location spinner
        //locationSpinner.set
        String[] locationValues = getResources().getStringArray(R.array.ingredient_locations);
        String currentLocation = currentIngredient.getLocation().toString().toLowerCase();
        for (int i = 0; i < locationValues.length; i++) {
            if(currentLocation.equalsIgnoreCase(locationValues[i])) {
                locationSpinner.setSelection(i);

            }
        }

        // date picker dialog
        expirationDate = currentIngredient.getBestBefore();
        String fullDate = expirationDate.toString();
        String [] displayDate = fullDate.split(" ");
        ingredientExpiry.setText(displayDate[1] + " " + displayDate[2] + " " + displayDate[5]);

        // user inputted name
        ingredientName = view.findViewById(R.id.ingredient_name);
        ingredientName.setText(currentIngredient.getName());

        // user inputted description
        ingredientDescription = view.findViewById(R.id.ingredient_description);
        ingredientDescription.setText(currentIngredient.getDescription());

        // user inputted price
        ingredientPrice = view.findViewById(R.id.ingredient_price);
        ingredientPrice.setText(String.valueOf(currentIngredient.getCost()));

        // user inputted amount
        ingredientAmount = view.findViewById(R.id.ingredient_amount);
        ingredientAmount.setText(String.valueOf(currentIngredient.getCount()));

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = adapterView.getId();

        String string = adapterView.getItemAtPosition(i).toString().toUpperCase();
        // get user inputted category from spinner
        if (id == R.id.ingredient_category) {
            category = IngredientCategory.valueOf(string);
            System.out.println(string);
            System.out.println(category);
        }

        // get user inputted location from spinner
        else if (id == R.id.ingredient_location) { location = Location.valueOf(string); }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    public void getUserInput() {
        // get user inputted name
        name = ingredientName.getText().toString();
        ingredientName.getText().clear();

        // get user inputted description
        description = ingredientDescription.getText().toString();
        ingredientDescription.getText().clear();

        // get user inputted price
        try {
            String priceInput = ingredientPrice.getText().toString();
            price = Integer.parseInt(priceInput);
        } catch (NumberFormatException e) {
            price = 0;
        }
        ingredientPrice.getText().clear();

        // get user inputted amount
        try {
            String amountInput = ingredientAmount.getText().toString();
            amount = Integer.parseInt(amountInput);
        } catch (NumberFormatException e) {
            amount = 0;
        }
        ingredientAmount.getText().clear();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        System.out.println(context);
        listener = (RecipeFragment.OnFragmentInteractionListener) context;

    }
}


package com.example.lunchmunch;
import android.app.DatePickerDialog;
import android.os.Parcel;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Fragment for adding/editing ingredient functionality
 */
public class IngredientItemFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    // New alert dialog opens to enter information about new/existing Ingredient
    View view;
    DatePickerDialog datePickerDialog;

    ArrayAdapter<CharSequence> adapter;
    Spinner ingredientSpinner;
    Spinner locationSpinner;
    Button ingredientExpiryButton;
    EditText ingredientName;
    EditText ingredientAmount;
    EditText ingredientPrice;
    EditText ingredientDescription;

    // TEMPORARY defaults
    private String name;
    private String description;
    private IngredientCategory category;
    private Location location;
    private Integer price;
    private Integer amount;
    private Date expirationDate;

    private OnFragmentInteractionListener listener;
    // Interaction with fragment
    public interface OnFragmentInteractionListener {
        void onOkPressed(Ingredient ingredient, int position);
        void deleteIngredient();
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + "must implement listener");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.ingredient_item_fragment, null);

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
        ingredientExpiryButton = view.findViewById(R.id.ingredient_expiry);
        ingredientExpiryButton.setOnClickListener(e -> { datePickerDialog.show(); });

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
                .setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("DEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.deleteIngredient();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getUserInput();
                        Ingredient ingredient = new Ingredient(name, description, expirationDate, location, price, amount, category);
                        // Check if ingredient is new
                        if (getArguments() != null) {
                            Integer position = getArguments().getInt("currentIngredientPosition");
                            if (position != null) {
                                listener.onOkPressed(ingredient, position);
                            }
                            else {
                                listener.onOkPressed(ingredient, -1);
                            }

                        }
                        else {
                            listener.onOkPressed(ingredient, -1);
                        }
                    }
                }).create();
        alert.setOnShowListener(a -> {
                Button positive = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setBackgroundResource(R.drawable.ic_save);
                Button negative = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setBackgroundResource(R.drawable.ic_delete);
                Button neutral = alert.getButton(AlertDialog.BUTTON_NEUTRAL);
                neutral.setBackgroundResource(R.drawable.cancel);
        });
        alert.show();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Ingredient currentIngredient = bundle.getParcelable("currentIngredient");
            setCurrentIngredient(currentIngredient);
        }

        return alert;
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
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /**
     * Sets information on existing ingredient for editing
     */
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


    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                // get use inputted date
                expirationDate = new GregorianCalendar(year, month, day).getTime();
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

    /**
     * Takes user input (excluding spinners)
     */
    private void getUserInput() {
        // get user inputted name
        name = ingredientName.getText().toString();
        ingredientName.getText().clear();

        // get user inputted description
        description = ingredientDescription.getText().toString();
        ingredientDescription.getText().clear();

        // get user inputted price
        String priceInput = ingredientPrice.getText().toString();
        price  = Integer.parseInt(priceInput);
        ingredientPrice.getText().clear();

        // get user inputted amount
        String amountInput = ingredientAmount.getText().toString();
        amount = Integer.parseInt(amountInput);
        ingredientAmount.getText().clear();
    }
}

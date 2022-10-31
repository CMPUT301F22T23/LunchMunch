package com.example.lunchmunch;
import android.app.DatePickerDialog;
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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    private String name = "default";
    private String description = "default";
    private IngredientCategory category = IngredientCategory.FRUIT;
    private Location location = Location.FRIDGE;
    private Integer price = 1;
    private Integer amount = 1;
    private Date expirationDate = new Date();

    private OnFragmentInteractionListener listener;
    // Interaction with fragment
    public interface OnFragmentInteractionListener {
        void onOkPressed(String name, String description, Date bestBefore, Location location, Integer count, Integer cost, IngredientCategory category);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AddIngredientCustomAlertDialog);
        AlertDialog alert = builder
                .setView(view)
                .setTitle(getContext().getResources().getString(R.string.add_edit_ingredient_title))
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
                        listener.onOkPressed(name, description, expirationDate, location, amount, price, category);
                    }
                }).create();
        alert.setOnShowListener(a -> {
                Button positive = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setBackgroundResource(R.drawable.ic_save);
                Button negative = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setBackgroundResource(R.drawable.ic_delete);
        });
        alert.show();
        return alert;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = view.getId();

        String string = adapterView.getItemAtPosition(i).toString();

        // get user inputted category from spinner
        if (id == R.id.ingredient_category) { category = IngredientCategory.valueOf(string); }

        // get user inputted location from spinner
        else if (id == R.id.ingredient_location) { location = Location.valueOf(string); }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);

    }

    private void getUserInput() {
        // get user inputted name
        ingredientName = view.findViewById(R.id.ingredient_name);
        name = ingredientName.getText().toString();
        ingredientName.getText().clear();

        // get user inputted description
        ingredientDescription = view.findViewById(R.id.ingredient_description);
        description = ingredientDescription.getText().toString();
        ingredientDescription.getText().clear();

        // get user inputted price
        ingredientPrice = view.findViewById(R.id.ingredient_price);
        String priceInput = ingredientPrice.getText().toString();
        price  = Integer.parseInt(priceInput);
        ingredientPrice.getText().clear();

        // get user inputted amount
        ingredientAmount = view.findViewById(R.id.ingredient_amount);
        String amountInput = ingredientAmount.getText().toString();
        amount = Integer.parseInt(amountInput);
        ingredientAmount.getText().clear();
    }
}

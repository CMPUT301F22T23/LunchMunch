package com.example.lunchmunch;

import static com.example.lunchmunch.IngredientCategory.FRUIT;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddShopIngrFragment extends DialogFragment {

    //View view;
    DatePickerDialog datePickerDialog;

    ArrayAdapter<CharSequence> ingrAdapter, locAdapter;
    Spinner ingredientSpinner;
    Spinner locationSpinner;
    EditText ingredientExpiry;
    EditText ingredientName;
    EditText ingredientAmount;
    EditText ingredientPrice;
    EditText ingredientDescription;

    TextView errMsgTxt;


    // TEMPORARY defaults
    private String name;
    private String description;
    // not using Input for cat and loc as you cant leave blank
    private IngredientCategory category;
    private Location location;
    private String priceInput;
    private String amountInput;
    private Float price;
    private Float amount;
    private String inputExpirationDate;
    private Date expirationDate;

    private OnFragmentInteractionListener listener;

    static AddShopIngrFragment newInstance(Ingredient ingredient, Integer ingrIdx) {
        Bundle args = new Bundle();
        args.putSerializable("ingredient", (Serializable) ingredient);
        args.putInt("ingrIdx", ingrIdx);

        AddShopIngrFragment fragment = new AddShopIngrFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public interface OnFragmentInteractionListener {
        void onOkPressed(Ingredient ingredient, Integer newIngrIdx);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.ingredient_item_fragment, null);
        ingredientSpinner = view.findViewById(R.id.ingredient_category);
        ingrAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.ingredient_categories, android.R.layout.simple_spinner_item);
        ingrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredientSpinner.setAdapter(ingrAdapter);

        locationSpinner = view.findViewById(R.id.ingredient_location);
        locAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.ingredient_locations, android.R.layout.simple_spinner_item);
        locAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locAdapter);
        //locationSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

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


        //textview for any possible error msgs
        errMsgTxt = view.findViewById(R.id.errMsgTxt);


        if (getArguments() != null) {
            Ingredient ingredient = (Ingredient) getArguments().get("ingredient");
            ingredientSpinner.setSelection(ingrAdapter.getPosition(String.valueOf(ingredient.getCategory())));
            locationSpinner.setSelection(locAdapter.getPosition(String.valueOf(ingredient.getLocation())));
            ingredientName.setText(ingredient.getName());
            ingredientDescription.setText(ingredient.getDescription());
            ingredientPrice.setText(ingredient.getCost().toString());
            ingredientAmount.setText(ingredient.getCount().toString());

            // disable and grey out inputs: category selector, name
            // the rest the user can modify

            ingredientSpinner.setEnabled(false);
            ingredientSpinner.setBackgroundColor(Color.DKGRAY);
            ingredientName.setEnabled(false);
            ingredientName.setBackgroundColor(Color.DKGRAY);

        }


        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Add Ingredient to Ingredient Storage")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button addBtn = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);

                addBtn.setOnClickListener(view -> {
                    name = ingredientName.getText().toString();

                    // get user inputted exp date
                    inputExpirationDate = ingredientExpiry.getText().toString();
                    // get user inputted price
                    priceInput = ingredientPrice.getText().toString();
                    // get user inputted amount
                    amountInput = ingredientAmount.getText().toString();
                    // get user inputted desc
                    description = ingredientDescription.getText().toString();
                    // get user selected location
                    location = Location.valueOf(locationSpinner.getSelectedItem().toString().toUpperCase());
                    // get user selected category
                    category = IngredientCategory.valueOf(ingredientSpinner.getSelectedItem().toString().toUpperCase());

                    String errMsg = validateIngrInputs(inputExpirationDate, priceInput, amountInput);

                    System.out.println("inEXPDATE: "+ inputExpirationDate);

                    if (errMsg.equals("")) {
                        Ingredient newIngredient = new Ingredient(name, description, expirationDate, location, amount, price, category);
                        Integer newIngrIdx = getArguments().getInt("ingrIdx");

                        listener.onOkPressed(newIngredient, newIngrIdx);

                        //can clear inputs now
                        clearUserInput();
                        // close/dismiss popup only when valid inputs
                        dialog.dismiss();
                    } else { // errMsg was not empty meaning one of the inputs were invalid
                        errMsgTxt.setText(errMsg);
                    }

                });
            }
        });
        dialog.show();
        return dialog;
    }

    private void clearUserInput() {
        //reset inputs (incase affects fragment in ingredient activity)
        ingredientSpinner.setEnabled(true);
        ingredientSpinner.setBackgroundColor(Color.WHITE);
        ingredientName.setEnabled(true);
        ingredientName.setBackgroundColor(Color.WHITE);

        ingredientName.getText().clear();
        ingredientDescription.getText().clear();
        ingredientPrice.getText().clear();
        ingredientAmount.getText().clear();
        errMsgTxt.setText("");

    }

    private String validateIngrInputs(String inputExpirationDate, String priceInput, String amountInput) {
        String errMsg = "";

        if (inputExpirationDate.equals("")) {
            errMsg += "Please select an expiry date, ";
        }

        if (priceInput.equals("")) {
            errMsg += "Enter a number for price, ";
        } else { // otherwise we have a valid int and we can parse
            price = Float.parseFloat(priceInput);
            //xml restricts entering neg number
            if (price == 0) {
                errMsg += "Enter a positive number for price, ";
            }
            // otherwise we have converted price to valid pos Integer and can proceed
        }

        // also verify that user enters the minimum amount for the ingr (needed for shopping list)
        if (amountInput.equals("")) {
            errMsg += "Enter a number for amount, ";
        } else { // otherwise we have a valid int and we can parse
            amount = Float.parseFloat(amountInput);
            //xml restricts entering neg number
            if (amount == 0) {
                errMsg += "Enter a positive number for amount, ";
            }
            // otherwise we have converted price to valid pos Integer and can proceed
        }

        // if errMsg exists remove last 2 char from total err msg (last 2 will be ", ")
        if (!errMsg.equals("")) {
            errMsg = errMsg.substring(0, errMsg.length() - 2);
        }

        return errMsg;
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                // get user inputted date and set the textview to display selected expiry date
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

        int style = android.app.AlertDialog.THEME_HOLO_LIGHT;


        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }
}

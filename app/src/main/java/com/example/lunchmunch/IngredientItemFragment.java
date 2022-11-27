package com.example.lunchmunch;

import android.app.AlertDialog;
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
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    private Date expirationDate;

    private OnFragmentInteractionListener listener;
    // Interaction with fragment
    public interface OnFragmentInteractionListener {
        void onOkPressed(Ingredient ingredient, int position);
        void deleteIngredient();
    }
    /**
     * implement OnFragmentInteractionListener
     * @param context context
     * @throws RuntimeException If we couldn't implement
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context + "must implement listener");
        }
    }



    /**
     * Returns an alert object that will is used to take user input about an ingredient
     * @param savedInstanceState In case we need to restore ourselves to a previous state, can be NULL
     * @return                   alert dialog
     */

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.ingredient_item_fragment, null);

        // ingredient category spinner
        ingredientSpinner = view.findViewById(R.id.ingredient_category);
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.ingredient_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredientSpinner.setAdapter(adapter);
        ingredientSpinner.setOnItemSelectedListener(this);

        // ingredient location spinner
        locationSpinner = view.findViewById(R.id.ingredient_location);
        adapter = ArrayAdapter.createFromResource(getContext(),
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


        //textview for any possible error msgs
        errMsgTxt = view.findViewById(R.id.errMsgTxt);

        final AlertDialog alert = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Add/Edit Ingredient")
                .setNegativeButton("Delete", null)
                .setPositiveButton("Save", null)
                .setNeutralButton("Cancel", null)
                .create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button delBtn = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
                delBtn.setTextColor(Color.BLACK);
                //saveBtn.setBackgroundResource(R.drawable.ic_save);
                delBtn.setOnClickListener(view -> {
                    System.out.println("DELETE");
                    //can clear inputs now
                    clearUserInput();
                    // close/dismiss popup
                    alert.dismiss();
                    // delete ingr from app and db
                    listener.deleteIngredient();
                });

                // using inside onShow allows us to close the dialog when we want (even if user pressed positive button (in this case would have invalid inputs))
                Button saveBtn = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                saveBtn.setTextColor(Color.BLACK);
                //saveBtn.setBackgroundResource(R.drawable.ic_save);
                saveBtn.setOnClickListener(view -> {
                    getUserInput();
                    // only send inputs that are necessary and could be left blank or have an invalid input (desc can leave blank, loc & cat cant be left blank)
                    String errMsg = validateIngrInputs(name, expirationDate, priceInput, amountInput);
                    if (errMsg.equals("")) {
                        Ingredient ingredient = new Ingredient(name, description, expirationDate, location, price, amount, category);
                        // Check if ingredient is new
                        if (getArguments() != null) {
                            Integer position = getArguments().getInt("currentIngredientPosition");
                            if (position != null) {
                                listener.onOkPressed(ingredient, position);
                            } else {
                                listener.onOkPressed(ingredient, -1);
                            }

                        } else {
                            listener.onOkPressed(ingredient, -1);
                        }
                        //can clear inputs now
                        clearUserInput();
                        // close/dismiss popup only when valid inputs
                        alert.dismiss();
                    } else { // errMsg was not empty meaning one of the inputs were invalid
                        errMsgTxt.setText(errMsg);
                    }
                });

                Button cancelBtn = alert.getButton(AlertDialog.BUTTON_NEUTRAL);
                cancelBtn.setTextColor(Color.BLACK);
                cancelBtn.setOnClickListener(view1 -> {
                    alert.dismiss();
                });

            }
        });


        /*
        alert.setOnShowListener(a -> {
                Button positive = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setBackgroundResource(R.drawable.ic_save);
                Button negative = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setBackgroundResource(R.drawable.ic_delete);
                Button neutral = alert.getButton(AlertDialog.BUTTON_NEUTRAL);
                neutral.setBackgroundResource(R.drawable.cancel);
        });

         */

        alert.show();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Ingredient currentIngredient = bundle.getParcelable("currentIngredient");
            setCurrentIngredient(currentIngredient);
        }

        return alert;
    }




    private String validateIngrInputs(String nameInput, Date expirationDate, String priceInput, String amountInput) {
        // the only 3 inputs that actually have any constraints
        String errMsg = "";

        // not sure if Date fully constraints to valid dates (ex Feb 30th)
        //TODO: test if possible to select Feb 30th in date input (if possible then add constraint here)

        if (nameInput.equals("")) {
            errMsg += "Enter a name, ";
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

        // could add future constraints here
        return errMsg;
    }
    /**
     * When item is selected, set category or set location
     * @param adapterView  The viewgroup we have our items stored in
     * @param view         Not currently being used
     * @param i            Used to get item at position i
     * @param l            Not currently being used
     */
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
     * @param currentIngredient The ingredient we are setting information on
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

        int style = AlertDialog.THEME_HOLO_LIGHT;


        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

    /**
     * Takes user input (excluding spinners)
     */
    private void getUserInput() {
        // get user inputted name
        name = ingredientName.getText().toString();

        // get user inputted description
        description = ingredientDescription.getText().toString();


        // get user inputted price
        priceInput = ingredientPrice.getText().toString();
        // will cause error if user doesnt enter int (possible if they dont enter anything)
        //price  = Integer.parseInt(priceInput);


        // get user inputted amount
        amountInput = ingredientAmount.getText().toString();
        //amount = Integer.parseInt(amountInput);


    }

    private void clearUserInput() {
        ingredientName.getText().clear();
        ingredientDescription.getText().clear();
        ingredientPrice.getText().clear();
        ingredientAmount.getText().clear();
        errMsgTxt.setText("");
    }
}

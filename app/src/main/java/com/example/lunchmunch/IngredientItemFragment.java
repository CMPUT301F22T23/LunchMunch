package com.example.lunchmunch;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class IngredientItemFragment extends DialogFragment {
    // New alert dialog opens to enter information about new/existing Ingredient
    private EditText ingredient_desc;
    private EditText ingredient_price;


    private OnFragmentInteractionListener listener;
    // Interaction with fragment
    public interface OnFragmentInteractionListener {
        void onOkPressed();
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.ingredient_item_fragment, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AddIngredientCustomAlertDialog);
        return builder
                .setView(view)
                .setTitle(getContext().getResources().getString(R.string.add_edit_ingredient_title))
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onOkPressed();
                    }
                }).create();
    }
}

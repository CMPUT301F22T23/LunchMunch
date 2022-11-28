package com.example.lunchmunch;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import android.content.Context;

import org.junit.Test;

public class AddShopIngrFragmentTest {

    /**
     * Should throw an exception when the context is not an instance of
     * onfragmentinteractionlistener
     */
    @Test
    public void
    onAttachWhenContextIsNotAnInstanceOfOnFragmentInteractionListenerThenThrowException() {
        AddShopIngrFragment addShopIngrFragment = mock(AddShopIngrFragment.class);
        try {
            Context context = mock(Context.class);
            addShopIngrFragment.onAttach(context);
        } catch (RuntimeException e) {
            assertEquals("Context must implement OnFragmentInteractionListener", e.getMessage());
        }
    }
}

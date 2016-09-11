package com.cybermoosemoosemedia.honeydo.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.cybermoosemoosemedia.honeydo.activity.HoneyDoListActivity;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year;
        int month;
        int day;

        //get previously used date if there is one
        long dueDate;
        Bundle due = this.getArguments();

        if (due != null) {
            dueDate = due.getLong("dueDate");

            if (dueDate > 0) {
                c.setTimeInMillis(dueDate);
            }
        }

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // Activity needs to implement this interface
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getActivity();
        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), (HoneyDoListActivity)getActivity(), year, month, day);

    }

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }
}

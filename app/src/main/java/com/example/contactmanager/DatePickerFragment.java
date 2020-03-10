package com.example.contactmanager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public interface OnDateSelected {
        void sendData(String data);
    }

    public OnDateSelected mOnDateSelected;

    private static final String TAG = "DatePickerFragment";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        if (month < 10 && day < 10) {
            String date = "0" + (month + 1) + "/" + "0" + day + "/" + year;
            mOnDateSelected.sendData(date);
        }
        else if (month < 10) {
            String date = "0" + (month + 1) + "/" + day + "/" + year;
            mOnDateSelected.sendData(date);
        }
        else if (day < 10) {
            String date = (month + 1) + "/" + "0" + day + "/" + year;
            mOnDateSelected.sendData(date);
        }
        getDialog().dismiss();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnDateSelected = (OnDateSelected) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException" + e.getMessage() );
        }
    }
}
package com.example.contactmanager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public interface OnDateSelected {
        void sendDate(String date, int tvKey);
    }

    private OnDateSelected mOnDateSelected;
    private int mTvKey;
    public int datePickerStyle;


    private static final String TAG = "DatePickerFragment";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mTvKey = bundle.getInt("tvKey");
            datePickerStyle = bundle.getInt("datePickerStyle", -1);
        }
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if (datePickerStyle != -1) {
            return new DatePickerDialog(getActivity(), datePickerStyle, this, year, month, day);
        } else {
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        if (month < 10 && day < 10) {
            String date = "0" + (month + 1) + "/" + "0" + day + "/" + year;
            mOnDateSelected.sendDate(date, mTvKey);
        }
        else if (month < 10) {
            String date = "0" + (month + 1) + "/" + day + "/" + year;
            mOnDateSelected.sendDate(date, mTvKey);
        }
        else if (day < 10) {
            String date = (month + 1) + "/" + "0" + day + "/" + year;
            mOnDateSelected.sendDate(date, mTvKey);
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
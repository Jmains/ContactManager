/* Contact Manager Program
*
*  A simple android app that mimics the functionality of a contact manager
*  in either Android or iOS phones. Users can view their list of contacts
*  sorted in alphabetical order by last name. They may also add, edit,
*  and remove a contact from the contact list when a contact is tapped on.
*
*  When adding a contact, users must add the contacts first name, last name,
*  phone number, date of birth, and date of first contact.
*
*  Written by Supachai Main for CS4301.002, ContactManager Part.3 , starting March 9th, 2020.
        NetID: sxm163830 */

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

    // Interface to send data to parent activity
    public interface OnDateSelected {
        void sendDate(String date, int tvKey);
    }

    // Member variables
    private OnDateSelected mOnDateSelected;
    private int mTvKey;

    //private static final String TAG = "DatePickerFragment";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int datePickerStyle = -1;
        // Grab date picker style from parent activity.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mTvKey = bundle.getInt("tvKey");    // 1 == spinner, 2 == calendar
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
        // Format date with slashes (I'm sure there's an easier way to do this).
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
            //Log.e(TAG, "onAttach: ClassCastException" + e.getMessage() );
            e.printStackTrace();
        }
    }
}
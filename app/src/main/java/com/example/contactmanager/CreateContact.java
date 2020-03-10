package com.example.contactmanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.io.File;

public class CreateContact extends AppCompatActivity implements DatePickerFragment.OnDateSelected {

    private static final String TAG = "CreateContactActivity";

    static final int ADD_CONTACT_REQUEST = 1; // The request code.
    static final int DELETE_CONTACT_REQUEST = 2; // The request code.
    static final int EDIT_CONTACT_REQUEST = 3; // The request code.
    static final int ADD_MODE = 1; //
    static final int EDIT_MODE = 2; //

    // Widgets
    Button mSaveBtn;
    Button mDeleteBtn;
    Button mAddBtn;
    Button mCancelBtn;
    TextView mDobTv;
    TextView mDofcTv;
    EditText mFirstNameTe;
    EditText mLastNameTe;
    EditText mPhoneNumTe;

    // Vars
    int mMode = -1; // 1 = add contact, 2 = edit contact
    int mContactId = -1;

    ContactManager mContactManager;
    Contact mContact;
    File filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDobTv = findViewById(R.id.id_show_dob_tv);
        mDofcTv = findViewById(R.id.id_show_dofc_tv);
        mFirstNameTe = findViewById(R.id.id_first_name_tedit);
        mLastNameTe = findViewById(R.id.id_last_name_tedit);
        mPhoneNumTe = findViewById(R.id.id_phone_num_tedit);
        mSaveBtn = findViewById(R.id.id_save_button);
        mAddBtn = findViewById(R.id.id_add_button);
        mCancelBtn = findViewById(R.id.id_cancel_button);
        mDeleteBtn = findViewById(R.id.id_delete_button);

        mDeleteBtn.setVisibility(View.INVISIBLE);
        mSaveBtn.setVisibility(View.INVISIBLE);
        mCancelBtn.setVisibility(View.INVISIBLE);
        mAddBtn.setVisibility(View.INVISIBLE);

        filePath = new File(getFilesDir(), "contacts.txt");
        mContactManager = new ContactManager(filePath);

        Intent intent = getIntent();
        if (intent != null) {

            mMode = intent.getIntExtra("mode", -1);

            if (mMode == ADD_MODE) {

                mAddBtn.setVisibility(View.VISIBLE);
                mCancelBtn.setVisibility(View.VISIBLE);


            } else if (mMode == EDIT_MODE) {

                mSaveBtn.setVisibility(View.VISIBLE);
                mDeleteBtn.setVisibility(View.VISIBLE);
                mContactId = intent.getIntExtra("contactId", -1);
                mContact = mContactManager.findContactById(mContactId);
                setFormFields(mContact);
            } else {
                Log.d(TAG, "onCreate: No mode was received");
            }
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onAdd(View v) {

//        if (!mFirstNameTe.getText().toString().equals("") &&
//            !mLastNameTe.getText().toString().equals("") &&
//            !mPhoneNumTe.getText().toString().equals("")) {
//            mSaveBtn.setClickable(true);
//        }

        filePath = new File(getFilesDir(), "contacts.txt");

        String firstName = mFirstNameTe.getText().toString().trim();
        String lastName = mLastNameTe.getText().toString().trim();
        String phoneNum = mPhoneNumTe.getText().toString().trim();
        String dob = mDobTv.getText().toString().replace("/", "");
        String dofc = mDofcTv.getText().toString().replace("/", "");

        // Write contact to database
        mContactManager.addContactToDb(firstName, lastName, phoneNum, dob, dofc);

        // Return to main activity with request code
        Intent intent = new Intent();
        setResult(ADD_CONTACT_REQUEST, intent);
        finish();
    }

    public void onEdit(View v) {

        filePath = new File(getFilesDir(), "contacts.txt");

        String firstName = mFirstNameTe.getText().toString().trim();
        String lastName = mLastNameTe.getText().toString().trim();
        String phoneNum = mPhoneNumTe.getText().toString().trim();
        String dob = mDobTv.getText().toString().replace("/", "");
        String dofc = mDofcTv.getText().toString().replace("/", "");

        // Write contact to database
        mContactManager.editContactInDb(mContactId, firstName, lastName, phoneNum, dob, dofc);

        // Return to main activity with request code
        Intent intent = new Intent();
        setResult(EDIT_CONTACT_REQUEST, intent);
        finish();
    }

    public void onDelete(View v) {
        filePath = new File(getFilesDir(), "contacts.txt");
        mContactManager.deleteContactFromDb(mContactId);

        Intent intent = new Intent();
        setResult(DELETE_CONTACT_REQUEST, intent);
        finish();
    }

    @Override
    public void sendData(String data) {
        Log.d(TAG, "sendData: got the input");
        mDobTv.setText(data);
        mDofcTv.setText(data);
    }

    private void setFormFields(Contact contact) {
        mFirstNameTe.setText(contact.getFirstName());
        mLastNameTe.setText(contact.getLastName());
        mPhoneNumTe.setText(contact.getPhoneNum());
        mDobTv.setText(contact.getDateOfBirth());
        mDofcTv.setText(contact.getDateOfFirstContact());
    }

}

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

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ContactDetailsActivity extends AppCompatActivity implements DatePickerFragment.OnDateSelected {

    //private static final String TAG = "CreateContactActivity";

    // Request codes
    static final int ADD_CONTACT_REQUEST = 1;
    static final int DELETE_CONTACT_REQUEST = 2;
    static final int EDIT_CONTACT_REQUEST = 3;
    static final int CANCEL_CONTACT_REQUEST = 4;

    // Modes
    static final int ADD_MODE = 1;
    static final int EDIT_MODE = 2;

    // Whether to show date picker as
    // calendar view or spinner view.
    static final int DOB_TV = 1;    // spinner calendar
    static final int DOFC_TV = 2;   // calendar

    // Widgets
    Button mSaveBtn;
    Button mDeleteBtn;
    Button mAddBtn;
    Button mCancelBtn;
    TextView mDobTv;
    TextView mDofcTv;
    EditText mFirstNameEt;
    EditText mLastNameEt;
    EditText mPhoneNumEt;
    EditText mAddress1Et;
    EditText mAddress2Et;
    EditText mCityEt;
    EditText mStateEt;
    EditText mZipcodeEt;

    // Member variables
    int mContactId = -1;
    ContactManager mContactManager;
    DatabaseManager mDatabaseManager;
    Contact mContact;
    File mFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mDobTv = findViewById(R.id.id_show_dob_tv);
        mDofcTv = findViewById(R.id.id_show_dofc_tv);
        mFirstNameEt = findViewById(R.id.id_first_name_tedit);
        mLastNameEt = findViewById(R.id.id_last_name_tedit);
        mPhoneNumEt = findViewById(R.id.id_phone_num_tedit);
        mAddress1Et = findViewById(R.id.id_address_1_editText);
        mAddress2Et = findViewById(R.id.id_address_2_editText);
        mCityEt = findViewById(R.id.id_city_editText);
        mStateEt = findViewById(R.id.id_state_editText);
        mZipcodeEt = findViewById(R.id.id_zipcode_editText);
        mSaveBtn = findViewById(R.id.id_save_button);
        mAddBtn = findViewById(R.id.id_add_button);
        mCancelBtn = findViewById(R.id.id_cancel_button);
        mDeleteBtn = findViewById(R.id.id_delete_button);

        mDeleteBtn.setVisibility(View.INVISIBLE);
        mSaveBtn.setVisibility(View.INVISIBLE);
        mCancelBtn.setVisibility(View.INVISIBLE);
        mAddBtn.setVisibility(View.INVISIBLE);

        mSaveBtn.setEnabled(false);
        mAddBtn.setEnabled(false);

        mFirstNameEt.addTextChangedListener(contactFormTW);
        mLastNameEt.addTextChangedListener(contactFormTW);
        mPhoneNumEt.addTextChangedListener(contactFormTW);
        mDobTv.addTextChangedListener(contactFormTW);

        //Address Text Watchers
        mAddress1Et.addTextChangedListener(contactFormTW);
        mAddress2Et.addTextChangedListener(contactFormTW);
        mCityEt.addTextChangedListener(contactFormTW);
        mStateEt.addTextChangedListener(contactFormTW);
        mZipcodeEt.addTextChangedListener(contactFormTW);

        mFilePath = new File(getFilesDir(), "contacts.txt");
        mContactManager = new ContactManager(mFilePath);
        mDatabaseManager = new DatabaseManager(this.getApplicationContext());

        int mode; // 1 = add contact, 2 = edit contact
        Intent intent = getIntent();
        if (intent != null) {

            mode = intent.getIntExtra("mode", -1);

            if (mode == ADD_MODE) {

                mAddBtn.setVisibility(View.VISIBLE);
                mCancelBtn.setVisibility(View.VISIBLE);
                toolbar.setTitle("New Contact");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                mDofcTv.setText(sdf.format(date));

            } else if (mode == EDIT_MODE) {

                mSaveBtn.setVisibility(View.VISIBLE);
                mDeleteBtn.setVisibility(View.VISIBLE);
                toolbar.setTitle("Edit Contact");
                mContactId = intent.getIntExtra("contactId", -1);
                //mContact = mContactManager.findContactById(mContactId);
                mContact = mDatabaseManager.findContactById(mContactId);
                setFormFields(mContact);

            } else {
                //Log.d(TAG, "onCreate: No mode was received");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.id_map_address:
                String address1 = mDatabaseManager.findContactById(mContactId).getAddress1();
                String address2 = mDatabaseManager.findContactById(mContactId).getAddress2();
                String fullAddress = address1 + address2;
                if (!fullAddress.isEmpty()) {
                    Intent intent = new Intent(this, MapAddressActivity.class);
                    intent.putExtra("fullAddress", fullAddress);
                    startActivity(intent);
                }



                // Open new activity and do reverse geocoding
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    /*This onClick method passes the view that was clicked
    * indicated by a key and shows the respective date pickers
    * for DateOfBirthTv(Spinner) and DateOfFirstContactTv (Calendar).
    * It takes a view as parameter and returns void */
    public void showDatePickerDialog(View v) {

        Bundle bundle = new Bundle();
        switch (v.getId()) {

            case R.id.id_add_dob_tv:
                bundle.putInt("tvKey", DOB_TV); // 1 for DOB
                bundle.putInt("datePickerStyle", R.style.CustomDatePickerDialog);
                break;

            case R.id.id_add_dofc_tv:
                bundle.putInt("tvKey", DOFC_TV);  // 2 for DOFC
                break;
        }

        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.setArguments(bundle);
        dateFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /*This onClick method gets the inputs from the form typed by
     * the user and creates a new contact in the database
     * via the contactManager based on the information entered
     * then and returns to the mainActivity.
     * It takes a View as a parameter and returns void.
     */
    public void onAdd(View v) {

        String firstName = mFirstNameEt.getText().toString().trim();
        String lastName = mLastNameEt.getText().toString().trim();
        String phoneNum = mPhoneNumEt.getText().toString().trim();
        String dob = mDobTv.getText().toString().replace("/", "");
        String dofc = mDofcTv.getText().toString().replace("/", "");

        // Address Details
        String address1 = mAddress1Et.getText().toString().toUpperCase().trim();
        String address2 = mAddress2Et.getText().toString().toUpperCase().trim();
        String city = mCityEt.getText().toString().toUpperCase().trim();
        String state = mStateEt.getText().toString().toUpperCase().trim();
        String zipcode = mZipcodeEt.getText().toString().toUpperCase().trim();

        // Write contact to file database
        //mContactManager.addContactToDb(firstName, lastName, phoneNum, dob, dofc);
        // Write contact to SQLite DB

        //mDatabaseManager.addContact(firstName, lastName, phoneNum, dob, dofc);
        mDatabaseManager.addContact(firstName, lastName, phoneNum, dob, dofc,
                address1, address2, city, state, zipcode);

        // Return to main activity with request code
        Intent intent = new Intent();
        setResult(ADD_CONTACT_REQUEST, intent);
        finish();
    }

    /*This onClick method gets the inputs typed in the form by
    * the user and updates the specific contact in the database
    * via the contactManager based on the id and returns to the
    * mainActivity. It takes a view as a parameter and returns void.
    */
    public void onEdit(View v) {

        String firstName = mFirstNameEt.getText().toString().trim();
        String lastName = mLastNameEt.getText().toString().trim();
        String phoneNum = mPhoneNumEt.getText().toString().trim();
        String dob = mDobTv.getText().toString().replace("/", "");
        String dofc = mDofcTv.getText().toString().replace("/", "");

        // Address Details
        String address1 = mAddress1Et.getText().toString().toUpperCase().trim();
        String address2 = mAddress2Et.getText().toString().toUpperCase().trim();
        String city = mCityEt.getText().toString().toUpperCase().trim();
        String state = mStateEt.getText().toString().toUpperCase().trim();
        String zipcode = mZipcodeEt.getText().toString().toUpperCase().trim();

        // Write contact to database
        //mContactManager.editContactInDb(mContactId, firstName, lastName, phoneNum, dob, dofc);
        // Update contact in SQLite DB
        //mDatabaseManager.editContact(mContactId, firstName, lastName, phoneNum, dob, dofc);
        mDatabaseManager.editContact(mContactId, firstName, lastName, phoneNum, dob, dofc,
                address1, address2, city, state, zipcode);

        // Return to main activity with request code
        Intent intent = new Intent();
        intent.putExtra("contactId", mContactId);
        setResult(EDIT_CONTACT_REQUEST, intent);
        finish();
    }

    /*This onClick method simply returns
    * to the main activity when clicked.
    * It takes a view as a parameter and
    * returns void.*/
    public void onCancel(View v) {

        Intent intent = new Intent();
        setResult(CANCEL_CONTACT_REQUEST, intent);
        finish();
    }

    /*This onClick method grabs the id of the current contact
    * and passes the id to the contact manager to be deleted
    * in the database. It then returns to the MainActivity.
    * It takes a view as a parameter and returns void. */
    public void onDelete(View v) {

        //mContactManager.deleteContactFromDb(mContact.getId());
        mDatabaseManager.deleteContactById(mContact.getId());

        Intent intent = new Intent();
        setResult(DELETE_CONTACT_REQUEST, intent);
        finish();
    }

    /*This method retrieves the data from the DatePicker fragment
    and sets the appropriate dates based on the TextView that was
    selected. It takes a date string and TextViewKey integer as
    parameters and returns void. */
    @Override
    public void sendDate(String date, int tvKey) {

        //Log.d(TAG, "sendData: input received");
        switch (tvKey) {
            case DOB_TV:
                mDobTv.setText(date);
                break;
            case DOFC_TV:
                mDofcTv.setText(date);
                break;
            default:    // set both date TV's to the same date
                mDobTv.setText(date);
                mDofcTv.setText(date);
        }
    }

    /*This method takes the a contact object as a parameter
    * and fills in the current contacts data into the form fields.
    * It returns void.*/
    private void setFormFields(Contact contact) {

        mFirstNameEt.setText(contact.getFirstName());
        mLastNameEt.setText(contact.getLastName());
        mPhoneNumEt.setText(contact.getPhoneNum());
        // Set Address details
        mAddress1Et.setText(contact.getAddress1());
        mAddress2Et.setText(contact.getAddress2());
        mCityEt.setText(contact.getCity());
        mStateEt.setText(contact.getState());
        mZipcodeEt.setText(contact.getZipcode());

        // Add slashes to separate month day and year
        String dob = contact.getDateOfBirth();
        dob = dob.substring(0,2) + "/" + dob.substring(2,4) + "/" + dob.substring(4);
        String dofc = contact.getDateOfFirstContact();
        dofc = dofc.substring(0,2) + "/" + dofc.substring(2,4) + "/" + dofc.substring(4);
        mDobTv.setText(dob);
        mDofcTv.setText(dofc);
    }

    /*This textWatcher checks if the first and last name and phone number
    * fields are none empty. If they are empty then enable the add or save
    * button depending on if we're in edit or add mode in this activity. Else
    * disable the buttons. */
    private TextWatcher contactFormTW = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String fName = mFirstNameEt.getText().toString().trim();
            String lName = mLastNameEt.getText().toString().trim();
            String phoneNum = mPhoneNumEt.getText().toString().trim();
            String dob = mDobTv.getText().toString().trim();

            // Address details
            String address1 = mAddress1Et.getText().toString().trim();
            String address2 = mAddress2Et.getText().toString().trim();
            String state = mStateEt.getText().toString().trim();
            String city = mCityEt.getText().toString().trim();
            String zipcode = mZipcodeEt.getText().toString().trim();

            // Automatically hide soft keyboard when 10 numbers are entered
            // in the phone number field
//            if (phoneNum.length() == 10 || zipcode.length() == 5) {
//                hideSoftKeyBoard();
//            }

            if (!fName.isEmpty() && !lName.isEmpty() && !phoneNum.isEmpty() && !dob.isEmpty()
            && !address1.isEmpty() && !state.isEmpty() && !city.isEmpty() && !zipcode.isEmpty()) {
                mSaveBtn.setEnabled(true);
                mAddBtn.setEnabled(true);
            } else {
                mSaveBtn.setEnabled(false);
                mAddBtn.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    /*Simple method to hide soft keyboard returns void and
    * takes no parameters. */
    private void hideSoftKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}

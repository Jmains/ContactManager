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

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ContactListActivity extends AppCompatActivity {

    //private static final String TAG = "MainActivity";

    // onActivityForResult Requests
    static final int ADD_CONTACT_REQUEST = 1;
    static final int DELETE_CONTACT_REQUEST = 2;
    static final int EDIT_CONTACT_REQUEST = 3;
    static final int EDIT_MODE = 2;
    static final int ADD_MODE = 1;

    // Sensors
    private SensorManager mSm;
    private Sensor mAccelerometer;
    private PhoneShake mPhoneShake;

    // Member Variables
    File mFilePath;
    ArrayList<Contact> mContactList;
    ContactListAdapter mContactListAdapter;
    ListView mListView;
    ContactManager mContactManager;
    DatabaseManager mContactManager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initial Setup
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setTitle("Contacts");

        // Initialize Sensors
        mSm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mPhoneShake = new PhoneShake();

        if (mSm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {

            mAccelerometer = mSm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSm.registerListener(mPhoneShake, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

            mPhoneShake.setOnShakeListener(new PhoneShake.OnShakeListener() {
                @Override
                public void onShake() {
                    // Reverse sort the list of names
                    reverseSortByLastName(mContactList);
                }
            });

        } else {
            // Blehh no sensor bro...
        }

        // Grab Views
        mListView = findViewById(R.id.id_list_view);

        // Grab Database file path
        //mFilePath = new File(getFilesDir(), "contacts.txt");

        // Grab Database manager, grab contacts and sort
        //mContactManager = new ContactManager(mFilePath);
        // Grab SQLite DB manager
        mContactManager2 = new DatabaseManager(getApplicationContext());
        mContactList = mContactManager2.getContactList();
        sortByLastName(mContactList);

        //mContactList = mContactManager.getContactList();


        // Inflate the list view with first names of every contact and display it
        mContactListAdapter = new ContactListAdapter(this,
                android.R.layout.simple_list_item_1, mContactList);
        mListView.setAdapter(mContactListAdapter);


        // Click listener to add a contact. (+) Button on screen
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addContactIntent = new Intent(view.getContext(), ContactDetailsActivity.class);
                addContactIntent.putExtra("mode", ADD_MODE);
                startActivityForResult(addContactIntent, ADD_CONTACT_REQUEST, null);
            }
        });

        /* Click listener to edit a contact
         This listener passes the contact id to the new activity
         And passes the mode that the new activity should be in. */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d(TAG, "onItemClick: List item clicked");

                Intent editContactIntent = new Intent(getApplicationContext(), ContactDetailsActivity.class);
                Contact contact = (Contact) parent.getItemAtPosition(position);
                int contactId = contact.getId();
                editContactIntent.putExtra("contactId", contactId);
                editContactIntent.putExtra("mode", EDIT_MODE);
                startActivityForResult(editContactIntent, EDIT_CONTACT_REQUEST, null);
            }
        });
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
            case R.id.id_reinitialize:
                mContactManager2.reinitialize();
                if (mContactListAdapter != null) {
                    mContactListAdapter.clear();
                }
                mContactList.clear();
                mContactListAdapter.addAll(mContactList);
                mContactListAdapter.notifyDataSetChanged();
                return true;
            case R.id.id_import_contacts:
                importContacts();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSm.registerListener(mPhoneShake, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSm.unregisterListener(mPhoneShake);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Regardless of the request code update UI with new information
        // b/c for now all request codes require updating the UI.
        // Grab the contact list from the contactmanager , sort by
        // last name and inflate the list view.
        if (mContactListAdapter != null) {
            mContactListAdapter.clear();
        }
        //ContactManager cm = new ContactManager(mFilePath);
        DatabaseManager cm2 = new DatabaseManager(getApplicationContext());
        ArrayList<Contact> contacts = cm2.getContactList();

        if (contacts != null && contacts.size() != 0) {
            sortByLastName(contacts);
            mContactListAdapter.addAll(contacts);
            mContactListAdapter.notifyDataSetChanged();
        }
    }

    /* Simple method that takes the array list of contacts and
     sorts the contacts by last name. It takes the contacts
     ArrayList as arguments and returns void.*/
    public void sortByLastName(ArrayList<Contact> contacts) {
        if (contacts != null || contacts.size() != 0) {
            Collections.sort(contacts, new Comparator<Contact>() {
                @Override
                public int compare(Contact c1, Contact c2) {
                    int res = c1.getLastName().compareToIgnoreCase(c2.getLastName());
                    if (res != 0) {
                        return res;
                    }
                    return c1.getLastName().compareToIgnoreCase(c2.getLastName());
                }
            });
        }
    }

    /*
    * Simple method that takes the array list of contacts
    * and sorts the contacts by last name in reverse order.
    * It takes the contact ArrayList as arguments and returns
    * void.
    */
    public void reverseSortByLastName(ArrayList<Contact> contacts) {
        sortByLastName(contacts);
        Collections.reverse(contacts);
    }

    /*This method opens the contacts text file database,
    reads the information in the file and stores the contacts
    from the file in an arraylist, then it appends the current
    list of contacts with the list of contacts in the file. It then
    destroys the previous list of contacts in the SQLite DB and
    creates a new one with the new contacts int it. The view is
    also updated to show the newly updated contact list. This method
    takes nothing as parameters and returns void*/
    public void importContacts() {
        mFilePath = new File(getFilesDir(), "contacts.txt");
        mContactManager = new ContactManager(mFilePath);
        ArrayList<Contact> contactsFromFile = mContactManager.getContactList();
        ArrayList<Contact> oldContacts = mContactManager2.getContactList();
        // Append contacts from file with the new contacts
        contactsFromFile.addAll(oldContacts);

        // Upgrade to new db version
        mContactManager2.reinitialize();
        mContactList.clear();
        // Clear the list adapter
        if (mContactListAdapter != null) {
            mContactListAdapter.clear();
        }

        // Add the contacts to the database
        for (Contact contact: contactsFromFile) {
            mContactManager2.addContact(
                    contact.getFirstName(),
                    contact.getLastName(),
                    contact.getPhoneNum(),
                    contact.getDateOfBirth(),
                    contact.getDateOfFirstContact()
            );
        }
        mContactList = mContactManager2.getContactListFromDB();
        // Inflate the list view
        mContactListAdapter.addAll(mContactList);
        mContactListAdapter.notifyDataSetChanged();
    }
}

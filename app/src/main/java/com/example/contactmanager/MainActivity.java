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
*  Written by Supachai Main for CS4301.002, ContactManager Part.1 , starting March 9th, 2020.
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

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

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
            // Blehh
        }

        // Grab Views
        mListView = findViewById(R.id.id_list_view);

        // Grab Database file path
        mFilePath = new File(getFilesDir(), "contacts.txt");

        // Grab Database manager, grab contacts and sort
        mContactManager = new ContactManager(mFilePath);
        mContactList = mContactManager.getContactList();
        sortByLastName(mContactList);

        if (mContactList != null && mContactList.size() != 0) {
            // Inflate the list view with first names of every contact and display it
            mContactListAdapter = new ContactListAdapter(this,
                    android.R.layout.simple_list_item_1, mContactList);
            mListView.setAdapter(mContactListAdapter);
        }

        // Click listener to add a contact. (+) Button on screen
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addContactIntent = new Intent(view.getContext(), CreateContact.class);
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

                Intent editContactIntent = new Intent(getApplicationContext(), CreateContact.class);
                Contact contact = (Contact) parent.getItemAtPosition(position);
                int contactId = contact.getId();
                editContactIntent.putExtra("contactId", contactId);
                editContactIntent.putExtra("mode", EDIT_MODE);
                startActivityForResult(editContactIntent, EDIT_CONTACT_REQUEST, null);
            }
        });
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
        mContactListAdapter.clear();
        ContactManager cm = new ContactManager(mFilePath);
        ArrayList<Contact> contacts = cm.getContactList();
        sortByLastName(contacts);
        mContactListAdapter.addAll(contacts);
        mContactListAdapter.notifyDataSetChanged();
    }

    /* Simple method that takes the array list of contacts and
     sorts the contacts by last name. It takes the contacts
     ArrayList as arguments and returns void.*/
    public void sortByLastName(ArrayList<Contact> contacts) {
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

    public void reverseSortByLastName(ArrayList<Contact> contacts) {
        sortByLastName(mContactList);
        Collections.reverse(mContactList);
    }
}

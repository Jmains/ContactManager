package com.example.contactmanager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    static final int ADD_CONTACT_REQUEST = 1;
    static final int DELETE_CONTACT_REQUEST = 2;
    static final int EDIT_CONTACT_REQUEST = 3;
    static final int EDIT_MODE = 2;
    static final int ADD_MODE = 1;

    File filePath;
    private static ArrayList<Contact> mContactList;
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.id_list_view);

        filePath = new File(getFilesDir(), "contacts.txt");

        ContactManager contactManager = new ContactManager(filePath);


        //mContactList = mContactManager.getAllContactsFromDb(filePath);
        mContactList = contactManager.getContactList();

        if (mContactList != null && mContactList.size() != 0) {
            String[] names = getListOfFirstNames(mContactList);
            // Inflate the list view with first names of every contact and display it
             adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, names);
            listView.setAdapter(adapter);
        }


        //mContactList = mContactManager.getAllContactsFromDb(filePath);
        System.out.println("Value of contactList: " + mContactList);
        for (Contact c: mContactList) {
            System.out.println("Name: " + c.getFirstName());
        }

        // Click listener to add a contact
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addContactIntent = new Intent(view.getContext(), CreateContact.class);
                addContactIntent.putExtra("mode", ADD_MODE);
                startActivityForResult(addContactIntent, ADD_CONTACT_REQUEST, null);
            }
        });

        // Click listener to edit a contact
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: List item clicked");

                Intent editContactIntent = new Intent(getApplicationContext(), CreateContact.class);
                editContactIntent.putExtra("contactId", position);
                editContactIntent.putExtra("mode", EDIT_MODE);
                startActivityForResult(editContactIntent, EDIT_CONTACT_REQUEST, null);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The added picked a contact.
                if (data != null) {
//                    mContactList = mContactManager.getAllContactsFromDb(filePath);
//                    if (mContactList.size() != 0) {
//                        String[] names = getListOfFirstNames(mContactList);
//                        adapter.addAll(names);
//                        // Inflate the list view with first names of every contact and display it
//                        adapter.notifyDataSetChanged();
//                    }
                    //Contact contact = (Contact)data.getSerializableExtra("Contact");
                }

            }
            // result code was RESULT_CANCELED do nothing
        } else if (requestCode == DELETE_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {

            }
        }
    }




    private String[] getListOfFirstNames(ArrayList<Contact> contactList) {

        System.out.println("arr size: " + contactList.size());

        int listSize = contactList.size();
        int i = 0;
        String[] names = new String[listSize];
        for (Contact c: contactList) {
            names[i] = c.getFirstName() + " " + c.getLastName();
            i++;
        }

        return names;
    }

    private void updateUI() {

    }

    @Override
    protected void onResume() {
//        mContactList = mContactManager.getAllContactsFromDb(filePath);
//        if (mContactList!= null) {
//            String[] names = getListOfFirstNames(mContactList);
//            adapter.addAll(names);
//            adapter.notifyDataSetChanged();
//        }

        super.onResume();
    }
}

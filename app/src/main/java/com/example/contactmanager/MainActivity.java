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
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    static final int ADD_CONTACT_REQUEST = 1;
    static final int DELETE_CONTACT_REQUEST = 2;
    static final int EDIT_CONTACT_REQUEST = 3;
    static final int EDIT_MODE = 2;
    static final int ADD_MODE = 1;

    File filePath;
    private ArrayList<Contact> mContactList;
    ContactListAdapter mContactListAdapter;
    ListView listView;
    ContactManager mContactManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.id_list_view);
        filePath = new File(getFilesDir(), "contacts.txt");
        mContactManager = new ContactManager(filePath);
        mContactList = mContactManager.getContactList();
        sortByLastName(mContactList);

        if (mContactList != null && mContactList.size() != 0) {
            // Inflate the list view with first names of every contact and display it
            mContactListAdapter = new ContactListAdapter(this, android.R.layout.simple_list_item_1, mContactList);
            listView.setAdapter(mContactListAdapter);
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
                Contact contact = (Contact) parent.getItemAtPosition(position);
                int contactId = contact.getId();
                editContactIntent.putExtra("contactId", contactId);
                editContactIntent.putExtra("mode", EDIT_MODE);
                startActivityForResult(editContactIntent, EDIT_CONTACT_REQUEST, null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Regardless of the request code update UI with new information
        mContactListAdapter.clear();
        ContactManager cm = new ContactManager(filePath);
        ArrayList<Contact> contacts = cm.getContactList();
        sortByLastName(contacts);
        mContactListAdapter.addAll(contacts);
        mContactListAdapter.notifyDataSetChanged();
    }

    // Simple method that takes the array list of contacts and
    // sorts the contacts by last name
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

}

package com.example.contactmanager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class MainActivity extends AppCompatActivity {

    static final int VIEW_CONTACT_REQUEST = 1; // The request code.
    int userIdm;
    String filePath = "contacts.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Read contacts from file


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewContactIntent = new Intent(view.getContext(), CreateContact.class);
                viewContactIntent.putExtra("userId", userIdm);
                startActivityForResult(viewContactIntent, VIEW_CONTACT_REQUEST, null);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Check which request we're responding to
        if (requestCode == VIEW_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                if (data != null) {
                    Contact contact = (Contact)data.getSerializableExtra("Contact");
                }
                // Do something with the contact here (bigger example below)
            }
        }
    }

    public void readFile() {

    }
}

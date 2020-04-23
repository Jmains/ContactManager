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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ContactListAdapter extends ArrayAdapter<Contact> {

    // Member variables
    private Context mContext;
    private int mResource;

    // ViewHolder class for Contact object
    // could have more fields but firstName
    // is all we need
    static class ViewHolder {
        TextView contactName;
    }

    public ContactListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Contact> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String firstName = null;
        String lastName = null;
        // Grab the Contact object at the position clicked
        if (getItem(position) != null) {
            Contact contact = getItem(position);
            firstName = contact.getFirstName();
            lastName = contact.getLastName();
        }

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            // We're using the default layout and view provided by android studio
            holder = new ViewHolder();
            holder.contactName = convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (firstName != null && lastName != null) {
            String fullName = firstName + " " + lastName;
            holder.contactName.setText(fullName);
        }

        return convertView;
    }
}

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

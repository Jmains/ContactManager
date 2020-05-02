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

import android.provider.BaseColumns;

/* This class lays down the basic schema for a user(contact)
   in our SQlite database.
 */
public class UserContract {

    private UserContract() {}

    public static class UserTable implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_FIRST_NAME = "firstName";
        public static final String COLUMN_NAME_LAST_NAME = "lastName";
        public static final String COLUMN_NAME_PHONE_NUM = "phoneNum";
        public static final String COLUMN_NAME_DOB = "dateOfBirth";
        public static final String COLUMN_NAME_DOFC = "dateOfFirstContact";

        public static final String COLUMN_NAME_ADDRESS_1 = "addressLine1";
        public static final String COLUMN_NAME_ADDRESS_2 = "addressLine2";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_STATE = "state";
        public static final String COLUMN_NAME_ZIPCODE = "zipcode";
    }
}

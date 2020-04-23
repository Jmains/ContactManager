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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/* This class controls all Contacts I/O between the SQlite Database and the app.*/

public class DatabaseManager {

    private static final String TAG = "DatabaseManager";
    private ArrayList<Contact> mContactList;

    private SQLiteDatabase db;
    private Context mContext;

    DatabaseManager(Context context) {
        mContext = context;
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        db = dbHelper.getWritableDatabase();
        mContactList = getContactListFromDB();
    }

    /*This method returns the cached contact list arraylist from the database.
    * it takes nothing as a parameter and returns an arraylist of contacts. */
    public ArrayList<Contact> getContactList() {
        return this.mContactList;
    }

    /*This returns all the contacts from the SQLite database. It takes in nothing
    * as parameters and returns the contacts from the db in the form of an arraylist
    * of type Contact.*/
    public ArrayList<Contact> getContactListFromDB() {

        mContactList = new ArrayList<>();
        String where = UserContract.UserTable._ID + ">?";
        String[] whereArgs = {"0"};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        Cursor cs = null;

        try {
            cs = db.query(UserContract.UserTable.TABLE_NAME, null, where, whereArgs, groupBy, having, orderBy);
            if (cs == null) {
                return mContactList;
            }
        } catch (Exception ex) {
            Log.d(TAG, "getContactList: " + ex.getMessage());
        }

        try {
            while (cs.moveToNext()) {
                Contact contact = new Contact(
                        cs.getInt(cs.getColumnIndexOrThrow(UserContract.UserTable._ID)),
                        cs.getString(cs.getColumnIndexOrThrow(UserContract.UserTable.COLUMN_NAME_FIRST_NAME)),
                        cs.getString(cs.getColumnIndexOrThrow(UserContract.UserTable.COLUMN_NAME_LAST_NAME)),
                        cs.getString(cs.getColumnIndexOrThrow(UserContract.UserTable.COLUMN_NAME_PHONE_NUM)),
                        cs.getString(cs.getColumnIndexOrThrow(UserContract.UserTable.COLUMN_NAME_DOB)),
                        cs.getString(cs.getColumnIndexOrThrow(UserContract.UserTable.COLUMN_NAME_DOFC))
                );
                mContactList.add(contact);
            }
        } catch (Exception ex) {
            Log.d(TAG, "getContactList: " + ex.getMessage());
        } finally {
            cs.close();
        }
        return mContactList;
    }
    /*This method takes in a first name, last name, phone num, date of birth, and date of first contact
    * as parameters and adds the new user in the database. Returns void. */
    public void addContact(String fName, String lName, String phoneNum, String dob, String dofc) {

        try {
            ContentValues cv = new ContentValues();
            cv.put(UserContract.UserTable.COLUMN_NAME_FIRST_NAME, fName);
            cv.put(UserContract.UserTable.COLUMN_NAME_LAST_NAME, lName);
            cv.put(UserContract.UserTable.COLUMN_NAME_PHONE_NUM, phoneNum);
            cv.put(UserContract.UserTable.COLUMN_NAME_DOB, dob);
            cv.put(UserContract.UserTable.COLUMN_NAME_DOFC, dofc);

            db.insert(UserContract.UserTable.TABLE_NAME, null, cv);

        } catch (Exception ex) {
            Log.d(TAG, "addContact: " + ex.getMessage());
        }
    }

    /*This method searches the database for the contact given the contact id as a parameter.
    * Then updates that id with the given fields. Returns void. */
    public void editContact(int id, String fName, String lName, String phoneNum, String dob, String dofc) {

        try {
            String where = "_ID=?";
            String whereArgs[] = { Integer.toString(id) };
            ContentValues cv = new ContentValues();
            cv.put(UserContract.UserTable.COLUMN_NAME_FIRST_NAME, fName);
            cv.put(UserContract.UserTable.COLUMN_NAME_LAST_NAME, lName);
            cv.put(UserContract.UserTable.COLUMN_NAME_PHONE_NUM, phoneNum);
            cv.put(UserContract.UserTable.COLUMN_NAME_DOB, dob);
            cv.put(UserContract.UserTable.COLUMN_NAME_DOFC, dofc);
            db.update(UserContract.UserTable.TABLE_NAME, cv, where, whereArgs);

        } catch (Exception ex) {
            Log.d(TAG, "editContact: " + ex.getMessage());
        }
    }

    /*This method takes in the contact id as a parameter and
     * returns the specified user from the local contact arraylist.
     * Returns a contact object. */
    public Contact findContactById(int id) {

        for (Contact contact: mContactList) {
            if (contact.getId() == id) {
                return contact;
            }
        }
        return null;
    }

    /*This method takes in the contact id as a parameter and
    * deletes the specified user from the database. Returns void. */
    public void deleteContactById(int id) {

        try {
            String where = "_ID=?";
            String whereArgs[] = { Integer.toString(id) };
            db.delete(UserContract.UserTable.TABLE_NAME, where, whereArgs);
        } catch (Exception ex) {
            Log.d(TAG, "deleteContactById: " + ex.getMessage());
        }
    }

    /*This method drops the user tables and creates a new table based on
    * the new schema and clears the contactList arraylist. This method takes
    * in void as a parameter and returns void. */
    public void reinitialize() {

        db.execSQL("DROP TABLE IF EXISTS " + UserContract.UserTable.TABLE_NAME);
        db.execSQL("CREATE TABLE " + UserContract.UserTable.TABLE_NAME + " (" +
                UserContract.UserTable._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT" + ", " +
                UserContract.UserTable.COLUMN_NAME_FIRST_NAME + " varchar(25) NOT NULL, " +
                UserContract.UserTable.COLUMN_NAME_LAST_NAME + " varchar(25) NOT NULL, " +
                UserContract.UserTable.COLUMN_NAME_PHONE_NUM + " varchar(25) NOT NULL, " +
                UserContract.UserTable.COLUMN_NAME_DOB + " varchar(10), " +
                UserContract.UserTable.COLUMN_NAME_DOFC + " varchar(10)" +
                ")"
        );
        //DatabaseHelper.incrementDbVersion();
        mContactList.clear();
    }
}

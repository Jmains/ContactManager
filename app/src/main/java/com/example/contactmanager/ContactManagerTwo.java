package com.example.contactmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class ContactManagerTwo {

    private static final String TAG = "ContactManagerTwo";
    private ArrayList<Contact> mContactList;

    private SQLiteDatabase db;
    private Context mContext;

    ContactManagerTwo(Context context) {
        mContext = context;
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        db = dbHelper.getWritableDatabase();
        mContactList = getContactListFromDB();
    }

    public ArrayList<Contact> getContactList() {
        return this.mContactList;
    }

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

    public void addContact(String fName, String lName, String phoneNum, String dob, String dofc) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(UserContract.UserTable.COLUMN_NAME_FIRST_NAME, fName);
            cv.put(UserContract.UserTable.COLUMN_NAME_LAST_NAME, lName);
            cv.put(UserContract.UserTable.COLUMN_NAME_PHONE_NUM, phoneNum);
            cv.put(UserContract.UserTable.COLUMN_NAME_DOB, dob);
            cv.put(UserContract.UserTable.COLUMN_NAME_DOFC, dofc);

            long id = db.insert(UserContract.UserTable.TABLE_NAME, null, cv);


        } catch (Exception ex) {
            Log.d(TAG, "addContact: " + ex.getMessage());
        }
    }

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

    public Contact findContactById(int id) {
        for (Contact contact: mContactList) {
            if (contact.getId() == id) {
                return contact;
            }
        }
        return null;
    }

    public void deleteContactById(int id) {
        try {
            String where = "_ID=?";
            String whereArgs[] = { Integer.toString(id) };
            db.delete(UserContract.UserTable.TABLE_NAME, where, whereArgs);
        } catch (Exception ex) {
            Log.d(TAG, "deleteContactById: " + ex.getMessage());
        }
    }


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

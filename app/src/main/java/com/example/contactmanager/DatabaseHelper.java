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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* This class creates a new database if one does not exist.
* Its constructor receives the application context as parameter
* as creates a database with SQliteOpenHelper. The class handles
* all changes to the database schema.
* */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Contacts.db";

    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + UserContract.UserTable.TABLE_NAME + " (" +
                    UserContract.UserTable._ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT" + ", " +
                    UserContract.UserTable.COLUMN_NAME_FIRST_NAME + " varchar(25) NOT NULL, " +
                    UserContract.UserTable.COLUMN_NAME_LAST_NAME + " varchar(25) NOT NULL, " +
                    UserContract.UserTable.COLUMN_NAME_PHONE_NUM + " varchar(25) NOT NULL, " +
                    UserContract.UserTable.COLUMN_NAME_DOB + " varchar(10), " +
                    UserContract.UserTable.COLUMN_NAME_DOFC + " varchar(10) " +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS);
    }

    /*Once db version increase add new fields*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int upgradeTo = oldVersion + 1;
        while(upgradeTo <= newVersion) {
            switch(upgradeTo) {
                case 2: db.execSQL(
                        "ALTER TABLE " + UserContract.UserTable.TABLE_NAME +
                                " ADD COLUMN " + UserContract.UserTable.COLUMN_NAME_ADDRESS_1 + " varchar(25);"
                    );
                    db.execSQL(
                            "ALTER TABLE " + UserContract.UserTable.TABLE_NAME +
                                    " ADD COLUMN " + UserContract.UserTable.COLUMN_NAME_ADDRESS_2 + " varchar(25);"
                    );
                    db.execSQL(
                            "ALTER TABLE " + UserContract.UserTable.TABLE_NAME +
                                    " ADD COLUMN " + UserContract.UserTable.COLUMN_NAME_CITY + " varchar(25);"
                    );
                    db.execSQL(
                            "ALTER TABLE " + UserContract.UserTable.TABLE_NAME +
                                    " ADD COLUMN " + UserContract.UserTable.COLUMN_NAME_STATE + " varchar(2);"
                    );
                    db.execSQL(
                            "ALTER TABLE " + UserContract.UserTable.TABLE_NAME +
                                    " ADD COLUMN " + UserContract.UserTable.COLUMN_NAME_ZIPCODE + " varchar(5);"
                    );
                    break;
            }
            upgradeTo++;
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.setVersion(newVersion);
    }
}

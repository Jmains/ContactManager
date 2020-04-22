package com.example.contactmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Contacts.db";

    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + UserContract.UserTable.TABLE_NAME + " (" +
                    UserContract.UserTable._ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT" + ", " +
                    UserContract.UserTable.COLUMN_NAME_FIRST_NAME + " varchar(25) NOT NULL, " +
                    UserContract.UserTable.COLUMN_NAME_LAST_NAME + " varchar(25) NOT NULL, " +
                    UserContract.UserTable.COLUMN_NAME_PHONE_NUM + " varchar(25) NOT NULL, " +
                    UserContract.UserTable.COLUMN_NAME_DOB + " varchar(10), " +
                    UserContract.UserTable.COLUMN_NAME_DOFC + " varchar(10)" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserContract.UserTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(newVersion);
    }

    public static int incrementDbVersion() {
        ++DATABASE_VERSION;
        return DATABASE_VERSION;
    }
}

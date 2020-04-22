package com.example.contactmanager;

import android.provider.BaseColumns;

public class UserContract {

    private UserContract() {}

    public static class UserTable implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_FIRST_NAME = "firstName";
        public static final String COLUMN_NAME_LAST_NAME = "lastName";
        public static final String COLUMN_NAME_PHONE_NUM = "phoneNum";
        public static final String COLUMN_NAME_DOB = "dateOfBirth";
        public static final String COLUMN_NAME_DOFC = "dateOfFirstContact";
    }
}

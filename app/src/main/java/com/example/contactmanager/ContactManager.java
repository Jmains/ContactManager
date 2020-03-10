package com.example.contactmanager;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class ContactManager {

    private File fp;

    private static final String TAG = "ContactManager";

    private ArrayList<Contact> mContactList = new ArrayList<>();

    ContactManager(File filePath) {

        fp = filePath;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(fp, "r");
            // Go to beginning of file
            raf.seek(0);

            if (raf.length() == 0) {
                return;
            }
            // Read file line by line
            int i = 0;
            int id = 0;
            long length = raf.length();

            while (i < length) {
                raf.seek(i);
                byte[] fName = new byte[25];
                raf.read(fName);
                raf.seek(i + 25);
                byte[] lName = new byte[25];
                raf.read(lName);
                raf.seek(i + 50);
                byte[] phoneNum = new byte[20];
                raf.read(phoneNum);
                raf.seek(i + 70);
                byte[] birthDate = new byte[10];
                raf.read(birthDate);
                raf.seek(i + 80);
                byte[] dayOfFirstContact = new byte[10];
                raf.read(dayOfFirstContact);
                byte[] newline = new byte[2];
                raf.read(newline);

                String fName1 = new String(fName);
                String lName1 = new String(lName);
                String phoneNum1 = new String(phoneNum);
                String birthDate1 = new String(birthDate);
                String dayOfFirstContact1 = new String(dayOfFirstContact);

                Contact contact = new Contact(
                        id,
                        fName1.trim(),
                        lName1.trim(),
                        phoneNum1.trim(),
                        birthDate1.trim(),
                        dayOfFirstContact1.trim()
                );

                mContactList.add(contact);

                // Move to next record
                i += 91;
                // Increment ID
                id++;
            }
            raf.close();

        } catch (IOException e) {
            Log.d(TAG, "ContactManager: Failed to open and read file");
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    Log.d(TAG, "ContactManager: Failed to close file");
                }
            }
        }
    }

    public void addContactToDb(String fName, String lName, String phoneNum, String dob, String dofc) {

        // Create a new contact
        Contact contact = new Contact(fName, lName, phoneNum, dob, dofc);

        // Add the contact to the database
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(fp, "rw");
            // Seek to the end of the file
            raf.seek(raf.length());
            long fileLength = raf.length();

            raf.write(contact.getFirstName().getBytes());
            raf.seek(fileLength + 25);
            raf.write(contact.getLastName().getBytes());
            raf.seek(fileLength + 50);
            raf.write(contact.getPhoneNum().getBytes());
            raf.seek(fileLength + 70);
            raf.write(contact.getDateOfBirth().getBytes());
            raf.seek(fileLength + 80);
            raf.write(contact.getDateOfFirstContact().getBytes());
            raf.seek(fileLength + 90);
            raf.writeBytes(System.getProperty("line.separator"));   //2 bytes

            raf.close();

        } catch (IOException e) {
            Log.d(TAG, "addContactToDb: Failed to add contact to database");
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
            } catch (IOException e) {
                Log.d(TAG, "addContactToDb: failed to close file");
            }
        }

        // Add the contact list to the local contact list
        mContactList.add(contact);
    }

    public void deleteContactFromDb(int id) {
        // Remove the contact from the database by fill that area with empty string
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(fp, "rw");
            raf.seek(0);
            int contactPos = id * 91;
            raf.seek(contactPos);

            byte[] empty = new byte[25];
            byte[] empty2 = new byte[20];
            byte[] empty3 = new byte[10];

            raf.write(empty);
            raf.seek(contactPos + 25);
            raf.write(empty);
            raf.seek(contactPos + 50);
            raf.write(empty2);
            raf.seek(contactPos + 70);
            raf.write(empty3);
            raf.seek(contactPos + 80);
            raf.write(empty3);
            raf.writeBytes(System.getProperty("line.separator"));   //2 bytes

            raf.close();

        } catch (IOException e) {
            Log.d(TAG, "deleteContactFromDb: Failed to remove contact from db");
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
            } catch (IOException e) {
                Log.d(TAG, "deleteContactFromDb: Failed to close the file");
            }
        }
        // Remove the contact from the local database
        mContactList.remove(id);

    }

    public void editContactInDb(int id, String fName, String lName, String phoneNum, String dob, String dofc) {

        // Edit the contact in the database
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(fp, "rw");
            // Seek to beginning of file
            raf.seek(0);
            int contactPos = id * 91;
            raf.seek(contactPos);

            raf.write(fName.getBytes());
            raf.seek(contactPos + 25);
            raf.write(lName.getBytes());
            raf.seek(contactPos + 50);
            raf.write(phoneNum.getBytes());
            raf.seek(contactPos + 70);
            raf.write(dob.getBytes());
            raf.seek(contactPos + 80);
            raf.write(dofc.getBytes());
            raf.writeBytes(System.getProperty("line.separator"));   //2 bytes

            raf.close();

        } catch (IOException e) {
            Log.d(TAG, "editContact: failed to edit contact");
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
            } catch (IOException e) {
                Log.d(TAG, "editContact: Failed to close file");
            }
        }

        // Edit contact in the local contact list
        Contact contactToBeEdited = findContactById(id);
        contactToBeEdited.setFirstName(fName);
        contactToBeEdited.setLastName(lName);
        contactToBeEdited.setPhoneNum(phoneNum);
        contactToBeEdited.setDateOfBirth(dob);
        contactToBeEdited.setDateOfFirstContact(dofc);
    }

    public Contact findContactById(int i) {
        // Get updated Contact List
        ArrayList<Contact> cl = getContactList();
        for (Contact c: cl) {
            if (c.getId() == i) {
                return c;
            }
        }
        return null;
    }

    /*
    This method reads the file contents and returns an updated
    an array list of the Contact Object of every contact
    in the random access file.
    */
    public ArrayList<Contact> getContactList() {

//        RandomAccessFile raf = null;
//        try {
//            raf = new RandomAccessFile(fp, "r");
//            // Go to beginning of file
//            raf.seek(0);
//
//            if (raf.length() == 0) {
//                return null;
//            }
//            // Read file line by line
//            int i = 0;
//            int id = 0;
//            long length = raf.length();
//
//            while (i < length) {
//                raf.seek(i);
//                byte[] fName = new byte[25];
//                raf.read(fName);
//                raf.seek(i + 25);
//                byte[] lName = new byte[25];
//                raf.read(lName);
//                raf.seek(i + 50);
//                byte[] phoneNum = new byte[20];
//                raf.read(phoneNum);
//                raf.seek(i + 70);
//                byte[] birthDate = new byte[10];
//                raf.read(birthDate);
//                raf.seek(i + 80);
//                byte[] dayOfFirstContact = new byte[10];
//                raf.read(dayOfFirstContact);
//                byte[] newline = new byte[2];
//                raf.read(newline);
//
//                String fName1 = new String(fName);
//                String lName1 = new String(lName);
//                String phoneNum1 = new String(phoneNum);
//                String birthDate1 = new String(birthDate);
//                String dayOfFirstContact1 = new String(dayOfFirstContact);
//
//                Contact contact = new Contact(
//                        id,
//                        fName1.trim(),
//                        lName1.trim(),
//                        phoneNum1.trim(),
//                        birthDate1.trim(),
//                        dayOfFirstContact1.trim()
//                );
//
//                mContactList.add(contact);
//
//                // Move to next record
//                i += 91;
//                // Increment ID
//                id++;
//            }
//            raf.close();
//
//        } catch (IOException e) {
//            Log.d(TAG, "getContactList: Failed to open and read file");
//        } finally {
//            if (raf != null) {
//                try {
//                    raf.close();
//                } catch (IOException e) {
//                    Log.d(TAG, "getContactList: Failed to close file");
//                }
//            }
//        }
        return mContactList;
    }
}

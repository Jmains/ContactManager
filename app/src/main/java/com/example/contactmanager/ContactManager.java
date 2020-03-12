package com.example.contactmanager;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.ArrayList;

public class ContactManager {

    private File fp;

    private static final String TAG = "ContactManager";

    private static final int SIZE_OF_EACH_RECORD = 95;

    private ArrayList<Contact> mContactList = new ArrayList<>();
    private int mLatestUserIdInDb = 0;

    ContactManager(File filePath) {

        fp = filePath;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(fp, "r");
            // Go to beginning of file
            raf.seek(0);

            if (raf.length() == 0) {
                // You are the first user
                return;
            }
            // Read file line by line
            int bytesRead = 0;
            //int id = 0;
            long length = raf.length();

            while (bytesRead < length) {
                raf.seek(bytesRead);
                // Read the first byte at that position
                byte[] fByte = new byte[1];
                raf.read(fByte);
                int firstTwoBytes = new BigInteger(fByte).intValue();
                raf.seek(bytesRead);
                // if the first byte is a zero then continue
                // to the next iteration of the loop
                if (firstTwoBytes == 0) {
                    raf.skipBytes(SIZE_OF_EACH_RECORD);
                    bytesRead += SIZE_OF_EACH_RECORD;
                    continue;
                }
                // Write the contact to the database
                raf.seek(bytesRead);
                byte[] fName = new byte[25];
                raf.read(fName);
                raf.seek(bytesRead + 25);
                byte[] lName = new byte[25];
                raf.read(lName);
                raf.seek(bytesRead + 50);
                byte[] phoneNum = new byte[20];
                raf.read(phoneNum);
                raf.seek(bytesRead + 70);
                byte[] birthDate = new byte[10];
                raf.read(birthDate);
                raf.seek(bytesRead + 80);
                byte[] dayOfFirstContact = new byte[10];
                raf.read(dayOfFirstContact);
                raf.seek(bytesRead + 90);
                byte[] tempId = new byte[4];
                raf.read(tempId);
                raf.seek(bytesRead + 94);
                byte[] newline = new byte[2];
                raf.read(newline);

                int id = new BigInteger(tempId).intValue();
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
                bytesRead += SIZE_OF_EACH_RECORD;
                // Increment ID
                //id++;
                mLatestUserIdInDb = contact.getId();
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
        int contactId = ++mLatestUserIdInDb;

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
            raf.writeInt(contactId);
            raf.seek(fileLength + 94);
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
            int contactPos = (id - 1) * SIZE_OF_EACH_RECORD;
            raf.seek(contactPos);

            byte[] fnAndln = new byte[25];
            byte[] phone = new byte[20];
            byte[] dobAnddofc = new byte[10];
            byte[] emptyid = new byte[4];

            // Write empty bytes to file
            raf.write(fnAndln);
            //raf.seek(contactPos + 25);
            raf.write(fnAndln);
            //raf.seek(contactPos + 50);
            raf.write(phone);
            //raf.seek(contactPos + 70);
            raf.write(dobAnddofc);
            //raf.seek(contactPos + 80);
            raf.write(dobAnddofc);
            raf.write(emptyid);
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
    }

    public void editContactInDb(int id, String fName, String lName, String phoneNum, String dob, String dofc) {

        // Edit the contact in the database
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(fp, "rw");
            // Seek to beginning of file
            raf.seek(0);

            if (raf.length() == 0) {
                // You are the first user
                return;
            }
            // Read file line by line
            int bytesRead = 0;
            //int id = 0;
            long length = raf.length();

            while (bytesRead < length) {
                raf.seek(bytesRead);

                byte[] fByte = new byte[1];
                raf.read(fByte);
                int firstTwoBytes = new BigInteger(fByte).intValue();
                raf.seek(bytesRead);
                // If the first byte is a zero then continue
                // to the next iteration of the loop
                if (firstTwoBytes == 0) {
                    raf.skipBytes(SIZE_OF_EACH_RECORD);
                    bytesRead += SIZE_OF_EACH_RECORD;
                    continue;
                }
                // Seek to beginning of record
                raf.seek(bytesRead);
                raf.seek(bytesRead + 90);
                byte[] tempDbId = new byte[4];
                raf.read(tempDbId);
                int dbId = new BigInteger(tempDbId).intValue();
                if (dbId != id) {
                    bytesRead += SIZE_OF_EACH_RECORD;
                    continue;
                }
                // Id match found
                break;
            }

            // If end of file reached then return
            if (bytesRead == length) {
                Log.d(TAG, "editContactInDb: End of file reached");
                return;
            }

            // Clear the previous bytes
            raf.seek(bytesRead);

            byte[] fnAndln = new byte[25];
            byte[] phone = new byte[20];
            byte[] dobAnddofc = new byte[10];

            // Write empty bytes to file
            raf.write(fnAndln);
            //raf.seek(contactPos + 25);
            raf.write(fnAndln);
            //raf.seek(contactPos + 50);
            raf.write(phone);
            //raf.seek(contactPos + 70);
            raf.write(dobAnddofc);
            //raf.seek(contactPos + 80);
            raf.write(dobAnddofc);

            // Seek back to beginning of record
            raf.seek(bytesRead);
            // Write into database
            raf.write(fName.getBytes());
            raf.seek(bytesRead + 25);
            raf.write(lName.getBytes());
            raf.seek(bytesRead + 50);
            raf.write(phoneNum.getBytes());
            raf.seek(bytesRead + 70);
            raf.write(dob.getBytes());
            raf.seek(bytesRead + 80);
            raf.write(dofc.getBytes());
            raf.seek(bytesRead + 94);
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
    }

    public Contact findContactById(int i) {
        // Get updated Contact List
        ArrayList<Contact> cl = getContactList();
        for (Contact c: cl) {
            if (c.getId() == i)
                return c;
        }
        return null;
    }

    public ArrayList<Contact> getContactList() {
        return mContactList;
    }

}

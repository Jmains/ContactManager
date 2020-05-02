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

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.ArrayList;

public class ContactManager {

    //private static final String TAG = "ContactManager";

    private static final int SIZE_OF_EACH_RECORD = 95;

    // Member variables
    private File mFilePath;
    private ArrayList<Contact> mContactList = new ArrayList<>();
    private int mLatestUserIdInDb = 0;

    /* This construct reads through the contents of the
    * database and inserts it into the mContact Array list
    * it takes in the Filepath as a parameter and returns nothing*/
    ContactManager(File filePath) {

        mFilePath = filePath;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(mFilePath, "r");
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
                        dayOfFirstContact1.trim(),
                        "",
                        "",
                        "",
                        "",
                        ""
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
            //Log.d(TAG, "ContactManager: Failed to open and read file");
            e.printStackTrace();
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    //Log.d(TAG, "ContactManager: Failed to close file");
                    e.printStackTrace();
                }
            }
        }
    }

    /* This method takes in the first name, last name, phone number,
    * date of birth and date of first contact of the contact.
    * With these values it create a Contact object instance, writes
    * the values to the database and adds the contact to the
    * contactList array list */
    public void addContactToDb(String fName, String lName, String phoneNum, String dob, String dofc) {

        // Create a new contact
        Contact contact = new Contact(fName, lName, phoneNum, dob, dofc);
        int contactId = ++mLatestUserIdInDb;

        // Add the contact to the database
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(mFilePath, "rw");
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
            //Log.d(TAG, "addContactToDb: Failed to add contact to database");
            e.printStackTrace();
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
            } catch (IOException e) {
                //Log.d(TAG, "addContactToDb: failed to close file");
                e.printStackTrace();
            }
        }

        // Add the contact list to the local contact list
        mContactList.add(contact);
    }

    /* This method takes in the contact id as a parameter and deletes
    * the contact in the database by looking for the specific id in
    * the database. It does a linear search in the database */
    public void deleteContactFromDb(int id) {
        // Remove the contact from the database by fill that area with empty string
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(mFilePath, "rw");
            raf.seek(0);
            // Subtract 1 because user ID's start at 1 not 0
            int contactPos = (id - 1) * SIZE_OF_EACH_RECORD;
            raf.seek(contactPos);

            byte[] fnAndln = new byte[25];
            byte[] phone = new byte[20];
            byte[] dobAnddofc = new byte[10];
            byte[] emptyid = new byte[4];

            // Write empty bytes to file
            raf.write(fnAndln);
            raf.write(fnAndln);
            raf.write(phone);
            raf.write(dobAnddofc);
            raf.write(dobAnddofc);
            raf.write(emptyid);
            raf.writeBytes(System.getProperty("line.separator"));   //2 bytes

            raf.close();

        } catch (IOException e) {
            //Log.d(TAG, "deleteContactFromDb: Failed to remove contact from db");
            e.printStackTrace();
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
            } catch (IOException e) {
                //Log.d(TAG, "deleteContactFromDb: Failed to close the file");
                e.printStackTrace();
            }
        }
    }

    /*This method takes in the contact id, first name, last name,
    * phone number, date of birth and date of first contact as
    * parameters. The method does a linear search in the database
    * to look for the id. It then clears the record then rewrites
    * the new data into the database. */
    public void editContactInDb(int id, String fName, String lName, String phoneNum, String dob, String dofc) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(mFilePath, "rw");
            // Seek to beginning of file
            raf.seek(0);

            if (raf.length() == 0) {
                // No users in db
                return;
            }

            int bytesRead = 0;
            long fileLength = raf.length();

            while (bytesRead < fileLength) {
                raf.seek(bytesRead);

                byte[] fByte = new byte[1];
                raf.read(fByte);
                int firstTwoBytes = new BigInteger(fByte).intValue();

                // If the first byte is a zero then that record is
                // empty so jump to the next record.
                if (firstTwoBytes == 0) {
                    raf.skipBytes(SIZE_OF_EACH_RECORD);
                    bytesRead += SIZE_OF_EACH_RECORD;
                    continue;
                }

                // Seek to beginning of record b/c we just
                // read the first byte.
                raf.seek(bytesRead);

                // Seek to where the id or the record is located
                // and read it.
                raf.seek(bytesRead + 90);
                byte[] tempDbId = new byte[4];
                raf.read(tempDbId);
                int dbId = new BigInteger(tempDbId).intValue();
                // Compare the db id to the given id
                // if not a match then go to the next record.
                if (dbId != id) {
                    bytesRead += SIZE_OF_EACH_RECORD;
                    continue;
                }
                // Id match found or EOF reached
                break;
            }

            // If end of file reached then return
            if (bytesRead == fileLength) {
                //Log.d(TAG, "editContactInDb: End of file reached...no id match");
                return;
            }

            // Seek to beginning of the record
            raf.seek(bytesRead);

            byte[] fnAndln = new byte[25];
            byte[] phone = new byte[20];
            byte[] dobAnddofc = new byte[10];

            // Write empty bytes to the record
            raf.write(fnAndln);     // 25 bytes
            raf.write(fnAndln);     // 25 bytes
            raf.write(phone);       // 20 bytes
            raf.write(dobAnddofc);  // 10 bytes
            raf.write(dobAnddofc);  // 10 bytes

            // Seek back to beginning of record
            raf.seek(bytesRead);
            // Write updated record into database
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
            //Log.d(TAG, "editContact: failed to edit contact");
            e.printStackTrace();
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
            } catch (IOException e) {
                //Log.d(TAG, "editContact: Failed to close file");
                e.printStackTrace();
            }
        }
    }

    /* This method takes a contact id as a parameter. It loops through
    * the contactList array list and searches for a matching ID in the
    * arraylist and returns the matched id. */
    public Contact findContactById(int i) {
        if (i == -1)
            return null;
        // Get updated Contact List
        ArrayList<Contact> cl = getContactList();
        for (Contact c: cl) {
            if (c.getId() == i)
                return c;
        }
        return null;
    }

    /* This takes in nothing as a parameter and simply returns
    * the ArrayList of contacts defined by the class. */
    public ArrayList<Contact> getContactList() {
        return mContactList;
    }

}

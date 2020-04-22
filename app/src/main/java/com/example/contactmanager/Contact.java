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
*  Written by Supachai Main for CS4301.002, ContactManager Part.1 , starting March 9th, 2020.
        NetID: sxm163830 */

package com.example.contactmanager;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Contact implements Serializable {

    private int id; // for random access file pointer
    private String firstName;
    private String lastName;
    private String phoneNum;  // 10 digits
    private String dateOfBirth; // 10 characters
    private String dateOfFirstContact; // 10 characters
    private static final AtomicInteger count = new AtomicInteger(0);

    Contact(String firstName, String lastName, String phoneNum, String birthDate, String dateOfFirstContact) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNum = phoneNum;
        this.dateOfBirth = birthDate;
        this.dateOfFirstContact = dateOfFirstContact;
        id = count.incrementAndGet();
    }

    Contact(int id, String firstName, String lastName, String phoneNum, String birthDate, String dateOfFirstContact) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNum = phoneNum;
        this.dateOfBirth = birthDate;
        this.dateOfFirstContact = dateOfFirstContact;
        this.id = id;
    }

    public void setId(int id) { this.id = id;}

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getDateOfFirstContact() {
        return dateOfFirstContact;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setDateOfFirstContact(String dateOfFirstContact) {
        this.dateOfFirstContact = dateOfFirstContact;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getId() {
        return id;
    }
}

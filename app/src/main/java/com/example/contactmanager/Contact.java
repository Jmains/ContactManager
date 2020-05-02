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

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Contact implements Serializable {

    private int id; // for random access file pointer
    private String firstName;
    private String lastName;
    private String phoneNum;  // 10 digits
    private String dateOfBirth; // 10 characters
    private String dateOfFirstContact; // 10 characters
    private String address1 = "";
    private String address2 = "";
    private String city = "";
    private String state = "";
    private String zipcode = "";
    private static final AtomicInteger count = new AtomicInteger(0);

    Contact(String firstName, String lastName, String phoneNum, String birthDate, String dateOfFirstContact) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNum = phoneNum;
        this.dateOfBirth = birthDate;
        this.dateOfFirstContact = dateOfFirstContact;
        id = count.incrementAndGet();
    }

    Contact(int id, String firstName, String lastName, String phoneNum, String birthDate, String dateOfFirstContact,
            String address1, String address2, String city, String state, String zipcode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNum = phoneNum;
        this.dateOfBirth = birthDate;
        this.dateOfFirstContact = dateOfFirstContact;

        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;

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

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}

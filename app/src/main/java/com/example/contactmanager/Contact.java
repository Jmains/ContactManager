package com.example.contactmanager;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Contact implements Serializable {

    private final int id; // for random access file pointer
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

package com.example.contactmanager;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Contact implements Serializable {

    private final int id; // for random access pointer
    private String firstName;
    private String lastName;
    private int phoneNum;
    private String birthDate; // 10 characters
    private String dateOfFirstContact; // 10 characters
    private static final AtomicInteger count = new AtomicInteger(0);

    Contact(String firstName, String lastName, int phoneNum, String birthDate, String dateOfFirstContact) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNum = phoneNum;
        this.birthDate = birthDate;
        this.dateOfFirstContact = dateOfFirstContact;
        id = count.incrementAndGet();
    }

    public int getPhoneNum() {
        return phoneNum;
    }

    public String getBirthDate() {
        return birthDate;
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

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
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

    public void setPhoneNum(int phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getId() {
        return id;
    }
}

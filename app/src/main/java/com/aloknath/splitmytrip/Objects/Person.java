package com.aloknath.splitmytrip.Objects;

import android.graphics.Bitmap;

/**
 * Created by ALOKNATH on 2/24/2015.
 */
public class Person extends Trip{
    private final String name;
    private String contactNo;
    private Bitmap personImage;
    private double amountPaid;
    private double amountOwed;
    private double amountToGet;
    private String email;

    public Person(String tripName, String name) {
        super(tripName);
        this.name = name;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(double amountOwed) {
        this.amountOwed = amountOwed;
    }

    public double getAmountToGet() {
        return amountToGet;
    }

    public void setAmountToGet(double amountToGet) {
        this.amountToGet = amountToGet;
    }

    public String getName() {
        return name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public void setPersonImage(Bitmap personImage) {
        this.personImage = personImage;
    }

    public Bitmap getPersonImage() {
        return personImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

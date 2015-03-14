package com.aloknath.splitmytrip.Objects;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by ALOKNATH on 2/24/2015.
 */
public class Trip implements java.io.Serializable{
    private final String tripName;
    private Bitmap tripImage;
    private double totalCost;
    private int noOfPersons;
    private List<TripItem> tripItems;
    private List<Person> persons;
    private double amountPerHead;


    public Trip(String tripName) {
        this.tripName = tripName;
    }

    public Bitmap getTripImage() {
        return tripImage;
    }

    public void setTripImage(Bitmap tripImage) {
        this.tripImage = tripImage;
    }

    public List<TripItem> getTripItems() {
        return tripItems;
    }

    public void setTripItems(List<TripItem> tripItems) {
        this.tripItems = tripItems;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public int getNoOfPersons() {
        return noOfPersons;
    }

    public void setNoOfPersons(int noOfPersons) {
        this.noOfPersons = noOfPersons;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public String getTripName() {
        return tripName;
    }

    public double getAmountPerHead() {
        return amountPerHead;
    }

    public void setAmountPerHead(double amountPerHead) {
        this.amountPerHead = amountPerHead;
    }
}

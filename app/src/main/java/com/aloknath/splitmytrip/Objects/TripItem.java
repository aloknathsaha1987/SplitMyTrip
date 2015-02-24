package com.aloknath.splitmytrip.Objects;

import android.graphics.Bitmap;

/**
 * Created by ALOKNATH on 2/24/2015.
 */
public class TripItem extends Trip {

    private final String itemName;
    private double itemCost;

    public TripItem( String itemName, String tripName) {
        super(tripName);
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public double getItemCost() {
        return itemCost;
    }

    public void setItemCost(double itemCost) {
        this.itemCost = itemCost;
    }
}

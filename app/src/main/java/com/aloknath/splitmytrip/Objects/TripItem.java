package com.aloknath.splitmytrip.Objects;

/**
 * Created by ALOKNATH on 2/24/2015.
 */
public class TripItem extends Trip implements java.io.Serializable{

    private final String itemName;
    private double itemCost;
    //private transient Bitmap tripItemImage;

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

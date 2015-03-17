package com.aloknath.splitmytrip.Objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by ALOKNATH on 2/24/2015.
 */
public class Person extends Trip implements Comparable<Person>, java.io.Serializable{
    private final String name;
    private String contactNo;
    private transient Bitmap personImage;
    private double amountPaid;
    private double amountOwed;
    private double amountToGet;
    private String email;
    private double balance;

    public Person(String tripName, String name) {
        super(tripName);
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
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

    @Override
    public int compareTo(Person person) {

        int balance = (int) person.getBalance();
        return (int) (this.getBalance() - balance);
    }

    private void writeObject(ObjectOutputStream oos) throws IOException{
        // This will serialize all fields that you did not mark with 'transient'
        // (Java's default behaviour)
        oos.defaultWriteObject();
        // Now, manually serialize all transient fields that you want to be serialized
        if(personImage!=null){
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            boolean success = personImage.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
            if(success){
                oos.writeObject(byteStream.toByteArray());
            }
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        // Now, all again, deserializing - in the SAME ORDER!
        // All non-transient fields
        ois.defaultReadObject();
        // All other fields that you serialized
        byte[] image = (byte[]) ois.readObject();
        if(image != null && image.length > 0){
            personImage = BitmapFactory.decodeByteArray(image, 0, image.length);
        }
    }
}

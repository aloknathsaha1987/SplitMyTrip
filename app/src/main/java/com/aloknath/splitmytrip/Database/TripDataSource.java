package com.aloknath.splitmytrip.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aloknath.splitmytrip.Objects.Person;
import com.aloknath.splitmytrip.Objects.Trip;
import com.aloknath.splitmytrip.Objects.TripItem;

import java.util.ArrayList;
import java.util.List;

import static com.aloknath.splitmytrip.ImageConversion.DbBitmapUtility.getBytes;
import static com.aloknath.splitmytrip.ImageConversion.DbBitmapUtility.getImage;

/**
 * Created by ALOKNATH on 2/24/2015.
 */
public class TripDataSource {

    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase sqLiteDatabase;

    public static final String[] allTripTableColumns =
            {TripDbOpenHelper.TRIPNAME,
            TripDbOpenHelper.PERSONS,
            TripDbOpenHelper.TOTALAMOUNT,
            TripDbOpenHelper.AMOUNTPERHEAD};

    public static final String[] allItenaryTableColumns =
            {TripDbOpenHelper.ITERNARYTRIP,
                    TripDbOpenHelper.ITENARYNAME,
                    TripDbOpenHelper.AMOUNT
                   };

    public static final String[] allPersonTableColumns =
            {TripDbOpenHelper.PERSONNAME,
                    TripDbOpenHelper.PERSONCONTACT,
                    TripDbOpenHelper.PERSONTRIP,
                    TripDbOpenHelper.AMOUNTPAID,
                    TripDbOpenHelper.AMOUNTOWED,
                    TripDbOpenHelper.AMOUNTTOGET,
                    TripDbOpenHelper.BALANCE,
                    TripDbOpenHelper.PERSONIMAGE,
                    TripDbOpenHelper.PERSONEMAIL};


    public TripDataSource(Context context){
        sqLiteOpenHelper = new TripDbOpenHelper(context);

    }

    public void open(){
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close(){
        sqLiteOpenHelper.close();
    }

    public boolean addTrip(Trip trip){

        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TripDbOpenHelper.TRIPNAME, trip.getTripName());
        contentValues.put(TripDbOpenHelper.PERSONS, trip.getNoOfPersons());
        contentValues.put(TripDbOpenHelper.TOTALAMOUNT, trip.getTotalCost());
        contentValues.put(TripDbOpenHelper.AMOUNTPERHEAD, trip.getAmountPerHead());

        long insertId = sqLiteDatabase.insert(TripDbOpenHelper.TRIPSTABLENAME, null, contentValues);
        return (insertId != -1);
    }

    public double addItenary(TripItem tripItem){

        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        int flag = 0;
        double amount = 0;

        // Get the Items from the Database
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TripDbOpenHelper.ITENARYTABLENAME + " WHERE itenary_trip like '%" + tripItem.getTripName() + "%'", null);
        if (cursor.getCount()> 0) {
            while (cursor.moveToNext()) {
                String itemName = cursor.getString(cursor.getColumnIndex(TripDbOpenHelper.ITENARYNAME));
                double oldcost = cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.AMOUNT));
                if (itemName.equalsIgnoreCase(tripItem.getItemName())){
                    amount = tripItem.getItemCost() - oldcost;
                    flag = 1;
                    break;
                }
            }
        }

        if (flag == 0){

            amount = tripItem.getItemCost();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TripDbOpenHelper.ITERNARYTRIP, tripItem.getTripName());
            contentValues.put(TripDbOpenHelper.ITENARYNAME, tripItem.getItemName());
            contentValues.put(TripDbOpenHelper.AMOUNT, tripItem.getItemCost());
            //contentValues.put(TripDbOpenHelper.TRIPITEMIMAGE, getBytes(tripItem.getTripItemImage()));
             sqLiteDatabase.insert(TripDbOpenHelper.ITENARYTABLENAME, null, contentValues);
            return amount;
        }else{
            //update the data base
            updateTripItem(tripItem);
            return amount;
        }
    }

    public boolean addPerson(Person person){

        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        int flag = 0;

        // Get the Items from the Database
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TripDbOpenHelper.PERSONTABLENAME + " WHERE person_trip like '%" + person.getTripName() + "%'", null);
        if (cursor.getCount()> 0) {
            while (cursor.moveToNext()) {
                String personName = cursor.getString(cursor.getColumnIndex(TripDbOpenHelper.PERSONNAME));
                if (personName.equalsIgnoreCase(person.getName())){
                    flag = 1;
                    break;
                }
            }
        }

        if (flag == 0){

            ContentValues contentValues = new ContentValues();
            contentValues.put(TripDbOpenHelper.PERSONNAME, person.getName());
            contentValues.put(TripDbOpenHelper.PERSONCONTACT, person.getContactNo());
            contentValues.put(TripDbOpenHelper.PERSONEMAIL, person.getEmail());
            contentValues.put(TripDbOpenHelper.PERSONTRIP, person.getTripName());
            contentValues.put(TripDbOpenHelper.AMOUNTPAID, person.getAmountPaid());
            contentValues.put(TripDbOpenHelper.AMOUNTOWED, person.getAmountOwed());
            contentValues.put(TripDbOpenHelper.AMOUNTTOGET, person.getAmountToGet());
            contentValues.put(TripDbOpenHelper.BALANCE, person.getBalance());
            contentValues.put(TripDbOpenHelper.PERSONIMAGE, getBytes( person.getPersonImage()));

            sqLiteDatabase.insert(TripDbOpenHelper.PERSONTABLENAME, null, contentValues);

            return true;

        }else{
            return false;
        }
    }

    public List<Trip> getTrips(){

        // Get a list of Trips from the Trip Table
        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TripDbOpenHelper.TRIPSTABLENAME, null);
        // The cursor contains all the persons that have gone to a particular trip

        List<Trip> trips = cursorToTripsList(cursor);
        return trips;

    }

    private List<Trip> cursorToTripsList(Cursor cursor) {

        List<Trip> trips = new ArrayList<>();
        if (cursor.getCount()> 0) {
            while (cursor.moveToNext()) {
                Trip trip = new Trip(cursor.getString(cursor.getColumnIndex(TripDbOpenHelper.TRIPNAME)));
                trip.setNoOfPersons(cursor.getInt(cursor.getColumnIndex(TripDbOpenHelper.PERSONS)));
                trip.setTotalCost(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.TOTALAMOUNT)));
                trip.setAmountPerHead(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.AMOUNTPERHEAD)));
                trips.add(trip);
            }
        }

        return trips;

    }


    public List<Person> getPersonsInTrip(String tripName){

        // Get a list of person names from the person Table
        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TripDbOpenHelper.PERSONTABLENAME +
                " WHERE person_trip like '%" + tripName + "%'", null);
        // The cursor contains all the persons that have gone to a particular trip

        List<Person> persons = cursorToPersonsList(tripName, cursor);
        return persons;
    }

    private List<Person> cursorToPersonsList(String tripName, Cursor cursor) {

        List<Person> persons = new ArrayList<>();
        if (cursor.getCount()> 0) {
            while (cursor.moveToNext()) {
                Person person = new Person(tripName, cursor.getString(cursor.getColumnIndex(TripDbOpenHelper.PERSONNAME)));
                person.setContactNo(cursor.getString(cursor.getColumnIndex(TripDbOpenHelper.PERSONCONTACT)));
                person.setAmountPaid(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.AMOUNTPAID)));
                person.setAmountToGet(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.AMOUNTTOGET)));
                person.setAmountOwed(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.AMOUNTOWED)));
                person.setBalance(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.BALANCE)));
                person.setPersonImage(getImage(cursor.getBlob(cursor.getColumnIndex(TripDbOpenHelper.PERSONIMAGE))));
                person.setEmail(cursor.getString(cursor.getColumnIndex(TripDbOpenHelper.PERSONEMAIL)));
                persons.add(person);
            }
        }

        return persons;
    }

    public List<TripItem> getTripItems(String tripName){

        // Get a list of Item names from the Items Table
        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TripDbOpenHelper.ITENARYTABLENAME +
                " WHERE itenary_trip like '%" + tripName + "%'", null);
        // The cursor contains all the persons that have gone to a particular trip

        List<TripItem> items = cursorToItemsList(tripName, cursor);
        return items;
    }

    private List<TripItem> cursorToItemsList(String tripName, Cursor cursor) {

        List<TripItem> items = new ArrayList<>();

        if (cursor.getCount()> 0) {
            while (cursor.moveToNext()) {
                TripItem item = new TripItem(cursor.getString(cursor.getColumnIndex(TripDbOpenHelper.ITENARYNAME)), tripName);
                item.setItemCost(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.AMOUNT)));
              //  item.setTripItemImage(getImage(cursor.getBlob(cursor.getColumnIndex(TripDbOpenHelper.TRIPITEMIMAGE))));
                items.add(item);
            }
        }

        return items;
    }


    public Person getPersonDetails(String personName, String personTrip){

        Person person = new Person( personTrip, personName);
        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TripDbOpenHelper.PERSONTABLENAME +
                " WHERE person_name like '%" + personName + "%' AND person_trip like '%" + personTrip + "%'", null);

        if (cursor.getCount()> 0) {
            while (cursor.moveToNext()) {

                person.setContactNo(cursor.getString(cursor.getColumnIndex(TripDbOpenHelper.PERSONCONTACT)));
                person.setEmail(cursor.getString(cursor.getColumnIndex(TripDbOpenHelper.PERSONEMAIL)));
                person.setAmountPaid(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.AMOUNTPAID)));
                person.setAmountOwed(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.AMOUNTOWED)));
                person.setAmountToGet(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.AMOUNTTOGET)));
                person.setBalance(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.BALANCE)));
                person.setPersonImage(getImage(cursor.getBlob(cursor.getColumnIndex(TripDbOpenHelper.PERSONIMAGE))));
            }
        }

        return person;
    }


    public Trip getTrip(String tripName){
        Trip trip = new Trip(tripName);

        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TripDbOpenHelper.TRIPSTABLENAME +
                " WHERE trip_name like '%" + tripName + "%'", null);

        if (cursor.getCount()> 0) {
            while (cursor.moveToNext()) {
               trip.setNoOfPersons(cursor.getInt(cursor.getColumnIndex(TripDbOpenHelper.PERSONS)));
               trip.setTotalCost(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.TOTALAMOUNT)));
               trip.setAmountPerHead(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.AMOUNTPERHEAD)));
               return trip;
            }
        }
        return null;
    }

    public boolean updateTrip(Trip trip) {

        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TripDbOpenHelper.TRIPNAME, trip.getTripName());
        contentValues.put(TripDbOpenHelper.PERSONS, trip.getNoOfPersons());
        contentValues.put(TripDbOpenHelper.TOTALAMOUNT,trip.getTotalCost());
        contentValues.put(TripDbOpenHelper.AMOUNTPERHEAD, trip.getAmountPerHead());

       long insertid =   sqLiteDatabase.update(TripDbOpenHelper.TRIPSTABLENAME, contentValues,
                TripDbOpenHelper.TRIPNAME + " = ? ",
                new String[] { trip.getTripName() });

        return (insertid != -1);

    }

    public void removeTrip(Trip trip) {

        // Remove the Trip and the associated persons from all the three tables;
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String tripName = trip.getTripName();

        sqLiteDatabase.delete(TripDbOpenHelper.TRIPSTABLENAME, TripDbOpenHelper.TRIPNAME + " = ?", new String[]{tripName});
        sqLiteDatabase.delete(TripDbOpenHelper.ITENARYTABLENAME, TripDbOpenHelper.ITERNARYTRIP + " =?", new String[]{tripName});
        sqLiteDatabase.delete(TripDbOpenHelper.PERSONTABLENAME, TripDbOpenHelper.PERSONTRIP + " = ?", new String[]{tripName});

    }

    public void updatePersonsPaid(List<Person> persons) {

        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        for(Person person: persons){
            ContentValues contentValues = new ContentValues();
            contentValues.put(TripDbOpenHelper.AMOUNTPAID, person.getAmountPaid());
            contentValues.put(TripDbOpenHelper.AMOUNTOWED, person.getAmountOwed());
            contentValues.put(TripDbOpenHelper.AMOUNTTOGET, person.getAmountToGet());
            contentValues.put(TripDbOpenHelper.BALANCE, person.getBalance());

            sqLiteDatabase.update(TripDbOpenHelper.PERSONTABLENAME, contentValues,
                    TripDbOpenHelper.PERSONTRIP + " = ? AND " + TripDbOpenHelper.PERSONNAME + " = ?",
                    new String[] { person.getTripName(), person.getName() });
        }
    }

    public void updatePerson(Person person) {

        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TripDbOpenHelper.AMOUNTPAID, person.getAmountPaid());
        contentValues.put(TripDbOpenHelper.AMOUNTOWED, person.getAmountOwed());
        contentValues.put(TripDbOpenHelper.AMOUNTTOGET, person.getAmountToGet());
        contentValues.put(TripDbOpenHelper.BALANCE, person.getBalance());

        sqLiteDatabase.update(TripDbOpenHelper.PERSONTABLENAME, contentValues,
                TripDbOpenHelper.PERSONTRIP + " = ? AND " + TripDbOpenHelper.PERSONNAME + " = ?",
                new String[] { person.getTripName(), person.getName() });
    }

    public void updateTripItem(TripItem item) {

        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TripDbOpenHelper.ITERNARYTRIP, item.getTripName());
        contentValues.put(TripDbOpenHelper.ITENARYNAME, item.getItemName());
        contentValues.put(TripDbOpenHelper.AMOUNT, item.getItemCost());

        sqLiteDatabase.update(TripDbOpenHelper.ITENARYTABLENAME, contentValues,
                TripDbOpenHelper.ITERNARYTRIP + " = ? AND " + TripDbOpenHelper.ITENARYNAME + " = ?",
                new String[]{item.getTripName(), item.getItemName()});


    }

    public TripItem getTripItem(String itemTrip, String itemName) {

        TripItem item = new TripItem(itemName, itemTrip);

        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TripDbOpenHelper.ITENARYTABLENAME +
                " WHERE itenary_name like '%" + itemName + "%' AND itenary_trip like '%" + itemTrip + "%'", null);

        if (cursor.getCount()> 0) {
            while (cursor.moveToNext()) {
                item.setItemCost(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.AMOUNT)));
            }
        }
        return item;
    }
}

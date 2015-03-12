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
                    TripDbOpenHelper.AMOUNT};

    public static final String[] allPersonTableColumns =
            {TripDbOpenHelper.PERSONNAME,
                    TripDbOpenHelper.PERSONCONTACT,
                    TripDbOpenHelper.PERSONTRIP,
                    TripDbOpenHelper.AMOUNTPAID,
                    TripDbOpenHelper.AMOUNTOWED,
                    TripDbOpenHelper.AMOUNTTOGET,
                    TripDbOpenHelper.BALANCE};


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

    public boolean addItenary(TripItem tripItem){

        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TripDbOpenHelper.ITERNARYTRIP, tripItem.getTripName());
        contentValues.put(TripDbOpenHelper.ITENARYNAME, tripItem.getItemName());
        contentValues.put(TripDbOpenHelper.AMOUNT, tripItem.getItemCost());

        long insertId = sqLiteDatabase.insert(TripDbOpenHelper.ITENARYTABLENAME, null, contentValues);
        return (insertId != -1);
    }

    public boolean addPerson(Person person){

        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TripDbOpenHelper.PERSONNAME, person.getName());
        contentValues.put(TripDbOpenHelper.PERSONCONTACT, person.getContactNo());
        contentValues.put(TripDbOpenHelper.PERSONTRIP, person.getTripName());
        contentValues.put(TripDbOpenHelper.AMOUNTPAID, person.getAmountPaid());
        contentValues.put(TripDbOpenHelper.AMOUNTOWED, person.getAmountOwed());
        contentValues.put(TripDbOpenHelper.AMOUNTTOGET, person.getAmountToGet());
        contentValues.put(TripDbOpenHelper.BALANCE, person.getBalance());

        long insertId = sqLiteDatabase.insert(TripDbOpenHelper.PERSONTABLENAME, null, contentValues);
        return (insertId != -1);
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
                items.add(item);
            }
        }

        return items;
    }


    public Person getPersonDetails(String personName, String personTrip){

        Person person = new Person(personName, personTrip);
        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TripDbOpenHelper.PERSONTABLENAME +
                " WHERE person_name like '%" + personName + "%' AND person_trip like '%" + personTrip + "%'", null);

        if (cursor.getCount()> 0) {
            while (cursor.moveToNext()) {

                person.setContactNo(cursor.getString(cursor.getColumnIndex(TripDbOpenHelper.PERSONCONTACT)));
                person.setAmountPaid(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.AMOUNTPAID)));
                person.setAmountOwed(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.AMOUNTOWED)));
                person.setAmountToGet(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.AMOUNTTOGET)));
                person.setBalance(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.BALANCE)));

            }
        }

        return person;
    }

    public TripItem getItemDetails(String itemName, String itemTrip){

        TripItem item = new TripItem(itemName, itemTrip);

        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TripDbOpenHelper.TRIPSTABLENAME +
                " WHERE itenary_name like '%" + itemName + "%' AND itenary_trip like '%" + itemTrip + "%'", null);

        if (cursor.getCount()> 0) {
            while (cursor.moveToNext()) {
                item.setItemCost(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.AMOUNT)));
                item.setItemCost(cursor.getDouble(cursor.getColumnIndex(TripDbOpenHelper.AMOUNT)));
            }
        }
        return item;
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


}

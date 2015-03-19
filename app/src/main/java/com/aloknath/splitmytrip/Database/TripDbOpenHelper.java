package com.aloknath.splitmytrip.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ALOKNATH on 2/24/2015.
 */

public class TripDbOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASENAME = "split_my_trip_v_1_2.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TRIPSTABLENAME = "Trips_Table";
    public static final String TRIPNAME = "trip_name";
    public static final String PERSONS = "persons";
    public static final String TOTALAMOUNT = "total_amount";
    public static final String AMOUNTPERHEAD = "amount_per_head";

    public static final String ITENARYTABLENAME = "Itenary_Table";
    public static final String ITERNARYTRIP = "itenary_trip";
    public static final String ITENARYNAME = "itenary_name";
    public static final String AMOUNT = "amount";
   // public static final String TRIPITEMIMAGE = "item_image";


    public static final String PERSONTABLENAME = "Person_table";
    public static final String PERSONNAME = "person_name";
    public static final String PERSONCONTACT = "person_contact";
    public static final String PERSONTRIP = "person_trip";
    public static final String AMOUNTPAID = "amount_paid";
    public static final String AMOUNTOWED = "amount_owed";
    public static final String AMOUNTTOGET = "amount_to_get";
    public static final String BALANCE = "balance";
    public static final String PERSONIMAGE = "person_image";
    public static final String PERSONEMAIL = "person_email";

    private static final String TABLECREATE_TRIPS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TRIPSTABLENAME + "(" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TRIPNAME + " TEXT, " +
            PERSONS + " INTEGER, " +
            TOTALAMOUNT + " BLOB, " +
            AMOUNTPERHEAD + " BLOB " + ")";

    private static final String TABLECREATE_ITENARY_TABLE =
            "CREATE TABLE IF NOT EXISTS " + ITENARYTABLENAME + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ITERNARYTRIP + " TEXT, " +
                    ITENARYNAME + " TEXT, " +
                   //TRIPITEMIMAGE + " BLOB, " +
                    AMOUNT + " BLOB " + ")";

    private static final String TABLECREATE_PERSON_TABLE =
            "CREATE TABLE IF NOT EXISTS " + PERSONTABLENAME + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PERSONNAME + " TEXT, " +
                    PERSONCONTACT + " TEXT, " +
                    PERSONEMAIL + " TEXT, " +
                    PERSONTRIP + " TEXT, " +
                    AMOUNTPAID + " BLOB, " +
                    AMOUNTOWED + " BLOB, " +
                    AMOUNTTOGET + " BLOB, " +
                    PERSONIMAGE + " BLOB, " +
                    BALANCE + " BLOB " +")";

    public TripDbOpenHelper(Context context) {
        super(context, DATABASENAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLECREATE_TRIPS_TABLE);
        database.execSQL(TABLECREATE_ITENARY_TABLE);
        database.execSQL(TABLECREATE_PERSON_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i2) {
        database.execSQL("DROP TABLE IF EXISTS "+ TABLECREATE_TRIPS_TABLE);
        database.execSQL("DROP TABLE IF EXISTS "+ TABLECREATE_ITENARY_TABLE);
        database.execSQL("DROP TABLE IF EXISTS "+ TABLECREATE_PERSON_TABLE);
        onCreate(database);
    }
}

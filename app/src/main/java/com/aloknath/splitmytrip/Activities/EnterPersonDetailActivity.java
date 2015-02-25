package com.aloknath.splitmytrip.Activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aloknath.splitmytrip.Database.TripDataSource;
import com.aloknath.splitmytrip.Objects.Person;
import com.aloknath.splitmytrip.R;

/**
 * Created by ALOKNATH on 2/24/2015.
 */
public class EnterPersonDetailActivity extends Activity {

    private Bundle bundle;
    private int noOfPersons;
    private String tripName;
    private Intent intent;
    private Person person;
    private String personName = null;
    private String phoneNumber = null;
    private String email = null;
    private boolean personDetailsFound;

    private int count = 1;
    Button cancel;
    Button next;
    private TripDataSource tripDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_person);

        tripDataSource = new TripDataSource(this);
        tripDataSource.open();

        intent = getIntent();
        bundle = intent.getExtras();
        tripName = bundle.getString("Trip_title");
        noOfPersons = bundle.getInt("Trip_no_of_persons");

        cancel = (Button)findViewById(R.id.button_cancel);
        next = (Button)findViewById(R.id.button_next);


        EditText editText = (EditText)findViewById(R.id.enter_person_name);
        personName = editText.getText().toString();

        editText = (EditText)findViewById(R.id.enter_person_number);

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                EditText editText = (EditText)findViewById(R.id.enter_person_name);
                personName = editText.getText().toString();

                if(personName != null) {
                    personDetailsFound = searchPerson(personName);
                    Toast.makeText(EnterPersonDetailActivity.this, "Phone No: " + phoneNumber + " Email: " + email, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });


        if(personDetailsFound){
            // Populate the Phone Number and Email Fields
            Toast.makeText(this, "Person details has been found", Toast.LENGTH_SHORT).show();
            if(phoneNumber != null){
                editText = (EditText)findViewById(R.id.enter_person_number);
                editText.setText(phoneNumber);
            }
            if(email != null){
                editText = (EditText)findViewById(R.id.enter_person_email);
                editText.setText(email);
            }

        }else{
            Toast.makeText(this, "Person details has Not been found", Toast.LENGTH_SHORT).show();
            //Enter the Phone Number and Email Fields
            editText = (EditText)findViewById(R.id.enter_person_number);
            phoneNumber = editText.getText().toString();

            editText = (EditText)findViewById(R.id.enter_person_email);
            email = editText.getText().toString();
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                if(count <= noOfPersons ){
                    saveToDb();
                    Toast.makeText(EnterPersonDetailActivity.this, "Item Saved", Toast.LENGTH_SHORT).show();
                    refreshDisplay();

                }else{

                    intent = new Intent(EnterPersonDetailActivity.this, MainActivity.class);
                    Toast.makeText(EnterPersonDetailActivity.this, "Item Saved", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
        });

    }

    private void refreshDisplay() {

        intent = new Intent(EnterPersonDetailActivity.this, EnterPersonDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }


    private void saveToDb() {

        double amount;
        EditText editText = (EditText)findViewById(R.id.enter_person_amount_paid);
        amount = Double.parseDouble(editText.getText().toString());

        person = new Person(tripName, personName);
        person.setContactNo(phoneNumber);
        person.setEmail(email);
        person.setAmountPaid(amount);
        person.setAmountOwed(0);
        person.setAmountToGet(0);

        tripDataSource.addPerson(person);
        tripDataSource.close();


    }

    private boolean searchPerson(final String personNamePassed) {

       Toast.makeText(EnterPersonDetailActivity.this, "Phone Name: " + personNamePassed, Toast.LENGTH_SHORT).show();

       new Thread(){
            public void run(){

                String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
                ContentResolver contentResolver = getContentResolver();
                Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
                String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
                Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
                String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
                String DATA = ContactsContract.CommonDataKinds.Email.DATA;

                Uri lkup = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, personNamePassed);
                Cursor idCursor = getContentResolver().query(lkup, null, null, null, null);

                while (idCursor.moveToNext()) {
                    String id = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts._ID));
//                    String key = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
//                    String name = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    int hasPhoneNumber = Integer.parseInt(idCursor.getString(idCursor.getColumnIndex( HAS_PHONE_NUMBER )));
                            if (hasPhoneNumber > 0) {
                                Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { id }, null);
                                while (phoneCursor.moveToNext()) {
                                    phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                                    //Toast.makeText(EnterPersonDetailActivity.this, "Phone Number: " + phoneNumber, Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                phoneCursor.close();

                                Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID+ " = ?", new String[] { id }, null);

                                while (emailCursor.moveToNext()) {
                                    email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                                    break;
                                   // Toast.makeText(EnterPersonDetailActivity.this, "Email: " + email, Toast.LENGTH_SHORT).show();
                                }
                                emailCursor.close();
                                //currentThread().interrupt();
                            }
                }
                idCursor.close();


//
//
//
//
//
//                Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
//                String _ID = ContactsContract.Contacts._ID;
//                String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
//                String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
//
//                Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
//                String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
//                String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
//
//                Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
//                String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
//                String DATA = ContactsContract.CommonDataKinds.Email.DATA;
//
//                ContentResolver contentResolver = getContentResolver();
//                Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, personNamePassed);
//                Cursor cur = contentResolver.query(CONTENT_URI,
//                        null, null, null, null);
//                if (cur.getCount() > 0) {
//                    while (cur.moveToNext()) {
//                        String contact_id  = cur.getString(cur.getColumnIndex(_ID));
//                        String name = cur.getString(cur.getColumnIndex(DISPLAY_NAME));
//                        if(name.equals(personName)){
//                            // Get the person's Phone Number and Email Address
//                            int hasPhoneNumber = Integer.parseInt(cur.getString(cur.getColumnIndex( HAS_PHONE_NUMBER )));
//                            if (hasPhoneNumber > 0) {
//                                Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
//                                while (phoneCursor.moveToNext()) {
//                                    phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
//                                    Toast.makeText(EnterPersonDetailActivity.this, "Phone Number: " + phoneNumber, Toast.LENGTH_SHORT).show();
//                                }
//                                phoneCursor.close();
//
//                                Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);
//
//                                while (emailCursor.moveToNext()) {
//                                    email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
//                                    Toast.makeText(EnterPersonDetailActivity.this, "Email: " + email, Toast.LENGTH_SHORT).show();
//                                }
//
//                            }
//                        }else{
//                            Log.e("Detail Not Found", "Null");
//                            //Toast.makeText(EnterPersonDetailActivity.this, "Details has not been found: " + phoneNumber, Toast.LENGTH_SHORT).show();
//                        }
//
//
//                    }
//                }

            }
        }.start();

        if(phoneNumber != null || email != null){
            return true;
        }

        return false;

    }

    @Override
    protected void onResume() {
        super.onResume();
        tripDataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tripDataSource.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tripDataSource.close();
    }


}

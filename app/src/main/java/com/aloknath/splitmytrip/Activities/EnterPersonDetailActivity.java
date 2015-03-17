package com.aloknath.splitmytrip.Activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.aloknath.splitmytrip.Database.TripDataSource;
import com.aloknath.splitmytrip.Fragments.KeyBoardFragment;
import com.aloknath.splitmytrip.Objects.Person;
import com.aloknath.splitmytrip.Objects.Trip;
import com.aloknath.splitmytrip.R;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ALOKNATH on 2/24/2015.
 */

public class EnterPersonDetailActivity extends FragmentActivity implements KeyBoardFragment.onKeyBoardEvent {

    private Bundle bundle;
    private int noOfPersons;
    private String tripName;
    private Intent intent;
    private Person person;
    private String personName = null;
    private String phoneNumber = null;
    private String email = null;
    private EditText editText;
    private ProgressDialog progressDialog;
    private KeyBoardFragment keyboard_fragment;
    private EditText enterCost;

    Button cancel;
    Button next;
    private TripDataSource tripDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_person);

        progressDialog = new ProgressDialog(EnterPersonDetailActivity.this);

        tripDataSource = new TripDataSource(this);
        tripDataSource.open();

        intent = getIntent();
        bundle = intent.getExtras();
        tripName = bundle.getString("Trip_title");
        noOfPersons = bundle.getInt("Trip_no_of_persons");

        cancel = (Button)findViewById(R.id.button_cancel);
        next = (Button)findViewById(R.id.button_next);


        editText = (EditText)findViewById(R.id.enter_person_name);
        personName = editText.getText().toString();

        editText = (EditText)findViewById(R.id.enter_person_number);

        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                EditText editText = (EditText)findViewById(R.id.enter_person_name);
                personName = editText.getText().toString();

                if(personName != null) {
                    SearchPhoneAsyncTask phoneAsyncTask = new SearchPhoneAsyncTask();
                    phoneAsyncTask.execute(personName);
                }

            }
        });

        enterCost = (EditText)findViewById(R.id.enter_person_amount_paid);
        enterCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                if(keyboard_fragment==null)
                {
                    keyboard_fragment=KeyBoardFragment.newInstance(enterCost.getText().toString());
                    getSupportFragmentManager().beginTransaction().add(R.id.Key_board_container, keyboard_fragment).commit();

                }
                else
                {
                    if(keyboard_fragment.isVisible())
                        getSupportFragmentManager().beginTransaction().hide(keyboard_fragment).commit();
                    else
                    {
                        keyboard_fragment=KeyBoardFragment.newInstance(enterCost.getText().toString());
                        getSupportFragmentManager().beginTransaction().add(R.id.Key_board_container, keyboard_fragment).commit();
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveToDb();
                noOfPersons = noOfPersons -1;
                Toast.makeText(EnterPersonDetailActivity.this, "Item Saved", Toast.LENGTH_SHORT).show();
                refreshDisplay();
            }
        });

    }

    private class SearchPhoneAsyncTask extends AsyncTask<String, Void, Map<String, String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Fetching Contact Info");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected Map<String, String> doInBackground(String... strings) {

            String personNamePassed = strings[0];
            Map<String, String> personDetails = new HashMap<>();

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

                int hasPhoneNumber = Integer.parseInt(idCursor.getString(idCursor.getColumnIndex( HAS_PHONE_NUMBER )));
                if (hasPhoneNumber > 0) {
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { id }, null);
                    while (phoneCursor.moveToNext()) {
                        personDetails.put("phoneNumber", phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER)));
                        break;
                    }
                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID+ " = ?", new String[] { id }, null);

                    while (emailCursor.moveToNext()) {
                        personDetails.put("email", emailCursor.getString(emailCursor.getColumnIndex(DATA)));
                        break;

                    }
                    emailCursor.close();
                }
            }
            idCursor.close();

            return personDetails;
        }

        @Override
        protected void onPostExecute(Map<String, String> stringStringMap) {
            super.onPostExecute(stringStringMap);
            progressDialog.hide();
            phoneNumber = stringStringMap.get("phoneNumber");
            email = stringStringMap.get("email");
            refreshTextBoxDisplay();
        }
    }

    private void refreshTextBoxDisplay() {

        if(phoneNumber != null){
            editText = (EditText)findViewById(R.id.enter_person_number);
            editText.setText(phoneNumber);

        }else{
            editText = (EditText)findViewById(R.id.enter_person_number);
            editText.setFocusable(true);
        }
        if(email != null){
            editText = (EditText)findViewById(R.id.enter_person_email);
            editText.setText(email);
        }
    }

    private void refreshDisplay() {

        if(noOfPersons < 1){
            intent = new Intent(EnterPersonDetailActivity.this, MainActivity.class);
            Toast.makeText(EnterPersonDetailActivity.this, "Item Saved", Toast.LENGTH_SHORT).show();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else {
            intent = new Intent(EnterPersonDetailActivity.this, EnterPersonDetailActivity.class);
            bundle.putInt("Trip_no_of_persons", noOfPersons);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    @Override
    public void numberIsPressed(String total) {
        enterCost.setText(total);
    }

    @Override
    public void doneButtonPressed(String total) {
        enterCost.setText(total);
        if(keyboard_fragment.isVisible())
            getSupportFragmentManager().beginTransaction().hide(keyboard_fragment).commit();
    }

    @Override
    public void backLongPressed() {
        enterCost.setText("");
    }

    @Override
    public void backButtonPressed(String total) {
        enterCost.setText(total);
    }

    @Override
    public void onBackPressed() {
        if(keyboard_fragment!=null)
        {
            if(keyboard_fragment.isVisible())
                getSupportFragmentManager().beginTransaction().remove(keyboard_fragment).commit();
            else
                super.onBackPressed();
        }
        else
            super.onBackPressed();
    }


    private void saveToDb() {

        double amount;
        EditText editText = (EditText)findViewById(R.id.enter_person_amount_paid);
        amount = Double.parseDouble(editText.getText().toString());

        Trip trip = tripDataSource.getTrip(tripName);
        double amountPerHead = trip.getAmountPerHead();

        double amountOwed = amountPerHead - amount;
        double amountToGet = 0;

        if(amountOwed < 0){
            amountToGet = -(amountOwed);
            amountOwed = 0;
        }
        amountToGet = Math.round(amountToGet*100)/100.0d;
        amountOwed = Math.round(amountOwed*100)/100.0d;

        person = new Person(tripName, personName);
        person.setContactNo(phoneNumber);
        person.setEmail(email);
        person.setAmountPaid(amount);
        person.setAmountOwed(amountOwed);
        person.setAmountToGet(amountToGet);
        person.setBalance(amount - amountPerHead );

        tripDataSource.addPerson(person);
        tripDataSource.close();

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

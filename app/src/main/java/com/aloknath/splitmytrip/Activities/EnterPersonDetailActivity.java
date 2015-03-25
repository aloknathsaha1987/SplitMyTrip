package com.aloknath.splitmytrip.Activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.aloknath.splitmytrip.Database.TripDataSource;
import com.aloknath.splitmytrip.Fragments.KeyBoardFragment;
import com.aloknath.splitmytrip.Objects.Person;
import com.aloknath.splitmytrip.Objects.Trip;
import com.aloknath.splitmytrip.R;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private InputStream photoStream;
    private Bitmap bitmap;
    private boolean personAdded;
    private double totalCost;
    private AlertDialog.Builder alertDialog;

    Button cancel;
    Button next;
    private TripDataSource tripDataSource;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_person);
        alertDialog = new AlertDialog.Builder(this);

        try
        {
            View view = getWindow().getDecorView().findViewById(R.id.scroll_view);
            InputStream ims = getAssets().open("enter_trip.jpg");
            Drawable d = Drawable.createFromStream(ims, null);
            view.setBackground(d);

            ImageView photo = (ImageView)findViewById(R.id.imageView_person);
            ims = getAssets().open("person_image.jpg");
            d = Drawable.createFromStream(ims, null);
            photo.setBackground(d);
            photo.setScaleType(ImageView.ScaleType.FIT_XY);


        }catch(IOException ex)
        {
            return;
        }

        progressDialog = new ProgressDialog(EnterPersonDetailActivity.this);

        tripDataSource = new TripDataSource(this);
        tripDataSource.open();

        intent = getIntent();
        bundle = intent.getExtras();
        tripName = bundle.getString("Trip_title");
        noOfPersons = bundle.getInt("Trip_no_of_persons");

        final Trip trip = tripDataSource.getTrip(tripName);
        totalCost = trip.getTotalCost();

        InputStream ims = null;
        try {
            ims = getAssets().open("button_next.png");
            Drawable d = Drawable.createFromStream(ims, null);

            next = (Button)findViewById(R.id.button_next);
            next.setBackground(d);

            ims = getAssets().open("button_cancel.png");
            d = Drawable.createFromStream(ims, null);

            cancel = (Button)findViewById(R.id.button_cancel);
            cancel.setBackground(d);

        } catch (IOException e) {
            e.printStackTrace();
        }

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
                if(personName.isEmpty() || enterCost.getText().toString().isEmpty()){
                    Toast.makeText(EnterPersonDetailActivity.this,"Enter The Person Name and the Amount Paid !!", Toast.LENGTH_SHORT).show();
                }else{

                        // Check to see if the entered Amount is less than the total trip cost, Also check if the
                        //entered amount + other person's cost is less than or equal to the total trip cost.

                        // 1. Get the Cost entered by the person : enterCost
                        // 2. Get the total trip oost from the database.
                        // 3. Compare them
                        if(Double.parseDouble(enterCost.getText().toString()) > totalCost){
                            //Display a Dialog Box Stating the cost entered is greater than the total cost
                            alertDialog.setTitle("Amount Exceeded !!");
                            alertDialog.setMessage(" Trip Cost : " + totalCost + "\n Entered Amount " + enterCost.getText().toString());
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    enterCost.setText("");
                                }
                            });
                            alertDialog.show();
                        }else{
                            // Get the persons so far in the trip from the Databsse
                            // Sum up their costs
                            List<Person> personsList = tripDataSource.getPersonsInTrip(tripName);
                            double costPaidByAll = 0;
                            for (Person person : personsList){
                                costPaidByAll += person.getAmountPaid();
                            }
                            costPaidByAll += Double.parseDouble(enterCost.getText().toString());

                            if(costPaidByAll > totalCost){
                                // Display Dialog Box Stating cost entered is greater than total trip cost
                                alertDialog.setTitle("Amount Exceeded !!");
                                alertDialog.setMessage(" Trip Cost : " + totalCost + "\n Entered Amount " + enterCost.getText().toString()
                                                        + " \n Amount Entered by All: " + costPaidByAll);
                                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        enterCost.setText("");
                                    }
                                });
                                alertDialog.show();

                            }else{
                                saveToDb();

                                if(personAdded) {

                                    noOfPersons = noOfPersons - 1;
                                    Toast.makeText(EnterPersonDetailActivity.this, "Item Saved", Toast.LENGTH_SHORT).show();
                                    refreshDisplay();
                                }else{
                                    Toast.makeText(EnterPersonDetailActivity.this, "Person Name Already Entered Before", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }



                }

            }
        });

    }

    private class SearchPhoneAsyncTask extends AsyncTask<String, Void, HashMap<String, Object>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Fetching Contact Info");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected HashMap<String, Object> doInBackground(String... strings) {

            String personNamePassed = strings[0];
            HashMap<String, Object> personDetails = new HashMap<>(3, 1.0f);
            //Map<String, Object> personImage = new HashMap<>(1, 1.0f);
            //List<HashMap<String, Object>> personInfo = new ArrayList<>(2);
            InputStream inputStream;

            String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
            ContentResolver contentResolver = getContentResolver();
            Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
            String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
            Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
            String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
            String DATA = ContactsContract.CommonDataKinds.Email.DATA;
            //String photoUri = ContactsContract.CommonDataKinds.Photo.PHOTO_URI;

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

                    Uri my_contact_Uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, id);

                    inputStream =  ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), my_contact_Uri);
                    personDetails.put("image", inputStream);
                }
            }
            idCursor.close();

            return personDetails;
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> stringStringMap) {
            super.onPostExecute(stringStringMap);
            progressDialog.hide();
            phoneNumber = (String) stringStringMap.get("phoneNumber");
            email = (String) stringStringMap.get("email");
            photoStream = (InputStream)stringStringMap.get("image");
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
            editText.setText("");
        }
        if(email != null){
            editText = (EditText)findViewById(R.id.enter_person_email);
            editText.setText(email);
        }else{
            editText = (EditText)findViewById(R.id.enter_person_email);
            editText.setText("");
        }
        if(photoStream != null){
            ImageView photo = (ImageView)findViewById(R.id.imageView_person);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(photoStream);
            bitmap = BitmapFactory.decodeStream(bufferedInputStream);
            photo.setImageBitmap(bitmap);
        }else{
            ImageView photo = (ImageView)findViewById(R.id.imageView_person);
            photo.setImageBitmap(null);
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
        person.setBalance(amount - amountPerHead);
        if(photoStream != null){
            person.setPersonImage(bitmap);
        }else{
            try {
                InputStream ims = getAssets().open("person_image.jpg");
                BufferedInputStream bufferedInputStream = new BufferedInputStream(ims);
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                person.setPersonImage(bmp);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        personAdded =  tripDataSource.addPerson(person);
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

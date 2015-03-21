package com.aloknath.splitmytrip.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.aloknath.splitmytrip.Adapters.ItemsAdapter;
import com.aloknath.splitmytrip.Adapters.PersonsAdapter;
import com.aloknath.splitmytrip.Database.TripDataSource;
import com.aloknath.splitmytrip.Objects.Person;
import com.aloknath.splitmytrip.Objects.TripItem;
import com.aloknath.splitmytrip.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.aloknath.splitmytrip.Calculator.BalanceCalculator.calculate;

/**
 * Created by ALOKNATH on 3/20/2015.
 */
public class TripAmountOwedActivity extends Activity {

    private String tripName;
    private TripDataSource tripDataSource;
    private List<Person> personList;
    private List<TripItem> itemList;
    private PersonsAdapter personsAdapter;
    private ItemsAdapter itemsAdapter;
    private ListView personListView;
    private ListView itemsListView;
    private HashMap<Integer, Bitmap> imagesPassed = new HashMap<>();
    private Button sendSms;
    private Button sendEmail;
    private List<HashMap<String, Object>> result;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_send_layout);
        InputStream ims;

        try
        {
            View view = getWindow().getDecorView().findViewById(android.R.id.content);
            ims = getAssets().open("send_amount.jpg");
            Drawable d = Drawable.createFromStream(ims, null);
            view.setBackground(d);

        }catch(IOException ex)
        {
            return;
        }


        try {
            ims = getAssets().open("button_sms.png");
            Drawable d = Drawable.createFromStream(ims, null);

            sendSms = (Button)findViewById(R.id.button_sms);
            sendSms.setBackground(d);

            ims = getAssets().open("button_email.png");
            d = Drawable.createFromStream(ims, null);

            sendEmail = (Button)findViewById(R.id.button_email);
            sendEmail.setBackground(d);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Bundle bundle = getIntent().getExtras();
        tripName = bundle.getString("tripName");

        TextView trip_name = (TextView)findViewById(R.id.trip_name);
        trip_name.setText(tripName);

        // Open the Database and fetch the tripItems and persons from the Database
        tripDataSource = new TripDataSource(this);
        tripDataSource.open();

        personList = tripDataSource.getPersonsInTrip(tripName);

        result = calculate(personList, 0);

        // Using String Builder, construct the body of the SmS to be sent
        // First get the amount owed by each person by calling in the calculator function

        StringBuilder messageBuild = new StringBuilder();
        String messageToSend = "Hello Friends !! \n \n Below is the Details of the Amount Owed: \n \n";
        messageBuild.append(messageToSend);

        if(result.size() > 0) {
            Person sender;
            Person recipient;
            double amount;

            Iterator iterator = result.iterator();

            while (iterator.hasNext()) {

                HashMap<String, Object> mapReturned = (HashMap<String, Object>) iterator.next();
                sender = (Person) mapReturned.get("from");
                recipient = (Person) mapReturned.get("to");
                amount = (Double) mapReturned.get("amount");

                messageToSend = sender.getName() + " has to give " + String.valueOf(amount) + " to " + recipient.getName() + "\n \n";
                messageBuild.append(messageToSend);

            }
        }else{
            Log.i("The Map Returned: "," is Null");
        }

        messageBuild.append(" Have a Blessed Day !!!");
        messageToSend = messageBuild.toString();

        // Using String Builder get the phone numbers separated by semicolon
        // The Phone numbers can be got from the personsList
        StringBuilder numberBuilder = new StringBuilder();
        String[] emailList;
        final List<String> myList = new ArrayList<>();

        String delim = "";
        for(Person person: personList){
            numberBuilder.append(delim).append(person.getContactNo());
            delim = ";";
        }

        String numbers = numberBuilder.toString();

        for(Person person: personList){
            Log.i(" Person Details:", person.toString());

            if(person.getEmail() != null) {
                myList.add(person.getEmail());

            }
        }

        emailList = myList.toArray(new String[myList.size()]);


        itemList = tripDataSource.getTripItems(tripName);

        personsAdapter = new PersonsAdapter(this, R.layout.person_list_display , personList);

        imagesPassed = getImages(itemList);
        itemsAdapter = new ItemsAdapter(this, R.layout.trip_items_list_display, itemList, imagesPassed );

        personListView = (ListView)findViewById(R.id.listView_persons);
        personListView.setAdapter(personsAdapter);

        itemsListView = (ListView)findViewById(R.id.listView_items);
        itemsListView.setAdapter(itemsAdapter);



        final String finalMessageToSend = messageToSend;
        final String finalNumbers = numbers;

        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.putExtra("address", finalNumbers);
                i.putExtra("sms_body", finalMessageToSend);
                i.setType("vnd.android-dir/mms-sms");
                startActivity(i);
            }
        });

        final String[] finalEmailList = emailList;

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent emailLauncher = new Intent(Intent.ACTION_SEND_MULTIPLE);
                emailLauncher.setType("message/rfc822");
                emailLauncher.putExtra(Intent.EXTRA_EMAIL, finalEmailList);
                emailLauncher.putExtra(Intent.EXTRA_SUBJECT, tripName + " Trip Amount Split Up");
                emailLauncher.putExtra(Intent.EXTRA_TEXT, finalMessageToSend);
                try{
                    startActivity(emailLauncher);
                }catch(ActivityNotFoundException e){

                }

            }
        });
    }

    private HashMap<Integer, Bitmap> getImages(List<TripItem> itemList) {
        int i =0;

        for (TripItem item: itemList){
            Bitmap bitmap = setItemImage(item.getItemName());
            imagesPassed.put(i, bitmap);
            i++;
        }

        return imagesPassed;
    }

    private Bitmap setItemImage(String imageName) {

        InputStream inputStream = null;
        Bitmap imageReturned = null;

        if(imageName.equalsIgnoreCase("travel")){

            try {
                inputStream = getAssets().open("travel.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("adventure")){

            try {
                inputStream = getAssets().open("adventure.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("hiking")||imageName.equalsIgnoreCase("trekking")){

            try {
                inputStream = getAssets().open("hiking.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("food")){

            try {
                inputStream = getAssets().open("food.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("water")){

            try {
                inputStream = getAssets().open("water.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("hotel")||imageName.equalsIgnoreCase("motel")){

            try {
                inputStream = getAssets().open("hotel.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);


        }else if(imageName.equalsIgnoreCase("bar")||imageName.equalsIgnoreCase("drinks")||imageName.equalsIgnoreCase("disco")||imageName.equalsIgnoreCase("pub")||imageName.equalsIgnoreCase("drink")){

            try {
                inputStream = getAssets().open("bar.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("fuel")||imageName.equalsIgnoreCase("car")||imageName.equalsIgnoreCase("gas")){

            try {
                inputStream = getAssets().open("fuel.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("museum")||imageName.equalsIgnoreCase("gallery")){

            try {
                inputStream = getAssets().open("museum.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("aquarium")){

            try {
                inputStream = getAssets().open("aquarium.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("park")){

            try {
                inputStream = getAssets().open("park.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("train")){

            try {
                inputStream = getAssets().open("train.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("bus")){

            try {
                inputStream = getAssets().open("bus.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("shopping")){

            try {
                inputStream = getAssets().open("shopping.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("beach")){

            try {
                inputStream = getAssets().open("beach.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);
        }

        return imageReturned;
    }

}

package com.aloknath.splitmytrip.Activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_send_layout);

        Bundle bundle = getIntent().getExtras();
        tripName = bundle.getString("tripName");

        TextView trip_name = (TextView)findViewById(R.id.trip_name);
        trip_name.setText(tripName);

        // Open the Database and fetch the tripItems and persons from the Database
        tripDataSource = new TripDataSource(this);
        tripDataSource.open();

        personList = tripDataSource.getPersonsInTrip(tripName);

        itemList = tripDataSource.getTripItems(tripName);

        personsAdapter = new PersonsAdapter(this, R.layout.person_list_display , personList);

        imagesPassed = getImages(itemList);
        itemsAdapter = new ItemsAdapter(this, R.layout.trip_items_list_display, itemList, imagesPassed );

        personListView = (ListView)findViewById(R.id.listView_persons);
        personListView.setAdapter(personsAdapter);

        itemsListView = (ListView)findViewById(R.id.listView_items);
        itemsListView.setAdapter(itemsAdapter);
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

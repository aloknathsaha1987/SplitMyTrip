package com.aloknath.splitmytrip.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.aloknath.splitmytrip.Adapters.ExpandableBaseAdapter;
import com.aloknath.splitmytrip.Database.TripDataSource;
import com.aloknath.splitmytrip.Fragments.ParentViewPagerFragment;
import com.aloknath.splitmytrip.Objects.Person;
import com.aloknath.splitmytrip.Objects.TripItem;
import com.aloknath.splitmytrip.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.aloknath.splitmytrip.Calculator.BalanceCalculator.calculate;

/**
 * Created by ALOKNATH on 3/11/2015.
 */
public class TripActivity extends ActionBarActivity {

    private ExpandableListView expListView;
    private List<TripItem> tripItems;
    private TripItemsPersons tripItemsPersons;
    private List<Person> persons;
    private TripDataSource tripDataSource;
    private ExpandableBaseAdapter listAdapter;
    private List<HashMap<String, Object>> result;
    private HashMap<Integer, Bitmap> itemImages = new HashMap<>();
    private Toolbar toolbar;
    private String tripName;
    public static final int EDITOR_ACTIVITY_REQUEST = 1001;
    public static final int ITEM_ADDED_TO_TRIP = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_expandable_layout);
        expListView = (ExpandableListView)findViewById(R.id.expandable_listView);

        tripDataSource = new TripDataSource(this);
        tripDataSource.open();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //tripItemsPersons = getTripItems((String)bundle.get("tripName"));
        tripName = (String)bundle.get("tripName");
        // Set The ToolBar
        toolbar = (Toolbar)findViewById(R.id.include);

        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8C000000")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle((String)bundle.get("tripName") + " Trip");

        refreshDisplay();

        listViewListner();

    }

    private Bitmap setItemImage(String imageName) {

        imageName = imageName.trim();
        InputStream inputStream = null;
        Bitmap imageReturned;

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

        }else if(imageName.equalsIgnoreCase("flight")||imageName.equalsIgnoreCase("plane")||imageName.equalsIgnoreCase("airplane")||imageName.equalsIgnoreCase("aeroplane")){

            try {
                inputStream = getAssets().open("flight.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }
        else if(imageName.equalsIgnoreCase("water")){

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
        }else{
            try {
                inputStream = getAssets().open("default_item.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);
        }


        return imageReturned;
    }


    @Override
    public void onBackPressed()
    {
        finish();
    }

    private void listViewListner() {
        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if(groupPosition == 0) {
                    startActivity(startIntent(groupPosition, childPosition, tripItemsPersons.getItems().get(childPosition).getItemName(), tripItemsPersons.getItems().size()));
                   //Toast.makeText(TripActivity.this, " The Child Clicked :" + tripItemsPersons.getItems().get(childPosition).getItemName(), Toast.LENGTH_SHORT).show();

                }else{
                    startActivity(startIntent(groupPosition,childPosition, tripItemsPersons.getPersons().get(childPosition).getName(), tripItemsPersons.getPersons().size()));
                   // Toast.makeText(TripActivity.this, " The Child Clicked :" + tripItemsPersons.getPersons().get(childPosition).getName(), Toast.LENGTH_SHORT).show();

                }

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_person_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        Bundle b = new Bundle();
        switch (item.getItemId()) {
            case R.id.add_person:
                intent = new Intent(TripActivity.this, AddPersonToTripActivity.class);
                b.putString("Trip_title", tripName);
                b.putInt("Trip_no_of_persons", 1);
                intent.putExtras(b);
                startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
                break;

            case R.id.add_item:
                intent = new Intent(TripActivity.this, AddItemActivity.class);
                b.putString("Trip_title", tripName);
                intent.putExtras(b);
                startActivityForResult(intent, ITEM_ADDED_TO_TRIP);
                break;

            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
            refreshDisplay();
        }else if (requestCode == ITEM_ADDED_TO_TRIP && resultCode == RESULT_OK){
            refreshDisplay();
        }
    }

    private void refreshDisplay() {

        persons = tripDataSource.getPersonsInTrip(tripName);

        if(persons.size() > 0) {

            //Log.i("The Map Returned: ", "Sender: " + persons.get(0).getName());
            result = calculate(persons, 0);

            if(result.size() > 0) {
                Person sender;
                Person recipient;
                double amount;

                // Toast.makeText(TripActivity.this, "Sender: " + sender.getName() + " gave " + String.valueOf(amount) + " to " + recipient.getName(), Toast.LENGTH_LONG).show();

                Iterator iterator = result.iterator();

                while (iterator.hasNext()) {

                    HashMap<String, Object> mapReturned = (HashMap<String, Object>) iterator.next();
                    sender = (Person) mapReturned.get("from");
                    recipient = (Person) mapReturned.get("to");
                    amount = (Double) mapReturned.get("amount");

                    Log.i("The Map Returned: ", sender.getName() + " has to give " + String.valueOf(amount) + " to " + recipient.getName());

                    //Toast.makeText(TripActivity.this, "Sender: " + sender.getName() + " gave " + String.valueOf(amount) + " to " + recipient.getName(), Toast.LENGTH_LONG).show();

                }
            }else{
                Log.i("The Map Returned: "," is Null");
            }
        }

        tripItemsPersons = getTripItems(tripName);

        listAdapter = new ExpandableBaseAdapter(this, tripItemsPersons, itemImages);

        expListView.setAdapter(listAdapter);

    }


    private Intent startIntent(int groupPosition,int childPosition, String name, int count) {

        Intent intent = new Intent(this, AttachFragmentActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("groupPosition", groupPosition);
        extras.putInt("childPosition", childPosition);
        extras.putString("name", name);
        extras.putInt("count", count);

        if(groupPosition == 0){
            extras.putSerializable("hashMap", tripItemsPersons.getItems());
        }else{
            extras.putSerializable("hashMap", tripItemsPersons.getPersons());
            extras.putSerializable("price_split", (java.io.Serializable) result);
        }
        extras.putString("FragmentTagKey", ParentViewPagerFragment.TAG);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }



    private TripItemsPersons getTripItems(String tripName) {

      tripItems = tripDataSource.getTripItems(tripName);
//      TripItem addItem = new TripItem( "Add Trip Item", tripName);
//      tripItems.add(addItem);
      HashMap<Integer, TripItem> tripItemList = new HashMap<>();
      int i =0;
      for(TripItem item: tripItems){
          tripItemList.put(i, item);
          Log.i("The Item Name: ", item.getItemName());
          Bitmap bitMap = setItemImage(item.getItemName());
          itemImages.put(i, bitMap);
          i++;
      }
      i=0;
      persons = tripDataSource.getPersonsInTrip(tripName);


      HashMap<Integer, Person> personsList = new HashMap<>();
      for(Person person: persons){
          personsList.put(i, person);
          i++;
      }
      TripItemsPersons itemsPersons = new TripItemsPersons(tripItemList, personsList );
      tripDataSource.close();
      return itemsPersons;

    }

    public static class TripItemsPersons {
        private final HashMap<Integer, TripItem> items;
        private final HashMap<Integer, Person> persons;


        public TripItemsPersons(HashMap<Integer, TripItem> items, HashMap<Integer, Person> persons) {
            this.items = items;
            this.persons = persons;
        }

        public HashMap<Integer, TripItem> getItems() {
            return items;
        }

        public HashMap<Integer, Person> getPersons() {
            return persons;
        }

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

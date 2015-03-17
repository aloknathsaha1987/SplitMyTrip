package com.aloknath.splitmytrip.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
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
public class TripActivity extends Activity {

    private ExpandableListView expListView;
    private List<TripItem> tripItems;
    private TripItemsPersons tripItemsPersons;
    private List<Person> persons;
    private TripDataSource tripDataSource;
    private ExpandableBaseAdapter listAdapter;
    private List<HashMap<String, Object>> result;
    private HashMap<Integer, Bitmap> itemImages = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_expandable_layout);
        expListView = (ExpandableListView)findViewById(R.id.expandable_listView);

        tripDataSource = new TripDataSource(this);
        tripDataSource.open();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        tripItemsPersons = getTripItems((String)bundle.get("tripName"));

        if(persons.size() > 0) {

            //Log.i("The Map Returned: ", "Sender: " + persons.get(0).getName());
            result = calculate(persons, 0);

            if(result.size() > 0) {
                HashMap<String, Object> mapReturned0 = result.get(0);
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


        listAdapter = new ExpandableBaseAdapter(this, tripItemsPersons, itemImages);

        expListView.setAdapter(listAdapter);

        listViewListner();

    }

    private Bitmap setItemImage(String imageName) {

        InputStream inputStream = null;
        Bitmap imageReturned = null;

        if(imageName.equals("Travel") || imageName.equals("travel")||imageName.equals("TRAVEL")){

            try {
                inputStream = getAssets().open("travel.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Adventure") || imageName.equals("adventure")||imageName.equals("ADVENTURE")){

            try {
                inputStream = getAssets().open("adventure.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Hiking") || imageName.equals("hiking")||imageName.equals("HIKING")||imageName.equals("Trekking")||imageName.equals("trekking")||imageName.equals("TREKKING")){

            try {
                inputStream = getAssets().open("hiking.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Food") || imageName.equals("food")||imageName.equals("FOOD")){

            try {
                inputStream = getAssets().open("food.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Water") || imageName.equals("water")||imageName.equals("WATER")){

            try {
                inputStream = getAssets().open("water.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Hotel") || imageName.equals("hotel")||imageName.equals("HOTEL")){

            try {
                inputStream = getAssets().open("hotel.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);


        }else if(imageName.equals("Bar") || imageName.equals("bar")||imageName.equals("BAR")||imageName.equals("Drinks")||imageName.equals("drinks")||imageName.equals("DRINKS")||imageName.equals("Disco")||imageName.equals("disco")||imageName.equals("disco")||imageName.equals("Pub")||imageName.equals("pub")||imageName.equals("PUB")||imageName.equals("Drink")||imageName.equals("drink")||imageName.equals("DRINK")){

            try {
                inputStream = getAssets().open("bar.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Fuel") || imageName.equals("fuel")||imageName.equals("FUEL")||imageName.equals("Car")||imageName.equals("car")||imageName.equals("CAR")||imageName.equals("Gas")||imageName.equals("gas")||imageName.equals("GAS")){

            try {
                inputStream = getAssets().open("fuel.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Museum") || imageName.equals("museum")||imageName.equals("Museum")||imageName.equals("Gallery")||imageName.equals("gallery")||imageName.equals("gallery")){

            try {
                inputStream = getAssets().open("museum.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Aquarium") || imageName.equals("aquarium")||imageName.equals("AQUARIUM")){

            try {
                inputStream = getAssets().open("aquarium.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Park") || imageName.equals("park")||imageName.equals("PARK")){

            try {
                inputStream = getAssets().open("park.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Train") || imageName.equals("train")||imageName.equals("TRAIN")){

            try {
                inputStream = getAssets().open("train.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Bus") || imageName.equals("bus")||imageName.equals("BUS")){

            try {
                inputStream = getAssets().open("bus.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Shopping") || imageName.equals("shopping")||imageName.equals("SHOPPING")){

            try {
                inputStream = getAssets().open("shopping.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Beach") || imageName.equals("beach")||imageName.equals("BEACH")){

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

    public class TripItemsPersons {
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

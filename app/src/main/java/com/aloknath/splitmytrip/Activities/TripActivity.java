package com.aloknath.splitmytrip.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.aloknath.splitmytrip.Adapters.ExpandableBaseAdapter;
import com.aloknath.splitmytrip.Database.TripDataSource;
import com.aloknath.splitmytrip.Objects.Person;
import com.aloknath.splitmytrip.Objects.TripItem;
import com.aloknath.splitmytrip.R;

import java.util.ArrayList;
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
            List<HashMap<String, Object>> result;
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


        listAdapter = new ExpandableBaseAdapter(this, tripItemsPersons);

        expListView.setAdapter(listAdapter);

        listViewListner();

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

                return false;
            }
        });
    }

    private TripItemsPersons getTripItems(String tripName) {



      tripItems = tripDataSource.getTripItems(tripName);
      HashMap<Integer, TripItem> tripItemList = new HashMap<>();
      int i =0;
      for(TripItem item: tripItems){
          tripItemList.put(i, item);
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

//        public void setItems(HashMap<Integer, TripItem> items) {
//            this.items = items;
//        }

        public HashMap<Integer, Person> getPersons() {
            return persons;
        }

//        public void setPersons(HashMap<Integer, Person> persons) {
//            this.persons = persons;
//        }
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

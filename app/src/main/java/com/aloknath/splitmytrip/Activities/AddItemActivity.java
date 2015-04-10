package com.aloknath.splitmytrip.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aloknath.splitmytrip.Database.TripDataSource;
import com.aloknath.splitmytrip.Objects.Person;
import com.aloknath.splitmytrip.Objects.Trip;
import com.aloknath.splitmytrip.Objects.TripItem;
import com.aloknath.splitmytrip.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALOKNATH on 4/10/2015.
 */
public class AddItemActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private EditText itemName;
    private EditText itemCost;
    private TripDataSource tripDataSource;
    private Button saveItem;
    private List<Person> personList = new ArrayList<>();
    private final int[] i = {0};
    private final List<EditText> allEds = new ArrayList<>();
    private final double[] totalcost = {0.0};
    private List<Person> persons_paid_list = new ArrayList<>();
    private String tripName;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_to_trip);

        progressDialog = new ProgressDialog(AddItemActivity.this);

        tripDataSource = new TripDataSource(this);
        tripDataSource.open();

        Bundle b = getIntent().getExtras();

        personList = tripDataSource.getPersonsInTrip(b.getString("Trip_title"));
        tripName = b.getString("Trip_title");

        toolbar = (Toolbar)findViewById(R.id.include);

        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8C000000")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add A New Item");

        itemName = (EditText)findViewById(R.id.enter_trip_item);

        itemCost = (EditText)findViewById(R.id.enter_item_cost);

        saveItem = (Button)findViewById(R.id.button_update_item_cost);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearlayout);

        TextView textView2;
        LinearLayout.LayoutParams layoutParams;
        EditText editText;

        for(Person person: personList){

            textView2 = new TextView(this);
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.TOP;
            layoutParams.setMargins(75, 10, 10, 10); // (left, top, right, bottom)
            textView2.setLayoutParams(layoutParams);
            textView2.setText(person.getName());
            textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textView2.setTextColor(Color.BLACK);
            linearLayout.addView(textView2);

            editText = new EditText(this);
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.TOP;
            layoutParams.setMargins(75, 10, 10, 10);
            editText.setLayoutParams(layoutParams);
            editText.setHint("Amount Paid");
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            editText.setBackgroundColor(Color.CYAN);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setId(i[0]);
            linearLayout.addView(editText);
            allEds.add(editText);
            i[0]++;
        }

        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1. Check if the Item Name and Cost was Entered Correctly
                if(itemName.getText().toString().isEmpty() || itemCost.getText().toString().isEmpty()){
                    Toast.makeText(AddItemActivity.this, "Please Enter The Item Name and Item Cost", Toast.LENGTH_SHORT).show();
                }else{
                    // The Item Cost and Name was entered
                    // Check if the total cost entered by all people is equal to the item cost
                    TripItem newItem = new TripItem(itemName.getText().toString(), tripName);
                    newItem.setItemCost(Double.parseDouble(itemCost.getText().toString()));
                    totalcost[0] = 0.0;

                    for(i[0] =0; i[0] < allEds.size(); i[0]++){

                        if(allEds.get(i[0]).getText().toString().equals("")) {
                            Log.i("Empty String", " The Person Cost was empty");
                        }else{

                            Person person = personList.get(i[0]);
                            Log.i("The Person name: ", person.getName() + String.valueOf(person.getAmountPaid()));
                            if(allEds.get(i[0]).getText().toString().isEmpty()){
                                //Do Nothing
//                                        person.setAmountPaid(person.getAmountPaid() + 0.0);
                            }else{
                                person.setAmountPaid(person.getAmountPaid() + Double.parseDouble(allEds.get(i[0]).getText().toString()));
                            }

                            persons_paid_list.add(person);
                            totalcost[0] += Double.parseDouble(allEds.get(i[0]).getText().toString());
                        }
                    }

                    if(itemCost.getText().toString().equals("")){
                        Log.i("Empty String", " The Item Cost was empty");

                    }else{

                        if(Double.parseDouble(itemCost.getText().toString()) !=  totalcost[0]){

                            Log.i(" The Amount ", " Total entered Amount" +
                                    String.valueOf( totalcost[0]) + " Does not Match the updated Item's Cost ");


                        }else{
                            // Pass In The New Item And the persons paid list to the DB.
                            // newItem and persons_paid_list

                            UpdateDbAsyncTask asyncTask = new UpdateDbAsyncTask(newItem, persons_paid_list,Double.parseDouble(itemCost.getText().toString()) );
                            asyncTask.execute();

                        }

                    }

                }
            }
        });

    }

    private class UpdateDbAsyncTask extends AsyncTask<Void, Void, Void>{

        private final TripItem item;
        private final List<Person> personList;
        private final double cost;

        public UpdateDbAsyncTask(TripItem item, List<Person> persons, double cost){
            this.item = item;
            this.personList = persons;
            this.cost = cost;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Saving to Db");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            // Add the New Trip Item
            tripDataSource.addItenary(item);

            //Update the Trips Table regarding the cost per head and Save it

            Trip trip = tripDataSource.getTrip(item.getTripName());

            double amountPerHead;

            amountPerHead = trip.getTotalCost() + cost;
            amountPerHead = amountPerHead / trip.getNoOfPersons();

            trip.setTotalCost(trip.getTotalCost() + cost);
            trip.setAmountPerHead(amountPerHead);

            tripDataSource.updateTrip(trip);

            for(Person person: personList){

                Log.i("Person in persons List: " , person.getName());

                double amountOwed = amountPerHead - person.getAmountPaid();
                double amountToGet = 0;

                if(amountOwed < 0){
                    amountToGet = -(amountOwed);
                    amountOwed = 0;
                }
                amountToGet = Math.round(amountToGet*100)/100.0d;
                amountOwed = Math.round(amountOwed*100)/100.0d;
                person.setAmountOwed(amountOwed);
                person.setAmountToGet(amountToGet);
                person.setBalance(person.getAmountPaid() - amountPerHead);
                tripDataSource.updatePerson(person);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.hide();
            tripDataSource.close();
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
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

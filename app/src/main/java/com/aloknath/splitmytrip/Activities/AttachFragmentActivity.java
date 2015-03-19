package com.aloknath.splitmytrip.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.aloknath.splitmytrip.Database.TripDataSource;
import com.aloknath.splitmytrip.Fragments.ChildFragment;
import com.aloknath.splitmytrip.Fragments.ParentViewPagerFragment;
import com.aloknath.splitmytrip.Objects.Person;
import com.aloknath.splitmytrip.Objects.Trip;
import com.aloknath.splitmytrip.Objects.TripItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ALOKNATH on 3/14/2015.
 */
public class AttachFragmentActivity extends FragmentActivity implements ChildFragment.onChildEvent {

    private TripDataSource tripDataSource;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        setTitle(extras.getString("name"));
        String tag = extras.getString("FragmentTagKey");

        progressDialog = new ProgressDialog(AttachFragmentActivity.this);

        tripDataSource = new TripDataSource(this);
        tripDataSource.open();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);

        if(fragment == null){
            fragment = Fragment.instantiate(this, tag);
            fragment.setArguments(getIntent().getExtras());
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(android.R.id.content, fragment, tag);
            ft.commit();
        }

    }

    private void refreshDisplay(String tripName) {

        // Refresh the fragment, i.e recalculate result and replace the old fragment by a new one
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(ParentViewPagerFragment.TAG);

        if(fragment != null){
           // fragment = Fragment.instantiate(this, ParentViewPagerFragment.TAG);
            //fragment.setArguments(bundle);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.remove(fragment);
            //ft.replace(android.R.id.content, fragment, ParentViewPagerFragment.TAG);
            ft.commit();

            Intent intent = new Intent(AttachFragmentActivity.this, TripActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("tripName", tripName);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        //fragmentTransaction.replace(R.id.fragmentNewTrip, fragment_new_trip.newInstance("New Trip"));
    }
    @Override
    public void amountPaid(String tripName, String from, String to, double amount) {
        // Update the Tables Accordingly for the particular persons involved in the trip
        // Get the trip name from any one of the persons
        // Update the Person's fields and send the person's list to the database for updation
        // Recalculate the Balance owed by the particular person
        // person.setBalance(amount - amountPerHead );

        tripDataSource.open();
        Person sender = tripDataSource.getPersonDetails(from, tripName);
        Person recipient = tripDataSource.getPersonDetails(to, tripName);
        List<Person> persons = new ArrayList<>();

        //amountToGet = Math.round(amountToGet*100)/100.0d;

        sender.setAmountOwed(Math.round((sender.getAmountOwed() - amount)*100)/100.0d);
        sender.setAmountPaid(Math.round((sender.getAmountPaid() + amount)*100)/100.0d);
        sender.setBalance(Math.round((sender.getBalance() + amount)*100)/100.0d);
        recipient.setAmountToGet(Math.round((recipient.getAmountToGet() - amount)*100)/100.0d);
        recipient.setBalance(Math.round((recipient.getBalance() - amount)*100)/100.0d);
        recipient.setAmountPaid(Math.round((recipient.getAmountPaid() - amount)*100)/100.0d);

        persons.add(sender);
        persons.add(recipient);
        tripDataSource.updatePersonsPaid(persons);
        refreshDisplay(tripName);
        tripDataSource.close();

    }

    @Override
    public void editPerson(Person person, double newAmountPaid, List<HashMap<String, Object>> result) {

        //Pass the list of sorted data to this function and update the person/ persons associated with the data

        /*
           I have got the list passed into this function

           Now, the person's updated object is at the end of the code.

           get that,
           From the list, check if the particular person had to pay to person/ persons
           If yes, then deduct the amount form the person/persons and update the person's list.

         */

        tripDataSource.open();
        Trip personTrip = tripDataSource.getTrip(person.getTripName());
        Toast.makeText(this, " TripData Source is Open" + personTrip.getTripName(), Toast.LENGTH_SHORT).show();

        double oldAmountPaid = person.getAmountPaid();
        person.setAmountPaid(newAmountPaid);

        person.setBalance((newAmountPaid) - personTrip.getAmountPerHead());
        Log.i("Person's New Balance: ", String.valueOf(person.getBalance()));
        Log.i("Amount per head: ", String.valueOf(personTrip.getAmountPerHead()));
        if(person.getAmountOwed()>0) {
            if((person.getAmountOwed() - (newAmountPaid- oldAmountPaid))>0) {
                person.setAmountOwed(person.getAmountOwed() - (newAmountPaid- oldAmountPaid));
            }else{

                person.setAmountToGet((newAmountPaid- oldAmountPaid) - person.getAmountOwed() );
                person.setAmountOwed(0);
            }
        }

        Log.i(" The Loop is running: ", person.getName());

//        if(newAmountPaid > oldAmountPaid) {
//            person.setBalance((newAmountPaid) - personTrip.getAmountPerHead());
//            if(person.getAmountOwed()>0) {
//                if((person.getAmountOwed() - (newAmountPaid- oldAmountPaid))>0) {
//                    person.setAmountOwed(person.getAmountOwed() - (newAmountPaid- oldAmountPaid));
//                }else{
//
//                    person.setAmountToGet((newAmountPaid- oldAmountPaid) - person.getAmountOwed() );
//                    person.setAmountOwed(0);
//                }
//            }
//
//        }else{
//            person.setBalance((newAmountPaid) - personTrip.getAmountPerHead());
//
//            if(person.getAmountOwed()>0) {
//                person.setAmountOwed(person.getAmountOwed() + ( oldAmountPaid - newAmountPaid));
//
////                if((person.getAmountOwed() - ( oldAmountPaid - newAmountPaid))>0) {
////                    person.setAmountOwed(person.getAmountOwed() - ( oldAmountPaid - newAmountPaid));
////                }else{
////
////                    person.setAmountToGet((oldAmountPaid - newAmountPaid) - person.getAmountOwed() );
////                    person.setAmountOwed(0);
////                }
//            }
//        }

        tripDataSource.updatePerson(person);

        HashMap<String, Object> personMap = new HashMap<>();
        personMap.put("person", person);
        result.add(personMap);

        Log.i(" The tripDatasource is updated with the person: ", person.getName());

        UpdateDbAsyncTask asyncTask = new UpdateDbAsyncTask();
        asyncTask.execute(result);

    }

    private class UpdateDbAsyncTask extends AsyncTask<List<HashMap<String, Object>>, Void, Person>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Saving to Db");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        @Override
        protected Person doInBackground(List<HashMap<String, Object>>... lists) {

            Person sender;
            Person recipient;
            double amount;
            HashMap<String, Object> mapReturned = (HashMap<String, Object>) lists[0].get(lists[0].size() - 1);
            Person person = (Person)mapReturned.get("person");
            Log.i(" The Results Size: ", String.valueOf(lists[0].size()));
            lists[0].remove(lists[0].size() - 1);
            Log.i(" The Results Size after deletion: ", String.valueOf(lists[0].size()));
            Iterator iterator = lists[0].iterator();
            Log.i(" The Loop is running inside the Async Task: ", person.getName());

            while (iterator.hasNext()) {

                mapReturned = (HashMap<String, Object>) iterator.next();
                sender = (Person) mapReturned.get("from");
                Person temp = tripDataSource.getPersonDetails(sender.getName(), sender.getTripName());
                sender.setBalance(temp.getBalance());
                recipient = (Person) mapReturned.get("to");
                temp = tripDataSource.getPersonDetails(recipient.getName(), recipient.getTripName());
                recipient.setBalance(temp.getBalance());

                if(sender.getName().equals(person.getName())){

                    Log.i(" Come inside this loop: ", sender.getName());
                    Log.i("Sender Details: ", sender.toString());
                    Log.i("Recipient Details: ", recipient.toString());

                    if(sender.getAmountOwed() > (person.getAmountPaid() - sender.getAmountPaid())){
                        amount = (person.getAmountPaid() - sender.getAmountPaid());

                    }else{
                        amount = sender.getAmountOwed();
                    }
                    amount = Math.round(amount * 100) / 100.0d;
                    // just have to update the recipient
                    Log.i("The Amount generated", String.valueOf(amount));
                    Log.i("The Recipient Balance before modification: ", String.valueOf(recipient.getBalance()) + ":" + String.valueOf(amount));
                    recipient.setAmountToGet(Math.round((recipient.getAmountToGet() - amount)*100)/100.0d);
                    recipient.setBalance(Math.round((recipient.getBalance() - amount)*100)/100.0d);
                    Log.i("The recipient's Name and Balance", recipient.getName() + " :"+ String.valueOf(recipient.getBalance()));
                    recipient.setAmountPaid(Math.round((recipient.getAmountPaid() - amount)*100)/100.0d);

                    // Update the recipient in the Database
//                    tripDataSource.open();
                    tripDataSource.updatePerson(recipient);
                    Log.i(" Updation of the Db Done ", recipient.getName());

                }
                Log.i(" Stuck In the Iterator Loop","");
            }

            Log.i(" Coming out of the Iterator Loop ","");

            return person;
        }

        @Override
        protected void onPostExecute(Person person) {
            super.onPostExecute(person);
            progressDialog.hide();
            tripDataSource.close();
            refreshDisplay(person.getTripName());

        }
    }

    @Override
    public void editItem(TripItem item, double newItemAmount) {

        Trip itemTrip = tripDataSource.getTrip(item.getTripName());

        TripItem checkerTripItem = tripDataSource.getTripItem(item.getTripName(), item.getItemName());

        itemTrip.setTotalCost(itemTrip.getTotalCost() - checkerTripItem.getItemCost() + newItemAmount);
        itemTrip.setAmountPerHead(itemTrip.getTotalCost()/ itemTrip.getNoOfPersons());
        item.setItemCost(newItemAmount);


        tripDataSource.updateTrip(itemTrip);
        tripDataSource.updateTripItem(item);
        refreshDisplay(item.getTripName());
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

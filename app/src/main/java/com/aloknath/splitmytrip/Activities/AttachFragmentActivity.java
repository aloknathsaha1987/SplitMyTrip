package com.aloknath.splitmytrip.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        setTitle(extras.getString("name"));
        String tag = extras.getString("FragmentTagKey");

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

        Trip personTrip = tripDataSource.getTrip(person.getTripName());

        double oldAmountPaid = person.getAmountPaid();
        person.setAmountPaid(newAmountPaid);
        if(newAmountPaid > oldAmountPaid) {
            person.setBalance((newAmountPaid - oldAmountPaid) - personTrip.getAmountPerHead());
            if(person.getAmountOwed()>0) {
                if((person.getAmountOwed() - (newAmountPaid- oldAmountPaid))>0) {
                    person.setAmountOwed(person.getAmountOwed() - (newAmountPaid- oldAmountPaid));
                }else{

                    person.setAmountToGet((newAmountPaid- oldAmountPaid) - person.getAmountOwed() );
                    person.setAmountOwed(0);
                }
            }

        }else{
            person.setBalance((oldAmountPaid - newAmountPaid) - personTrip.getAmountPerHead());

            if(person.getAmountOwed()>0) {
                person.setAmountOwed(person.getAmountOwed() - ( oldAmountPaid - newAmountPaid));

//                if((person.getAmountOwed() - ( oldAmountPaid - newAmountPaid))>0) {
//                    person.setAmountOwed(person.getAmountOwed() - ( oldAmountPaid - newAmountPaid));
//                }else{
//
//                    person.setAmountToGet((oldAmountPaid - newAmountPaid) - person.getAmountOwed() );
//                    person.setAmountOwed(0);
//                }
            }
        }

        tripDataSource.updatePerson(person);

        Iterator iterator = result.iterator();
        Person sender;
        Person recipient;
        double amount;

        while (iterator.hasNext()) {

            HashMap<String, Object> mapReturned = (HashMap<String, Object>) iterator.next();
            sender = (Person) mapReturned.get("from");
            recipient = (Person) mapReturned.get("to");

            if(sender.getName().equals(person.getName())){

                if(sender.getAmountOwed() > (newAmountPaid - sender.getAmountPaid())){
                    amount = (newAmountPaid - sender.getAmountPaid());

                }else{
                    amount = sender.getAmountOwed();
                }
                amount = Math.round(amount * 100) / 100.0d;
                // just have to update the recipient
                recipient.setAmountToGet(Math.round((recipient.getAmountToGet() - amount)*100)/100.0d);
                recipient.setBalance(Math.round((recipient.getBalance() - amount)*100)/100.0d);
                recipient.setAmountPaid(Math.round((recipient.getAmountPaid() - amount)*100)/100.0d);

                // Update the recipient in the Database
                tripDataSource.updatePerson(recipient);

            }
        }

        refreshDisplay(person.getTripName());
        tripDataSource.close();
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

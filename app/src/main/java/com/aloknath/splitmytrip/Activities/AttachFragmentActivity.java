package com.aloknath.splitmytrip.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.aloknath.splitmytrip.Database.TripDataSource;
import com.aloknath.splitmytrip.Fragments.ChildFragment;
import com.aloknath.splitmytrip.Fragments.ParentViewPagerFragment;
import com.aloknath.splitmytrip.Objects.Person;

import java.util.ArrayList;
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

        sender.setAmountOwed(sender.getAmountOwed() - amount);
        sender.setAmountPaid(sender.getAmountPaid() + amount);
        sender.setBalance(sender.getBalance() + amount);
        recipient.setAmountToGet(recipient.getAmountToGet() - amount);
        recipient.setBalance(recipient.getBalance() - amount);

        persons.add(sender);
        persons.add(recipient);
        tripDataSource.updatePersonsPaid(persons);

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

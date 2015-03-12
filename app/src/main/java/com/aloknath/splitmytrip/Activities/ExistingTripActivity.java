package com.aloknath.splitmytrip.Activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aloknath.splitmytrip.Adapters.TripsAdapter;
import com.aloknath.splitmytrip.Database.TripDataSource;
import com.aloknath.splitmytrip.Objects.Trip;
import com.aloknath.splitmytrip.R;

import java.util.List;

/**
 * Created by ALOKNATH on 2/24/2015.
 */
public class ExistingTripActivity extends ListActivity {

    private List<Trip> tripList;
    private TripsAdapter adapter;
    private TripDataSource tripDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existing_trips_layout);

        // Get the trip Items from the DB

        tripDataSource = new TripDataSource(this);
        tripDataSource.open();

        tripList = tripDataSource.getTrips();

        tripDataSource.close();

        // Set the Custom Adapter
        if (tripList.size() > 0) {
            Trip trip = tripList.get(0);
            Toast.makeText(this, "Trip Name: " + trip.getTripName() + " No of Persons: " + String.valueOf(trip.getNoOfPersons()) + " Total Cost" + String.valueOf(trip.getTotalCost()), Toast.LENGTH_LONG).show();
            adapter = new TripsAdapter(this, R.layout.trips_list_display, tripList);
            setListAdapter(adapter);
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Trip tripSelected = tripList.get(position);
        Intent intent = new Intent(ExistingTripActivity.this, TripActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tripName", tripSelected.getTripName());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        finish();
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

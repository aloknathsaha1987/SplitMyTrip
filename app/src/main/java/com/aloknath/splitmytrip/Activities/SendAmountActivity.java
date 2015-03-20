package com.aloknath.splitmytrip.Activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.aloknath.splitmytrip.Adapters.TripsAdapter;
import com.aloknath.splitmytrip.Database.TripDataSource;
import com.aloknath.splitmytrip.Objects.Trip;
import com.aloknath.splitmytrip.R;

import java.util.List;

/**
 * Created by ALOKNATH on 2/24/2015.
 */
public class SendAmountActivity extends ListActivity {

    private List<Trip> tripList;
    private TripsAdapter adapter;
    private TripDataSource tripDataSource;
    private int currentNoteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existing_trips_layout);

        // Get the trip Items from the DB
        tripDataSource = new TripDataSource(this);
        tripDataSource.open();
        refreshDisplay();
        tripDataSource.close();

    }

    public void refreshDisplay(){

        tripList = tripDataSource.getTrips();
        // Set the Custom Adapter
        if (tripList.size() > 0) {
            adapter = new TripsAdapter(this, R.layout.trips_list_display, tripList);
            setListAdapter(adapter);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Trip tripSelected = tripList.get(position);
        Intent intent = new Intent(SendAmountActivity.this, TripAmountOwedActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tripName", tripSelected.getTripName());
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

}

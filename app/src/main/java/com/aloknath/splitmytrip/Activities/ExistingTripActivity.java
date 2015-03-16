package com.aloknath.splitmytrip.Activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private int currentNoteId;
    public static final int MENU_DELETE_ID = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existing_trips_layout);

        // For Context Menu
        registerForContextMenu(getListView());

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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        currentNoteId = (int) info.id;
        menu.add(0, MENU_DELETE_ID, 0, "DELETE TRIP");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_DELETE_ID) {
            Trip trip = tripList.get(currentNoteId);
            tripDataSource.open();
            tripDataSource.removeTrip(trip);
            refreshDisplay();
            tripDataSource.close();
        }
        return true;
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

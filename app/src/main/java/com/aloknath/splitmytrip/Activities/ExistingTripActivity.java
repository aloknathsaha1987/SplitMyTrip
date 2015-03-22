package com.aloknath.splitmytrip.Activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existing_trips_layout);
        alertDialog = new AlertDialog.Builder(this);

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

            // Set Up an Alert Dialog Box with OK and a Cancel Button
            alertDialog.setTitle(" Confirm Deletion !!");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Trip trip = tripList.get(currentNoteId);
                    tripDataSource.open();
                    tripDataSource.removeTrip(trip);
                    tripDataSource.close();
                    tripList.clear();
                    refreshDisplay();
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    refreshDisplay();
                }
            });
            alertDialog.show();

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
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

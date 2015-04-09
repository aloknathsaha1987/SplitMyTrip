package com.aloknath.splitmytrip.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.aloknath.splitmytrip.Fragments.Fragment_Billing;
import com.aloknath.splitmytrip.Fragments.Fragment_Existing_Trip;
import com.aloknath.splitmytrip.Fragments.Fragment_Google_Maps;
import com.aloknath.splitmytrip.Fragments.Fragment_New_Trip;
import com.aloknath.splitmytrip.GoogleLicense.GPSLicenseActivity;
import com.aloknath.splitmytrip.R;

public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.include);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Split My Trip");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8C000000")));

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment_New_Trip fragment_new_trip = new Fragment_New_Trip();
        fragmentTransaction.replace(R.id.fragmentNewTrip, fragment_new_trip.newInstance("New Trip"));
        Fragment_Existing_Trip fragment_existing_trip = new Fragment_Existing_Trip();
        fragmentTransaction.replace(R.id.fragmentExistingTrip, fragment_existing_trip.newInstance("Existing Trip"));
        Fragment_Billing fragment_billing = new Fragment_Billing();
        fragmentTransaction.replace(R.id.fragmentBilling, fragment_billing.newInstance("Send Amount"));
        Fragment_Google_Maps fragment_google_maps = new Fragment_Google_Maps();
        fragmentTransaction.replace(R.id.fragmentgoogleMaps, fragment_google_maps.newInstance("Google Maps"));
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.google_play_license) {
            Intent intent = new Intent(MainActivity.this, GPSLicenseActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed()
    {
        finish();
    }

}

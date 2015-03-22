package com.aloknath.splitmytrip.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.aloknath.splitmytrip.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALOKNATH on 2/24/2015.
 */

public class GoogleMapsActivity extends ActionBarActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

    GoogleMap mMap;
    LocationClient mLocationClient;

    private double latitude;
    private double longitude;
    private boolean mShowMap;
    private static final float DEFAULTZOOM = 15;
    private static final int GPS_ERRORDIALOG_REQUEST = 9001;
    private ArrayList<Marker> markers = new ArrayList<Marker>();
    private Button goToLocation;
    private EditText destination;
    private String enteredDestination;
    private Toolbar toolbar;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (isOnline()) {
            if (servicesOK()) {
                setContentView(R.layout.google_map);

                // Set The ToolBar
                toolbar = (Toolbar)findViewById(R.id.include);
                setSupportActionBar(toolbar);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8C000000")));
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Google Maps");



                InputStream ims;
                try {
                    ims = getAssets().open("button_destination.png");
                    Drawable d = Drawable.createFromStream(ims, null);
                    goToLocation = (Button) findViewById(R.id.button_destination);
                    goToLocation.setBackground(d);

                }catch (IOException e) {
                    e.printStackTrace();
                }

                destination = (EditText)findViewById(R.id.enter_destination);
                goToLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        enteredDestination = destination.getText().toString();
                        if(enteredDestination.isEmpty()){
                            Toast.makeText(GoogleMapsActivity.this, " Please Enter The Destination", Toast.LENGTH_SHORT).show();
                        }else{
                            sendActionToIntent(enteredDestination);
                        }
                    }
                });

                if (initMap()) {

                    LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        mLocationClient = new LocationClient(GoogleMapsActivity.this, GoogleMapsActivity.this, GoogleMapsActivity.this);
                        mLocationClient.connect();
                        mShowMap = true;
                    }else{
                        Toast.makeText(this, "Location Manager Not Available", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "Map not available!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }

    }

    private void sendActionToIntent(String location) {
        StringBuilder uri = new StringBuilder("geo:");
        uri.append(latitude);
        uri.append(",");
        uri.append(longitude);
        uri.append("?z=10");
        uri.append("&q=" + URLEncoder.encode(location));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString()));
        startActivity(intent);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean servicesOK() {
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        }
        else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORDIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this, "Can't connect to Google Play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean initMap() {
        if(mMap == null){
            MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
            mMap = mapFragment.getMap();
        }
        return (mMap != null);
    }



    @Override
    public void onBackPressed()
    {
        finish();
    }

    @Override
    public void onConnected(Bundle bundle) {

        Location mLocation = mLocationClient.getLastLocation();
        if(mLocation == null){
            Toast.makeText(this, "My Location is not available", Toast.LENGTH_SHORT).show();
        }else {

            try {
                displayMyLocation();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this,"Disconnected from the location services", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = "Location" + location.getLatitude() + "," + location.getLongitude();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"Connection the location services Failed", Toast.LENGTH_SHORT).show();
    }


    private void displayMyLocation() throws IOException {

        if(mShowMap){
            if(mLocationClient.isConnected()) {
                Location mLocation = mLocationClient.getLastLocation();
                if (mLocation == null) {
                    Toast.makeText(this, "My Location is not available", Toast.LENGTH_LONG).show();
                } else {
                    gotoCurrentLocation();
                    Toast.makeText(this, "I'm Here", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "LocationClient is Not Connected", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void gotoCurrentLocation() throws IOException {

        Location mLocation = mLocationClient.getLastLocation();
        if(mLocation == null){
            Toast.makeText(this, "My Location is not available", Toast.LENGTH_SHORT).show();
        }else{
            LatLng latLng = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULTZOOM);
            mMap.animateCamera(cameraUpdate);
            Geocoder gc = new Geocoder(this);
            List<Address> list = gc.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(),1);
            Address add = list.get(0);
            String locality = "This is Me";
            String country = add.getCountryName();
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
            setMarker(country, locality, mLocation.getLatitude(),mLocation.getLongitude());
        }
    }

    public void setMarker(String country, String locality, double lat, double lng) {
        if(markers.size() == 2){
            removeEverything();
        }
        MarkerOptions markerOptions = new MarkerOptions()
                .title(locality)
                .position(new LatLng(lat, lng))
                .anchor(.5f, .5f)
                .icon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_CYAN
                ))
                .draggable(true);
        if (country.length() > 0) {
            markerOptions.snippet(country);
        }
        markers.add(mMap.addMarker(markerOptions));
    }

    private void removeEverything() {

        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.global, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return false;
    }

}

package com.aloknath.splitmytrip.GoogleLicense;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.aloknath.splitmytrip.R;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by ALOKNATH on 2/7/2015.
 */
public class GPSLicenseActivity extends ActionBarActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_license);

        toolbar = (Toolbar)findViewById(R.id.include);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8C000000")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Google Play License");

        String license = GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this);
        TextView tv = (TextView)findViewById(R.id.gps_license);
        if(license != null){
            tv.setText(license);
        }else{
            tv.setText("Google Play Services is not Installed on this device.");
        }
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

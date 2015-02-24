package com.aloknath.splitmytrip.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.aloknath.splitmytrip.R;

/**
 * Created by ALOKNATH on 2/24/2015.
 */
public class GoogleMapsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_layout);
        TextView textView = (TextView)findViewById(R.id.textView1);
        textView.setText("Google Maps Activity");
    }
}

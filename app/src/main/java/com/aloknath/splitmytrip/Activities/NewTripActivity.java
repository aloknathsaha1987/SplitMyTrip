package com.aloknath.splitmytrip.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aloknath.splitmytrip.Database.TripDataSource;
import com.aloknath.splitmytrip.Objects.Trip;
import com.aloknath.splitmytrip.R;

/**
 * Created by ALOKNATH on 2/24/2015.
 */
public class NewTripActivity extends Activity {


    private Button cancel;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_location);

        cancel = (Button)findViewById(R.id.button_cancel);
        next = (Button)findViewById(R.id.button_next);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewTripActivity.this, EnterItemActivity.class);
                Bundle bundle = new Bundle();

                EditText textView = (EditText)findViewById(R.id.edit_trip_location);
                bundle.putString("Trip_title", textView.getText().toString());

                textView = (EditText)findViewById(R.id.edit_trip_no_persons);
                bundle.putInt("Trip_no_of_persons", Integer.parseInt(textView.getText().toString()));

                intent.putExtras(bundle);

                startActivity(intent);

               // saveToDb();
            }
        });

    }

}

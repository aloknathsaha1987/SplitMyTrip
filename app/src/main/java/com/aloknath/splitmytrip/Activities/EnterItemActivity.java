package com.aloknath.splitmytrip.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aloknath.splitmytrip.Database.TripDataSource;
import com.aloknath.splitmytrip.Objects.TripItem;
import com.aloknath.splitmytrip.R;

/**
 * Created by ALOKNATH on 2/24/2015.
 */
public class EnterItemActivity extends Activity {

    Button cancel;
    Button next;
    Button save_enter;
    private Bundle bundle;
    private Intent intent;
    private TripDataSource tripDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_item);

        tripDataSource = new TripDataSource(this);
        tripDataSource.open();

        intent = getIntent();
        bundle = intent.getExtras();

        cancel = (Button)findViewById(R.id.button_cancel);
        next = (Button)findViewById(R.id.button_next);
        save_enter = (Button)findViewById(R.id.button_save_enter_new);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDb();
                Toast.makeText(EnterItemActivity.this, "Item Saved", Toast.LENGTH_SHORT).show();
                intent = new Intent(EnterItemActivity.this, EnterPersonDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        save_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDb();
                Toast.makeText(EnterItemActivity.this, "Item Saved", Toast.LENGTH_SHORT).show();
                refreshDisplay();
            }
        });

    }

    private void refreshDisplay() {

        intent = new Intent(EnterItemActivity.this, EnterItemActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private void saveToDb() {

        String itemName;
        double itemCost;
        String tripName;

        tripName = bundle.getString("Trip_title");

        EditText editText = (EditText)findViewById(R.id.enter_trip_item);
        itemName = editText.getText().toString();

        editText = (EditText)findViewById(R.id.edit_trip_item_cost);
        itemCost = Double.parseDouble(editText.getText().toString());

        TripItem tripItem = new TripItem(itemName, tripName);

        tripItem.setItemCost(itemCost);

        tripDataSource.addItenary(tripItem);
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

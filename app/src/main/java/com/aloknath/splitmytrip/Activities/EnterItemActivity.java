package com.aloknath.splitmytrip.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aloknath.splitmytrip.Database.TripDataSource;
import com.aloknath.splitmytrip.Fragments.KeyBoardFragment;
import com.aloknath.splitmytrip.Objects.Trip;
import com.aloknath.splitmytrip.Objects.TripItem;
import com.aloknath.splitmytrip.R;

/**
 * Created by ALOKNATH on 2/24/2015.
 */
public class EnterItemActivity extends FragmentActivity implements KeyBoardFragment.onKeyBoardEvent{

    Button cancel;
    Button next;
    Button save_enter;
    private Bundle bundle;
    private Intent intent;
    private TripDataSource tripDataSource;
    private EditText editText;
    private KeyBoardFragment keyboard_fragment;

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

        editText = (EditText)findViewById(R.id.edit_trip_item_cost);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                if(keyboard_fragment==null)
                {
                    keyboard_fragment=KeyBoardFragment.newInstance(editText.getText().toString());
                    getSupportFragmentManager().beginTransaction().add(R.id.Key_board_container, keyboard_fragment).commit();

                }
                else
                {
                    if(keyboard_fragment.isVisible())
                        getSupportFragmentManager().beginTransaction().hide(keyboard_fragment).commit();
                    else
                    {
                        keyboard_fragment=KeyBoardFragment.newInstance(editText.getText().toString());
                        getSupportFragmentManager().beginTransaction().add(R.id.Key_board_container, keyboard_fragment).commit();
                    }
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDb();
                Toast.makeText(EnterItemActivity.this, "Item Saved", Toast.LENGTH_SHORT).show();
                intent = new Intent(EnterItemActivity.this, EnterPersonDetailActivity.class);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

        // Also Save to the Trip Table, the Trip Name, The Trip Cost, No of Persons, Amount per Head

        // 1 Check is the particular trip exists in the table, if not create one.
        Trip trip = tripDataSource.getTrip(tripName);
        boolean value;

        if(trip == null){
            trip = new Trip(tripName);
            trip.setNoOfPersons(bundle.getInt("Trip_no_of_persons"));
            trip.setTotalCost(itemCost);
            trip.setAmountPerHead(itemCost/bundle.getInt("Trip_no_of_persons"));
            value = tripDataSource.addTrip(trip);

            Toast.makeText(this, " Trip added " + value, Toast.LENGTH_LONG).show();

        }else{
            trip.setTotalCost(trip.getTotalCost() + itemCost);
            trip.setAmountPerHead(trip.getTotalCost()/ bundle.getInt("Trip_no_of_persons"));
            value = tripDataSource.updateTrip(trip);

            Toast.makeText(this, " Trip updated " + value + "Trip Name: " + trip.getTripName(), Toast.LENGTH_LONG).show();
        }

        tripDataSource.close();

    }

    @Override
    public void numberIsPressed(String total) {
        editText.setText(total);
    }

    @Override
    public void doneButtonPressed(String total) {
        editText.setText(total);
        if(keyboard_fragment.isVisible())
            getSupportFragmentManager().beginTransaction().hide(keyboard_fragment).commit();
    }

    @Override
    public void backLongPressed() {
        editText.setText("");
    }

    @Override
    public void backButtonPressed(String total) {
        editText.setText(total);
    }

    @Override
    public void onBackPressed() {
        if(keyboard_fragment!=null)
        {
            if(keyboard_fragment.isVisible())
                getSupportFragmentManager().beginTransaction().remove(keyboard_fragment).commit();
            else
                super.onBackPressed();
        }
        else
            super.onBackPressed();
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

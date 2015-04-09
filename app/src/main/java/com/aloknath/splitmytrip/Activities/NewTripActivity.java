package com.aloknath.splitmytrip.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aloknath.splitmytrip.CustomViews.EditTextNoKeyBoard;
import com.aloknath.splitmytrip.Database.TripDataSource;
import com.aloknath.splitmytrip.Fragments.KeyBoardFragment;
import com.aloknath.splitmytrip.Objects.Trip;
import com.aloknath.splitmytrip.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ALOKNATH on 2/24/2015.
 */
public class NewTripActivity extends FragmentActivity implements KeyBoardFragment.onKeyBoardEvent{


    private Button cancel;
    private Button next;
    private KeyBoardFragment keyboard_fragment;
    private EditText editText;
    private EditText tripTitle;
    private EditText tripPersonCount;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_location);

        try
        {
            View view = getWindow().getDecorView().findViewById(android.R.id.content);
            InputStream ims = getAssets().open("enter_trip.jpg");
            Drawable d = Drawable.createFromStream(ims, null);
            view.setBackground(d);

        }catch(IOException ex)
        {
            return;
        }

        InputStream ims = null;
        try {
            ims = getAssets().open("button_next.png");
            Drawable d = Drawable.createFromStream(ims, null);

            next = (Button)findViewById(R.id.button_next);
            next.setBackground(d);

            ims = getAssets().open("button_cancel.png");
            d = Drawable.createFromStream(ims, null);

            cancel = (Button)findViewById(R.id.button_cancel);
            cancel.setBackground(d);

        } catch (IOException e) {
            e.printStackTrace();
        }


        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editText = (EditText)findViewById(R.id.edit_trip_no_persons);
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
                tripTitle = (EditText)findViewById(R.id.edit_trip_location);
                tripPersonCount = (EditText)findViewById(R.id.edit_trip_no_persons);

                if(tripTitle.getText().toString().isEmpty() || tripPersonCount.getText().toString().isEmpty()){
                    Toast.makeText(NewTripActivity.this,"Enter The Trip Title and the Person Count !!", Toast.LENGTH_SHORT).show();
                }else{

                    Intent intent = new Intent(NewTripActivity.this, EnterItemActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Trip_title", tripTitle.getText().toString());
                    bundle.putInt("Trip_no_of_persons", Integer.parseInt(tripPersonCount.getText().toString()));
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
            }
        });

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

}

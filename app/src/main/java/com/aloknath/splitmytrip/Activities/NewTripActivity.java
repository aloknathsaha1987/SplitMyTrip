package com.aloknath.splitmytrip.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aloknath.splitmytrip.CustomViews.EditTextNoKeyBoard;
import com.aloknath.splitmytrip.Database.TripDataSource;
import com.aloknath.splitmytrip.Fragments.KeyBoardFragment;
import com.aloknath.splitmytrip.Objects.Trip;
import com.aloknath.splitmytrip.R;

/**
 * Created by ALOKNATH on 2/24/2015.
 */
public class NewTripActivity extends FragmentActivity implements KeyBoardFragment.onKeyBoardEvent{


    private Button cancel;
    private Button next;
    private KeyBoardFragment keyboard_fragment;
    private EditText editText;
    private EditTextNoKeyBoard enter_count;
//    private View.OnTouchListener otl = new View.OnTouchListener() {
//        public boolean onTouch (View v, MotionEvent event) {
//            ((EditText)v).requestFocus();
//            return true; // the listener has consumed the event
//        }
//    };

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
                Intent intent = new Intent(NewTripActivity.this, EnterItemActivity.class);
                Bundle bundle = new Bundle();

                editText = (EditText)findViewById(R.id.edit_trip_location);
                bundle.putString("Trip_title", editText.getText().toString());

                editText = (EditText)findViewById(R.id.edit_trip_no_persons);
                bundle.putInt("Trip_no_of_persons", Integer.parseInt(editText.getText().toString()));

                intent.putExtras(bundle);

                startActivity(intent);

               // saveToDb();
            }
        });

    }

    private void hideKeyBoard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
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

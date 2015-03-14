package com.aloknath.splitmytrip.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.aloknath.splitmytrip.Fragments.ParentViewPagerFragment;

/**
 * Created by ALOKNATH on 3/14/2015.
 */
public class AttachFragmentActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        setTitle(extras.getString("name"));
        String tag = extras.getString("FragmentTagKey");

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);

        if(fragment == null){
            fragment = Fragment.instantiate(this, tag);
            fragment.setArguments(getIntent().getExtras());
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(android.R.id.content, fragment, tag);
            ft.commit();
        }

    }
}

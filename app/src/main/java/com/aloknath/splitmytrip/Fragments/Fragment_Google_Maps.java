package com.aloknath.splitmytrip.Fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aloknath.splitmytrip.Activities.GoogleMapsActivity;
import com.aloknath.splitmytrip.Activities.SendAmountActivity;
import com.aloknath.splitmytrip.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ALOKNATH on 2/24/2015.
 */
public class Fragment_Google_Maps extends Fragment {

    private String title;
    TextView textView;
    View view;

    public static Fragment_Google_Maps newInstance(String title){
        Fragment_Google_Maps fragment_google_maps = new Fragment_Google_Maps();
        Bundle args = new Bundle();
        args.putString("Title", title);
        fragment_google_maps.setArguments(args);
        return fragment_google_maps;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("Title");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_google_maps, viewGroup, false);

        return view;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try
        {
            // get input stream
            InputStream ims = getActivity().getAssets().open("Google_Maps.jpg");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            view.setBackground(d);
        }catch(IOException ex)
        {
            return;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GoogleMapsActivity.class);
                startActivity(intent);
            }
        });
    }

}

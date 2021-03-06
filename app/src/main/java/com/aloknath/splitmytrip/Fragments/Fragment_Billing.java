package com.aloknath.splitmytrip.Fragments;

/**
 * Created by ALOKNATH on 2/24/2015.
 */


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aloknath.splitmytrip.Activities.SendAmountActivity;
import com.aloknath.splitmytrip.CustomViews.RoundedImageView;
import com.aloknath.splitmytrip.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by ALOKNATH on 2/21/2015.
 */


@SuppressLint("NewApi")
public class Fragment_Billing extends Fragment {
    private String title;
    TextView textView;
    View view;

    public static Fragment_Billing newInstance(String title){
        Fragment_Billing fragmentBilling = new Fragment_Billing();
        Bundle args = new Bundle();
        args.putString("Title", title);
        fragmentBilling.setArguments(args);
        return fragmentBilling;
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
        view =  inflater.inflate(R.layout.fragment_billing, viewGroup, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RoundedImageView imageView = (RoundedImageView)view.findViewById(R.id.imageView2);

        try {
            InputStream inputStream = getActivity().getAssets().open("bill.jpg");
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SendAmountActivity.class);
                startActivity(intent);
            }
        });
    }
}
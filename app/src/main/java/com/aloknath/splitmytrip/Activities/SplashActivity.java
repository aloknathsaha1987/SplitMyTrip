package com.aloknath.splitmytrip.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.aloknath.splitmytrip.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ALOKNATH on 2/24/2015.
 */
public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);
        ImageView view = (ImageView)findViewById(R.id.imageView);
        try
        {
            // get input stream
            InputStream ims = getAssets().open("splash_screen.jpg");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            //view.setBackground(d);

            //this.findViewById(android.R.id.content).setBackground(d);
            view.setImageDrawable(d);
            view.setScaleType(ImageView.ScaleType.FIT_XY);

        }catch(IOException ex)
        {
            return;
        }

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
}

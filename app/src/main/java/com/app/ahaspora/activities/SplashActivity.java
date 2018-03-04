package com.app.ahaspora.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.ahaspora.R;
import com.app.ahaspora.utilities.DateUtil;

/**
 * Created by aangjnr on 02/06/2017.
 */

public class SplashActivity extends BaseActivity {

    Thread splashTread;
    LinearLayout textLayout;
    SharedPreferences prefs;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);


        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (!prefs.getString("today", "").equals(DateUtil.getFormattedDate())) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window w = getWindow(); // in Activity's onCreate() for instance
                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            setContentView(R.layout.splash_activity);



            StartAnimations();

        } else {// Start next Activity, check for whether person is signed in or not then move to next activity

            moveToNextActivity();
        }


    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        finish();
    }


    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout splash = (RelativeLayout) findViewById(R.id.splash_layout);
        splash.clearAnimation();
        splash.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate_splash_screen);
        anim.reset();
        LinearLayout iv = (LinearLayout) findViewById(R.id.splash_icon);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 2000) {
                        sleep(100);
                        waited += 100;
                    }


                    moveToNextActivity();


                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashActivity.this.finish();
                }

            }
        };
        splashTread.start();

    }


    private void moveToNextActivity(){

        boolean isSignedIn = prefs.getBoolean("isSignedIn", false);

        Log.i(TAG, "Is signed in? " + isSignedIn);


        if(!isSignedIn) {

            startActivity(new Intent(this, LoginActivity.class));

        }else {

            startActivity(new Intent(this, MainActivity.class));

        }


    }



}
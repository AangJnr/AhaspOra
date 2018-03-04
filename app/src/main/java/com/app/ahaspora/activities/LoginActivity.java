package com.app.ahaspora.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ahaspora.R;
import com.app.ahaspora.utilities.CustomToast;
import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.IndeterminateProgressButton;


/**
 * Created by aangjnr on 02/06/2017.
 */


public class LoginActivity extends BaseActivity {

    IndeterminateProgressButton btnMorph;
    TextView socialSignIn;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
         }

        setContentView(R.layout.login_activity);

        btnMorph =  findViewById(R.id.btnMorph);
        btnMorph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                simulateProgress(btnMorph);

            }
        });




         socialSignIn = (TextView) findViewById(R.id.social_sign_in);









        socialSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomLoginDialog();
            }
        });



    }


        private void login(){



            new Thread() {
                @Override
                public void run() {
                    try {
                        int waited = 0;
                        // Splash screen pause time
                        while (waited < 2000) {
                            sleep(100);
                            waited += 100;
                        }


                        PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit().putBoolean("isSignedIn", true).apply();
                        moveToNextActivity();

                    } catch (InterruptedException e) {
                        // do nothing
                    }

                }
            }.start();







        }


    private void moveToNextActivity(){

        startActivity(new Intent(this, MainActivity.class));
        this.finish();


    }




    void showCustomLoginDialog(){

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.custom_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this, R.style.AppDialog);
        alertDialogBuilderUserInput.setView(mView);


        TextView googleSignIn = (TextView) mView.findViewById(R.id.google_sign_in);

    TextView facebookSignIn = (TextView) mView.findViewById(R.id.facebook_sign_in);


        alertDialogBuilderUserInput
                .setCancelable(true);
        final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomToast.makeToast(LoginActivity.this, "Google sign in", Toast.LENGTH_SHORT).show();
                alertDialogAndroid.dismiss();
                signInWithGoogle();


            }
        });


        facebookSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomToast.makeToast(LoginActivity.this, "Facebook sign in", Toast.LENGTH_SHORT).show();
                alertDialogAndroid.dismiss();
                signInWithFacebook();
            }
        });


    }



    public void morphToSquare(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params square = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_corner_radius_2))
                .width(dimen(R.dimen.mb_width_200))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.mb_blue))
                .colorPressed(color(R.color.mb_blue_dark))
                .text(getString(R.string.mb_button));
        btnMorph.morph(square);
    }

    public void morphToSuccess(final MorphingButton btnMorph) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(integer(R.integer.mb_animation))
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_height_56))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.mb_green))
                .colorPressed(color(R.color.mb_green_dark))
                .icon(R.drawable.ic_done);
        btnMorph.morph(circle);
    }

    public void morphToFailure(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_height_56))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.mb_red))
                .colorPressed(color(R.color.mb_red_dark))
                .icon(R.drawable.ic_lock);
        btnMorph.morph(circle);
    }


    public void simulateProgress(@NonNull final IndeterminateProgressButton button) {
        int progressColor1 = color(R.color.colorAccent);
        int progressColor2 = color(R.color.holo_green_light);
        int progressColor3 = color(R.color.holo_orange_light);
        int progressColor4 = color(R.color.holo_red_light);
        int color = color(R.color.mb_gray);
        int progressCornerRadius = dimen(R.dimen.mb_corner_radius_4);
        int width = dimen(R.dimen.mb_width_200);
        int height = dimen(R.dimen.mb_height_8);
        int duration = integer(R.integer.mb_animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                morphToSuccess(button);
                button.unblockTouch();

                login();
            }
        }, 4000);

        button.blockTouch(); // prevent user from clicking while button is in progress
        button.morphToProgress(color, progressCornerRadius, width, height, duration, progressColor1);
    }



    void signInWithGoogle(){}
    void signInWithFacebook(){}



}

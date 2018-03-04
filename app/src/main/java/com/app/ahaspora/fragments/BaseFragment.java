package com.app.ahaspora.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.app.ahaspora.R;
import com.app.ahaspora.utilities.Constants;
import com.app.ahaspora.utilities.CustomToast;
import com.app.ahaspora.utilities.DatabaseHelper;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

/**
 * Created by aangjnr on 12/02/2018.
 */

public class BaseFragment extends Fragment {

    String TAG = BaseFragment.class.getSimpleName();
    static int mColors[];
    DatabaseHelper databaseHelper;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        databaseHelper = DatabaseHelper.getInstance(getActivity());
        mColors = getActivity().getResources().getIntArray(R.array.recommendations_colors);



    }




    public void startFadeInAnimation(View v) {

        v.animate()
                .alpha(1)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(500)
                .setDuration(500)
                .start();
    }



    public static int getColor(String tagName){
        if(tagName != null)

            switch(tagName) {

                case Constants.R_CATEGORY_ARTS_AND_CULTURE:
                    return mColors[1];

                case Constants.R_CLOTHING:
                    return mColors[2];

                case Constants.R_CRAFTSMEN:
                    return mColors[3];

                case Constants.R_EDUCATION:
                    return mColors[4];

                case Constants.R_FINANCE:
                    return mColors[5];

                case Constants.R_FITNESS:
                    return mColors[6];

                case Constants.R_FOOD:
                    return mColors[7];

                case Constants.R_GROOMING:
                    return mColors[8];

                case Constants.R_HEATHCARE:
                    return mColors[9];

                case Constants.R_HOUSE_KEEPING:
                    return mColors[10];

                case Constants.R_HR:
                    return mColors[11];

                case Constants.R_IT:
                    return mColors[12];

                case Constants.R_LEGAL:
                    return mColors[13];

                case Constants.R_MERCHANDISE:
                    return mColors[14];

                case Constants.R_PHOTO_VIDEOGRAPHY:
                    return mColors[15];

                case Constants.R_REAL_ESTATE:
                    return mColors[16];

                case Constants.R_SERVICES:
                    return mColors[17];

                case Constants.R_TRANSPORTATION:
                    return mColors[18];

                case Constants.R_TRAVEL:
                    return mColors[19];

                case Constants.R_MEN_CLOTHING:
                    return mColors[20];

                case Constants.R_DANCE_CHOREOGRAPHY:
                    return mColors[21];

                case Constants.R_SECURITY:
                    return mColors[22];

                case Constants.R_ADVERTS:
                    return mColors[23];

                case Constants.R_WORKOUT:
                    return mColors[24];

                default:
                    return mColors[0];

            }
        else  return mColors[0];

    }


    public static int getDrawable(String tagName){
        if(tagName != null)

            switch(tagName) {

                case Constants.R_CATEGORY_ARTS_AND_CULTURE:
                    return R.drawable.arts;

                case Constants.R_CLOTHING:
                    return R.drawable.clothing;

                case Constants.R_CRAFTSMEN:
                    return R.drawable.craftsmen;

                case Constants.R_EDUCATION:
                    return R.drawable.education;

                case Constants.R_FINANCE:
                    return R.drawable.finaince; //Todo rename

                case Constants.R_FITNESS:
                    return R.drawable.workout;

                case Constants.R_FOOD:
                    return R.drawable.food;

                case Constants.R_GROOMING:
                    return R.drawable.grooming;

                case Constants.R_HEATHCARE:
                    return R.drawable.healthcare;

                case Constants.R_HOUSE_KEEPING:
                    return R.drawable.house_keeping;

                case Constants.R_HR:
                    return R.drawable.hr;

                case Constants.R_IT:
                    return R.drawable.it;

                case Constants.R_LEGAL:
                    return R.drawable.legal;

                case Constants.R_MERCHANDISE:
                    return R.drawable.merchandise;

                case Constants.R_PHOTO_VIDEOGRAPHY:
                    return R.drawable.photo;

                case Constants.R_REAL_ESTATE:
                    return R.drawable.realestate;

                case Constants.R_SERVICES:
                    return R.drawable.services;

                case Constants.R_TRANSPORTATION:
                    return R.drawable.transport;

                case Constants.R_TRAVEL:
                    return R.drawable.travel;

                case Constants.R_MEN_CLOTHING:
                    return R.drawable.mens_clothing;

                case Constants.R_DANCE_CHOREOGRAPHY:
                    return R.drawable.dance;

                case Constants.R_SECURITY:
                    return R.drawable.security;

                case Constants.R_WORKOUT:
                    return R.drawable.workout;

                default:
                    return R.drawable.recommendation_default;
            }
        else  return R.drawable.recommendation_default;
    }



    public static int getJobsColor(String tagName){
        if(tagName != null)

            switch(tagName) {

                case Constants.J_FULL_TIME:
                    return mColors[1];

                case Constants.J_PART_TIME:
                    return mColors[2];

                case Constants.J_FREELANCE:
                    return mColors[3];

                case Constants.J_INTERNSHIP:
                    return mColors[4];

                case Constants.J_TEMPORARY:
                    return mColors[5];

                default:
                    return mColors[0];

            }
        else return R.color.colorAccent;
    }





    void startGoogleDirectionIntent(@Nullable LatLng source, LatLng destination, String nameOfPlace) {

/*

        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=" + source.latitude + "," + source.longitude + "+to:" + destination.latitude + ","
                + destination.longitude + " (%s)", 12f, 2f, "On Route");

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                CustomToast.makeToast(this, "Please install Google Maps from the PlayStore", Toast.LENGTH_SHORT).show();

            }
        }
*/






        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", destination.latitude, destination.longitude, nameOfPlace);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        try
        {
            startActivity(intent);
        }
        catch(ActivityNotFoundException ex)
        {
            try
            {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            }
            catch(ActivityNotFoundException innerEx)
            {
                CustomToast.makeToast(getActivity(), "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }




    void startEmailIntent(String email, String jobType){

        try{


            Intent intent = new Intent(Intent.ACTION_SENDTO);

            intent.setType("text/plain");
            intent.setData(Uri.parse("mailto:" + email));
            intent.putExtra(Intent.EXTRA_EMAIL, email);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Application Letter for " + jobType);
            //intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

            startActivity(Intent.createChooser(intent, "Send Email"));

        } catch (ActivityNotFoundException e) {


            e.printStackTrace();
        }

    }


    void startBrowserIntent(String url){

        Log.i(TAG, "URL IS " + url);

        if (!url.startsWith("https://") && !url.startsWith("http://")){
            url = "http://" + url;
        }


        try{
            Intent i = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));
            startActivity(i);

        } catch (ActivityNotFoundException e) {

            e.printStackTrace();

        }




    }



    void startTwitterIntent(String twitterUrl){

        Log.i(TAG, "URL IS " + twitterUrl);

        Intent intent = null;
        try {
            // get the Twitter app if possible
            getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);

        } catch (ActivityNotFoundException e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl));
            this.startActivity(intent);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }



}

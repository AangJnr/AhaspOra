package com.app.ahaspora.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.ahaspora.R;
import com.app.ahaspora.activities.BaseActivity;
import com.app.ahaspora.models.Job;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.hlab.fabrevealmenu.enums.Direction;
import com.hlab.fabrevealmenu.listeners.OnFABMenuSelectedListener;
import com.hlab.fabrevealmenu.view.FABRevealMenu;

/**
 * Created by aangjnr on 07/06/2017.
 */

public class DetailedJobListingActivity extends BaseActivity implements OnFABMenuSelectedListener {


    Job job;
    TextView jobTitle;
    TextView companyName;
    TextView bodyText;
    FloatingActionButton floatingActionButton;
    WebView summary;
    FABRevealMenu fabMenu;

    RelativeLayout parentLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();

        if(intent != null)
            job = new Gson().fromJson(intent.getStringExtra("job"), Job.class);



       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }*/

       setLightStatusBar(getWindow().getDecorView(), this);


        setContentView(R.layout.activity_detailed_job_listing);


        jobTitle = (TextView) findViewById(R.id.job_title);
        companyName  = (TextView) findViewById(R.id.company_name);
        bodyText  =   findViewById(R.id.body_text);

        summary = (WebView) findViewById(R.id.job_summary);
        parentLayout = (RelativeLayout) findViewById(R.id.parent_layout);



        if(job != null) {

                jobTitle.setText(job.getTitle());
                companyName.setText(job.getCompany().getName());


               // text = "<html><body><p align=\"justify\">";
                String text = job.getBody();
                //text += "</p></body></html>";
                //summary.loadData(text, "text/html", "utf-8");

            bodyText.setText(Html.fromHtml(text));

            floatingActionButton = findViewById(R.id.fab);
            fabMenu = findViewById(R.id.fabMenu);


            try {
                if (floatingActionButton != null && fabMenu != null) {

                    if(job.getCompany().getWebsite() == null || job.getCompany().getWebsite().isEmpty() || job.getCompany().getWebsite().equalsIgnoreCase(""))
                        fabMenu.getItemById(R.id.website).setEnabled(false);

                    if(job.getCompany().getEmail() == null|| job.getCompany().getEmail().isEmpty() || job.getCompany().getEmail().equalsIgnoreCase(""))
                        fabMenu.getItemById(R.id.email).setEnabled(false);

                    if(job.getCompany().getTwitter() == null || job.getCompany().getTwitter().isEmpty() || job.getCompany().getTwitter().equalsIgnoreCase(""))
                        fabMenu.getItemById(R.id.twitter).setEnabled(false);

                    if(job.getLocation().getLat() == null || job.getLocation().getLon() == null)
                        fabMenu.getItemById(R.id.location).setEnabled(false);



                        //  setFabMenu(fabMenu);
                    //attach menu to fab
                    fabMenu.bindAnchorView(floatingActionButton);
                    //set menu selection listener
                    fabMenu.setOnFABMenuSelectedListener(this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }




        }


    }

    @Override
    public void onMenuItemSelected(View view, int id) {

        switch(id){

            case R.id.website:

                     startBrowserIntent(job.getCompany().getWebsite());

                break;

            case R.id.email:

                     startEmailIntent(job.getCompany().getEmail(), job.getTitle());

                break;


            case R.id.twitter:

                     startTwitterIntent(job.getCompany().getTwitter());

                break;

            case R.id.location:

                     startGoogleDirectionIntent(null, new LatLng(Double.parseDouble(job.getLocation().getLat()), Double.parseDouble(job.getLocation().getLon())), job.getCompany().getName());

                break;

        }







    }


    @Override
    protected void onStop() {
        clearLightStatusBar(this, getWindow().getDecorView());
        super.onStop();
    }


    @Override
    public void onBackPressed() {
        if(fabMenu.isShowing())
            fabMenu.closeMenu();

        else super.onBackPressed();

    }
}

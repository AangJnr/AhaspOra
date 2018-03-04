package com.app.ahaspora.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
 import android.support.v7.app.ActionBarDrawerToggle;
 import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
 import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.ahaspora.models.Category;
import com.app.ahaspora.utilities.Constants;
import com.app.ahaspora.R;
import com.app.ahaspora.fragments.EventsFragment;
import com.app.ahaspora.fragments.JobsFragment;
import com.app.ahaspora.fragments.PostsFragment;
import com.app.ahaspora.fragments.RecommendationsFragment;
import com.app.ahaspora.utilities.CustomToast;
import com.app.ahaspora.utilities.MySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aangjnr on 02/06/2017.
 */

public class MainActivity extends BaseActivity implements RatingDialogListener {


    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    int lastSelectedId = 0;
    Toolbar toolbar;
    LinearLayout title_layout;
    NavigationView navigationView;

    String SELECTED_FRAGMENT = "PostsFragment";
    View coordLayout;

    String TAG = MainActivity.class.getSimpleName();
    boolean isTitleVisible = false;
    private DrawerLayout navDrawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private long onLongPressed = 0;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }*/

        fetchCategoriesData();


        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

           /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);*/

        }

        navDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        coordLayout = findViewById(R.id.coordinatorLayout);
        title_layout = (LinearLayout) findViewById(R.id.title_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View hView = navigationView.getHeaderView(0);

        CircleImageView profile_photo = hView.findViewById(R.id.nav_profile_photo);

         profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navDrawer.closeDrawers();

                 startActivity(new Intent(MainActivity.this, UserProfileActivity.class));


            }
        });


        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.main_appbar);


       /* appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
              *//*  Log.i(TAG, "AppBar vertical offset is " + Utils.dipsToPixels(MainActivity.this, -verticalOffset));
                Log.i(TAG, "Collapsing toolbar height " + collapsingToolbar.getMeasuredHeight());
                Log.i(TAG, "toolbar height " + toolbar.getMeasuredHeight());*//*


                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // Collapsed
                   // Log.i(TAG, "COLLAPSED! ");

                    if(title_layout.getVisibility() == View.GONE) {

                        Animation fadeIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in_text);
                        fadeIn.reset();

                        title_layout.startAnimation(fadeIn);
                        title_layout.setVisibility(View.VISIBLE);

                    }

                } else if (verticalOffset == 0) {

                   // Log.i(TAG, "EXPANDED! ");
                    // Expanded

                    if(title_layout.getVisibility() == View.VISIBLE) {

                        Animation fadeOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out_text);
                        fadeOut.reset();
                        title_layout.startAnimation(fadeOut);
                        title_layout.setVisibility(View.GONE);

                    }
                }
            }
        });

    */

         actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                navDrawer,
                toolbar,
                R.string.open,
                R.string.close
        )

        {
            @Override
            public void onDrawerSlide(View drawer, float slideOffset)
            {

                coordLayout.setX(drawer.getWidth() * slideOffset);

            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                syncState();

                Constants.IS_NAVIGATION_DRAWER_VISIBLE = false;

            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();

                Constants.IS_NAVIGATION_DRAWER_VISIBLE = true;
            }
        };


        navDrawer.addDrawerListener(actionBarDrawerToggle);
        //actionBarDrawerToggle.syncState();



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.payments:

                        navDrawer.closeDrawers();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                startActivity(new Intent(MainActivity.this, SetupPaymentsActivity.class));

                            }
                        },300);


                        break;


                    case R.id.starred_messages:

                        CustomToast.makeToast(MainActivity.this, "Starred Posts Activity", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(MainActivity.this, UserProfileActivity.class));

                        break;
                    case R.id.starred_jobs:

                        CustomToast.makeToast(MainActivity.this, "Starred Jobs Activity", Toast.LENGTH_SHORT).show();

                        break;

                    case R.id.starred_events:

                        CustomToast.makeToast(MainActivity.this, "Starred Events Activity", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.starred_recommendations:

                        CustomToast.makeToast(MainActivity.this, "Starred Recommendations Activity", Toast.LENGTH_SHORT).show();

                        break;

                    case R.id.settings:

                        CustomToast.makeToast(MainActivity.this, "Preference Activity", Toast.LENGTH_SHORT).show();

                        break;

                }

                return false;
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        Class selectedFragment = null;

                        switch (item.getItemId()) {

                            case R.id.home:

                                    selectedFragment =  PostsFragment.class;


                                lastSelectedId = 1;
                                break;
                            case R.id.jobs:

                                    selectedFragment = JobsFragment.class;


                                lastSelectedId = 2;
                                break;
                            case R.id.events:

                                    selectedFragment = EventsFragment.class;


                                lastSelectedId = 3;

                                break;

                            case R.id.recommendations:
                                lastSelectedId = 4;

                                      selectedFragment = RecommendationsFragment.class;


                        }


                        if (navDrawer.isShown())
                            navDrawer.closeDrawers();

                        if(selectedFragment != null)
                            replaceFragment(selectedFragment);

                        return true;
                    }
                });


        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SELECTED_FRAGMENT = PostsFragment.class.getSimpleName();
        transaction.replace(R.id.frame_layout, PostsFragment.newInstance(), SELECTED_FRAGMENT);
        transaction.commit();

        bottomNavigationView.getMenu().getItem(0).setChecked(true);

    }



    void replaceFragment(final Fragment selectedFragment) {

        if (!SELECTED_FRAGMENT.equals(selectedFragment.getClass().getSimpleName()))

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment, selectedFragment.getClass().getSimpleName());
                        transaction.commit();

                        SELECTED_FRAGMENT = selectedFragment.getClass().getSimpleName();


                }
            }, 300);


    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

       // getMenuInflater().inflate(R.menu.main_activity_menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            //CustomToast.makeToast(MainActivity.this, "Toggled! Now fix it!", Toast.LENGTH_SHORT).show();
            return true;
        }

        switch (item.getItemId()){
            case R.id.payments:

                startActivity(new Intent(MainActivity.this, PaymentsActivity.class));

                break;

            case R.id.starred_jobs:

                CustomToast.makeToast(this, "Starred Jobs Activity", Toast.LENGTH_SHORT).show();

                break;


            case R.id.starred_events:

                CustomToast.makeToast(this, "Starred Events Activity", Toast.LENGTH_SHORT).show();

                break;

            case R.id.starred_recommendations:

                CustomToast.makeToast(this, "Starred Recommendations Activity", Toast.LENGTH_SHORT).show();

                break;


        }





        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        Log.i(TAG, "Is bottom nav visible ? " + Constants.IS_BOTTOM_NAVIGATION_VISIBLE);

        Log.i(TAG, "Is drawer visible ? " + navDrawer.isShown());


        if(!Constants.IS_BOTTOM_NAVIGATION_VISIBLE){

            bottomNavigationView.animate()
                    .translationY(0)
                    .setDuration(300)
                    .setInterpolator(new AccelerateInterpolator())
                    .start();

            Constants.IS_BOTTOM_NAVIGATION_VISIBLE = true;

        }else if (Constants.IS_NAVIGATION_DRAWER_VISIBLE) navDrawer.closeDrawers();

        else{

             if (System.currentTimeMillis() < onLongPressed + 2000) {

                 super.onBackPressed();

            } else {
                 CustomToast.makeToast(this, "Please press back again to exit", Toast.LENGTH_SHORT).show();
            }
            onLongPressed = System.currentTimeMillis();

        }




    }



    public void toggleDrawer(View v) {

        if (navDrawer.isDrawerOpen(GravityCompat.START)) {
            navDrawer.closeDrawer(GravityCompat.START);
        } else
            navDrawer.openDrawer(GravityCompat.START);

    }

    @Override
    public void onPositiveButtonClicked(int i, String s) {

    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {
    }


    void fetchCategoriesData(){

        Log.i(TAG, "FETCHING CATEGORIES DATA! ");



        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Constants.PARENT_URL + "categories", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.i(TAG, "SIZE OF JSON ARRAY IS " + response.length());

                Type listType = new TypeToken<List<Category>>() {}.getType();

                List<Category> Categories = new Gson().fromJson(String.valueOf(response), listType);


                for (Category r : Categories){
                    Boolean value = databaseHelper.hasCategoryBeenUpdated(r.getId(), r.getLastUpdated());

                    if(value == null) {
                        databaseHelper.addACategory(r);

                    }
                    else{
                        if(value){
                            Log.i(TAG, "CATEGORY WAS UPDATED");
                            if(databaseHelper.deleteCategory(r.getId()))
                                databaseHelper.addACategory(r);
                        }
                    }



                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG, "***  ERR  *** " + error.getMessage());

            }
        });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);


    }




    public static boolean isFragmentInBackstack(final android.support.v4.app.FragmentManager fragmentManager, final String fragmentTagName) {
        for (int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++) {
            if (fragmentTagName.equals(fragmentManager.getBackStackEntryAt(entry).getName())) {
                return true;
            }
        }
        return false;
    }




    private void replaceFragment(Class fragmentClass) {

        Fragment fragment = null;
        String backStateName = fragmentClass.getSimpleName();


        Boolean fragmentExit = isFragmentInBackstack(getSupportFragmentManager(), backStateName);


        if (fragmentExit) {
            getSupportFragmentManager().popBackStackImmediate(fragmentClass.getSimpleName(), 0);

        } else {

             try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

             FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragment, fragmentClass.getSimpleName());
            ft.addToBackStack(fragmentClass.getSimpleName());
            ft.commit();

             Integer nFragment = getSupportFragmentManager().getBackStackEntryCount();

            Log.d("Fragment: ", "Number of Fragment: "+nFragment);


         }

    }
}

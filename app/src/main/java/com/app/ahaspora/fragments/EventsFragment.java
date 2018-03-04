package com.app.ahaspora.fragments;

import android.animation.Animator;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.ahaspora.activities.DetailedEventActivity;
import com.app.ahaspora.activities.DetailedJobListingActivity;
import com.app.ahaspora.adapters.EventsAdapter;
import com.app.ahaspora.adapters.EventsPagerAdapter;
import com.app.ahaspora.adapters.JobsAdapter;
import com.app.ahaspora.adapters.PostsRecyclerAdapter;
import com.app.ahaspora.models.Author;
import com.app.ahaspora.models.Comment;
import com.app.ahaspora.utilities.Constants;
import com.app.ahaspora.R;
import com.app.ahaspora.models.Event;
import com.app.ahaspora.utilities.CustomToast;
import com.app.ahaspora.utilities.DateUtil;
import com.app.ahaspora.utilities.MySingleton;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.gigamole.infinitecycleviewpager.OnInfiniteCyclePageTransformListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by aangjnr on 05/06/2017.
 */

public class EventsFragment extends BaseFragment {

    View rootView;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    String TAG = EventsFragment.class.getSimpleName();
    List<Event> events;
    private EventsAdapter mAdapter;



    List<Event> EVENTS;
    private int CURRENT_SELECTED_ITEM = 0;

    public static EventsFragment newInstance() {

        return new EventsFragment();
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.events_fragment_layout, container, false);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorAccentBlue));
        recyclerView = rootView.findViewById(R.id.recycler_view);


        return rootView;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onActivityCreated(savedInstanceState);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                fetchData(true);

            }
        });thread.start();



    }





    void fetchData(final Boolean shouldRefreshFilter){


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(true);


            }
        });


        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Constants.PARENT_URL + "events",
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.i(TAG, "SIZE OF JSON ARRAY IS " + response.length());

                Type listType = new TypeToken<List<Event>>() {}.getType();

                List<Event> events = new Gson().fromJson(String.valueOf(response), listType);


                for (Event e : events){
                    Boolean value = databaseHelper.hasEventBeenUpdated(e.getId(), e.getUpdatedAt());

                    if(value == null) {
                        databaseHelper.addAnEvent(e);

                    }
                    else{
                        if(value){
                            Log.i(TAG, "EVENTS WAS UPDATED");
                            if(databaseHelper.deleteEvent(e.getId()))
                                databaseHelper.addAnEvent(e);
                        }
                    }

                }



                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAdapter(shouldRefreshFilter);
                    }
                });


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG, "***  ERR  *** " + error.getMessage());




                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(false);
                        setAdapter(true);



                    }
                });

            }
        });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonArrayRequest);



    }

    void setAdapter(boolean refreshFilter){

        EVENTS =  databaseHelper.getAllEvents();
        mAdapter  = new EventsAdapter(getActivity(), EVENTS);


        if(EVENTS != null && EVENTS.size() > 0) {



            if(refreshFilter) {
                mAdapter  = new EventsAdapter(getActivity(), EVENTS);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(mAdapter);

                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        fetchData(false);
                    }
                });



                mAdapter.setOnItemClickListener(onItemClickListener);

            }else{

                mAdapter.setEvents(EVENTS);
                mAdapter.notifyDataSetChanged();

            }

        }



        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(false);



            }
        });









    }



     void animateHeader(final View v, float value) {

     }


    EventsAdapter.OnItemClickListener onItemClickListener = new EventsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            Event event = EVENTS.get(position);


            List<Comment> comments = new ArrayList<>();


            Comment comment = new Comment();
            comment.setCreatedAt("2018-02-21 7:05:19");
            comment.setId(5);
            comment.setAuthor(new Author("Aang Jnr", "https://avatars3.githubusercontent.com/u/8545691?s=400&u=351462146b0ba9b01661b4857adb9b657be932d4&v=4"));
            comment.setImage("http://www.comingsoon.net/assets/uploads/2017/11/blackpanthercharacter1.jpg");
            comment.setBody("This is Black Panther");

            comments.add(comment);

            event.setComments(comments);




            Log.i(TAG, "Item clicked at " + position);
            Intent intent = new Intent(getActivity(), DetailedEventActivity.class);

            intent.putExtra("event", new Gson().toJson(event));


            RelativeLayout parent_layout =   view.findViewById(R.id.parent_layout);

            TextView title_view = view.findViewById(R.id.event_title);
            ImageView imageView =  view.findViewById(R.id.events_image_view);

            TextView body_view =  view.findViewById(R.id.event_desc);

            Pair<View, String> parent = Pair.create((View) parent_layout, "parent");
            Pair<View, String> title = Pair.create((View) title_view, "title");
            Pair<View, String> image = Pair.create((View) imageView, "image");
            Pair<View, String> body = Pair.create((View) body_view, "body");


            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                    parent,  title, image);

            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());


        }
    };

}

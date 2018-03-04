package com.app.ahaspora.fragments;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.ahaspora.activities.StatusUpdateActivity;
import com.app.ahaspora.adapters.PostsRecyclerAdapter;
import com.app.ahaspora.adapters.MyFilterAdapter;
import com.app.ahaspora.models.Category;
import com.app.ahaspora.models.Post;
import com.app.ahaspora.utilities.Callbacks;
import com.app.ahaspora.utilities.Constants;
import com.app.ahaspora.R;
import com.app.ahaspora.adapters.RecommendationsAdapter;
import com.app.ahaspora.models.Recommendation;
import com.app.ahaspora.models.Tag;
import com.app.ahaspora.utilities.DatabaseHelper;
import com.app.ahaspora.utilities.MySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yalantis.filter.animator.FiltersListItemAnimator;
import com.yalantis.filter.listener.FilterListener;
import com.yalantis.filter.widget.Filter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aangjnr on 05/06/2017.
 */

public class PostsFragment extends BaseFragment implements  Callbacks.UpdateFeedAdapterListener {

 
    String TAG = PostsFragment.class.getSimpleName();
    private RecyclerView mRecycler;
    List<Post> POSTS;
    PostsRecyclerAdapter mAdapter;
    CircleImageView profilePhoto;

     View rootView;

    SwipeRefreshLayout swipeRefreshLayout;
    private BottomSheetBehavior mBottomSheetBehavior;

    View mLayoutBottomSheet;

    public static PostsFragment newInstance() {

        return new PostsFragment();
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         //categories = databaseHelper.getAllCategoriesByType("Post");

        StatusUpdateActivity.OnFeedItemUpdatedistener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.home_fragment_layout, container, false);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorAccentBlue));

        profilePhoto =  rootView.findViewById(R.id.profile_photo);

        mRecycler =  rootView.findViewById(R.id.recycler_view);

        return rootView;
    }

    @SuppressWarnings("unchecked")
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


        rootView.findViewById(R.id.whatsNewLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), StatusUpdateActivity.class);

                Pair<View, String> parent = Pair.create((View) rootView.findViewById(R.id.whatsNewCv), "whatsNewLayout");
                Pair<View, String> civPair = Pair.create((View) profilePhoto, "whatsNewCiv");


                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        parent, civPair);

                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

            }
        });

    }










    void fetchData(final Boolean shouldRefreshFilter){


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(true);


            }
        });


        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Constants.PARENT_URL + "posts", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.i(TAG, "SIZE OF JSON ARRAY IS " + response.length());

                Type listType = new TypeToken<List<Post>>() {}.getType();

                List<Post> posts = new Gson().fromJson(String.valueOf(response), listType);


                for (Post p : posts){
                    Boolean value = databaseHelper.hasPostBeenUpdated(p.getId(), p.getUpdatedAt());

                    if(value == null) {
                        databaseHelper.addAPost(p);

                    }
                    else{
                        if(value){
                            Log.i(TAG, "POSTS WAS UPDATED");
                            if(databaseHelper.deletePost(p.getId()))
                                databaseHelper.addAPost(p);
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

        POSTS =  databaseHelper.getAllPost();


        if(POSTS != null && POSTS.size() > 0) {

            if(refreshFilter) {
                mAdapter  = new PostsRecyclerAdapter(getActivity(), POSTS);


                LinearLayoutManager lll = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                mRecycler.setLayoutManager(lll);
                mRecycler.setHasFixedSize(true);
                mRecycler.buildDrawingCache(true);
                mRecycler.setAdapter(mAdapter);
                startFadeInAnimation();



                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //mFilter.deselectAll();

                        fetchData(false);
                    }
                });

            }else{

                mAdapter.setPosts(POSTS);
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






    private void startFadeInAnimation() {

        mRecycler.animate()
                .alpha(1)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(500)
                .setDuration(500)
                .start();
    }


    @Override
    public void onFeedItemAdded(Post post) {
        Log.i(TAG, "ON FEED ITEM ADDED UPDATED LISTENER");
        mAdapter.getPosts().add(0, post);
        mAdapter.notifyItemInserted(0);
        
        
        
    }
}

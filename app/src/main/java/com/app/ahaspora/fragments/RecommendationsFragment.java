package com.app.ahaspora.fragments;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.ahaspora.BottomDialog;
import com.app.ahaspora.models.Category;
import com.app.ahaspora.utilities.Constants;
import com.app.ahaspora.R;
import com.app.ahaspora.adapters.MyFilterAdapter;
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

/**
 * Created by aangjnr on 05/06/2017.
 */

public class RecommendationsFragment extends BaseFragment implements FilterListener<Tag> {

    RequestQueue requestQueue;

    String TAG = RecommendationsFragment.class.getSimpleName();
    private RecyclerView mRecycler;
    List<Recommendation> RECOMMENDATIONS;
    RecommendationsAdapter mAdapter;
    private BottomNavigationView bottomNavigationView;

    private Filter<Tag> mFilter;
    private int[] mColors;
    private List<Category> categories;
    View rootView;

    SwipeRefreshLayout swipeRefreshLayout;
    private BottomSheetBehavior mBottomSheetBehavior;

    View mLayoutBottomSheet;

    public static RecommendationsFragment newInstance() {
        return new RecommendationsFragment();
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         mColors = getResources().getIntArray(R.array.recommendations_colors);
        categories = databaseHelper.getAllCategoriesByType("Recommendation");

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation);

       // mLayoutBottomSheet = LayoutInflater.from(getActivity()).inflate(R.layout.custom_bottom_dialog, null);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.recommendations_fragment_layout, container, false);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorAccentBlue));

        mRecycler =  rootView.findViewById(R.id.recommendationsFragmentRecycler);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                fetchData(true);
            }
        });

        thread.start();

        return rootView;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onActivityCreated(savedInstanceState);



    }


    @Override
    public void onFiltersSelected(@NotNull ArrayList<Tag> filters) {
        Log.i(TAG, "On filters selected " +  filters.size());

        List<Recommendation> newRecs = findRecommendationsByTags(filters);
        List<Recommendation> oldRecs = mAdapter.getRecommendations();
        mAdapter.setRecommendations(newRecs);

        calculateDiff(oldRecs, newRecs);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onNothingSelected() {
        Log.i(TAG, "On nothing selected " );

        if (mRecycler != null) {
            mAdapter.setRecommendations(RECOMMENDATIONS);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onFilterSelected(Tag tag) {
        Log.i(TAG, "On filter selected " +  tag.getText());


        if (tag.getText().equalsIgnoreCase("All Items")) {
            mFilter.deselectAll();
            mFilter.collapse();
        }
    }

    @Override
    public void onFilterDeselected(Tag tag) {

        Log.i(TAG, "On filter deselected " +  tag);

        List<Recommendation> oldJobs = mAdapter.getRecommendations();
        List<Recommendation> newJobs = findRecommendationsByTag(tag);

        mAdapter.setRecommendations(newJobs);
        calculateDiff(oldJobs, newJobs);
        mAdapter.notifyDataSetChanged();
    }









    void fetchData(final Boolean shouldRefreshFilter){

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });




        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Constants.PARENT_URL + "recommendations", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.i(TAG, "SIZE OF JSON ARRAY IS " + response.length());

                Type listType = new TypeToken<List<Recommendation>>() {}.getType();

                List<Recommendation> recommendations = new Gson().fromJson(String.valueOf(response), listType);


                for (Recommendation r : recommendations){
                    Boolean value = databaseHelper.hasRecommendationBeenUpdated(r.getId(), r.getUpdatedAt());

                    if(value == null) {
                        databaseHelper.addARecommendation(r);

                    }
                    else{
                        if(value){
                            Log.i(TAG, "RECOMMENDATION WAS UPDATED");
                            if(databaseHelper.deleteRecommendation(r.getId()))
                                databaseHelper.addARecommendation(r);
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

        RECOMMENDATIONS =  databaseHelper.getAllRecommendations();


        if(RECOMMENDATIONS != null && RECOMMENDATIONS.size() > 0) {

            if(refreshFilter) {

                mAdapter  = new RecommendationsAdapter(getActivity(), RECOMMENDATIONS);

                mFilter = rootView.findViewById(R.id.filter);
                mFilter.setAdapter(new MyFilterAdapter(getActivity(), getTags()));
                //the text to show when there's no selected items
                mFilter.setNoSelectedItemText("All Items");
                mFilter.build();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFilter.deselectAll();
                        mFilter.collapse();
                    }
                }, 1000);

                mFilter.setListener(this);



                LinearLayoutManager lll = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(lll);
        mRecycler.setHasFixedSize(true);
        //mAdapter.setHasStableIds(true);
        mRecycler.buildDrawingCache(true);
        mRecycler.setAdapter(mAdapter);
        startFadeInAnimation();
        mRecycler.setItemAnimator(new FiltersListItemAnimator());


        mAdapter.setOnItemClickListener(new RecommendationsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                showButtomSheet(mAdapter.getRecommendations().get(position));

            }
        });



                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if(mFilter.isActivated())
                        mFilter.deselectAll();

                        fetchData(false);
                    }
                });

            }else{

                mAdapter.setRecommendations(RECOMMENDATIONS);
                mAdapter.notifyDataSetChanged();

                if(mFilter.isActivated())
                    mFilter.deselectAll();

            }

        }



        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(false);



            }
        });



    }


    void showButtomSheet(Recommendation recommendation){

        BottomDialog bottomSheetDialog = BottomDialog.getInstance(recommendation, Constants.TYPE_RECOMMENDATION);

        mLayoutBottomSheet = bottomSheetDialog.getView();
        if (mLayoutBottomSheet != null) {
            mBottomSheetBehavior = BottomSheetBehavior.from(mLayoutBottomSheet);
        }

        bottomSheetDialog.show(getActivity().getSupportFragmentManager(), "Bottom Sheet");



    }





    private void calculateDiff(final List<Recommendation> oldList, final List<Recommendation> newList) {
        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
            }
        }).dispatchUpdatesTo(mAdapter);
    }

    private List<Tag> getTags() {
        List<Tag> tags = new ArrayList<>();

        for(Category category : categories) {

            Log.i(TAG, "CATEGORIES " +  category);

            tags.add(new Tag(category.getName(), BaseFragment.getColor(category.getName())));
        }


        return tags;
    }


    private List<Recommendation> findRecommendationsByTags(List<Tag> tags) {
        List<Recommendation> mRecommendations = new ArrayList<>();

        Log.i(TAG, "In finding Recommendations by tags ");
        Log.i(TAG, "Size of recommendations is  " + RECOMMENDATIONS.size());


        for (Recommendation _Recommendation : RECOMMENDATIONS) {

            for (Tag tag : tags) {
                if (databaseHelper.getCategory(_Recommendation.getCategoryId()).getName().equalsIgnoreCase(tag.getText()) && !mRecommendations.contains(_Recommendation)) {
                    mRecommendations.add(_Recommendation);

                    Log.i(TAG, _Recommendation.getTitle() + " has been added to the list!");
                }
            }
        }



/*
            for (Tag tag : tags) {

               mRecommendations.addAll(databaseHelper.getRecommendationsListByCategory(tag.getText()));

        }*/

        return mRecommendations;
    }


    private List<Recommendation> findRecommendationsByTag(Tag tag) {
        List<Recommendation> mRecommendations = new ArrayList<>();

        Log.i(TAG, "In finding Recommendations by tag ");
        Log.i(TAG, "Size of recommendations is  " + RECOMMENDATIONS.size());


        for (Recommendation _Recommendation : RECOMMENDATIONS) {

            if (databaseHelper.getCategory(_Recommendation.getCategoryId()).getName().equalsIgnoreCase(tag.getText()) && !mRecommendations.contains(_Recommendation)) {
                mRecommendations.add(_Recommendation);

                Log.i(TAG, _Recommendation.getTitle() + " has been added to the list!");
            }

        }



        // mRecommendations.addAll(databaseHelper.getRecommendationsListByCategory(tag.getText()));


        return mRecommendations;
    }


    private void startFadeInAnimation() {

        mRecycler.animate()
                .alpha(1)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(500)
                .setDuration(500)
                .start();
    }



}

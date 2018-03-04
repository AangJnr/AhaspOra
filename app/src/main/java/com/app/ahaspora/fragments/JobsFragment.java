package com.app.ahaspora.fragments;

 import android.content.Intent;
 import android.os.Handler;
 import android.support.annotation.NonNull;
 import android.support.design.widget.BottomNavigationView;
 import android.support.design.widget.BottomSheetBehavior;
 import android.support.v4.app.ActivityCompat;
 import android.support.v4.app.ActivityOptionsCompat;
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
 import android.widget.ImageView;
 import android.widget.RelativeLayout;
 import android.widget.TextView;

 import com.android.volley.Request;
 import com.android.volley.RequestQueue;
 import com.android.volley.Response;
 import com.android.volley.VolleyError;
 import com.android.volley.toolbox.JsonArrayRequest;
 import com.app.ahaspora.BottomDialog;
 import com.app.ahaspora.adapters.RecommendationsAdapter;
 import com.app.ahaspora.models.Category;
 import com.app.ahaspora.models.Recommendation;
 import com.app.ahaspora.utilities.Constants;
 import com.app.ahaspora.activities.DetailedJobListingActivity;
 import com.app.ahaspora.R;
 import com.app.ahaspora.adapters.JobsAdapter;
 import com.app.ahaspora.adapters.MyFilterAdapter;
 import com.app.ahaspora.models.Job;
 import com.app.ahaspora.models.Tag;
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

 import static com.app.ahaspora.utilities.Constants.TYPE_JOB;

/**
 * Created by aangjnr on 05/06/2017.
 */

public class JobsFragment extends BaseFragment implements FilterListener<Tag> {


    RequestQueue requestQueue;

    String TAG = JobsFragment.class.getSimpleName();
    private RecyclerView mRecycler;
    List<Job> JOBS;
    JobsAdapter mAdapter;
    private BottomNavigationView bottomNavigationView;

    private Filter<Tag> mFilter;
    private int[] mColors;
    private List<Category> categories;
    View rootView;

    SwipeRefreshLayout swipeRefreshLayout;
    private BottomSheetBehavior mBottomSheetBehavior;

    View mLayoutBottomSheet;



    public static JobsFragment newInstance() {

        return new JobsFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mColors = getResources().getIntArray(R.array.jobs_colors);
        categories = databaseHelper.getAllCategoriesByType(TYPE_JOB);



    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recommendations_fragment_layout, container, false);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorAccentBlue));

        mRecycler =  rootView.findViewById(R.id.recommendationsFragmentRecycler);

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
        });

        thread.start();


    }





    @Override
    public void onFiltersSelected(@NotNull ArrayList<Tag> filters) {
        Log.i(TAG, "On filters selected " +  filters.size());

        List<Job> newRecs = findJobsByTags(filters);
        List<Job> oldRecs = mAdapter.getJobs();
        mAdapter.setJobs(newRecs);

        calculateDiff(oldRecs, newRecs);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onNothingSelected() {
        Log.i(TAG, "On nothing selected " );

        if (mRecycler != null) {
            mAdapter.setJobs(JOBS);
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

        List<Job> oldJobs = mAdapter.getJobs();
        List<Job> newJobs = findJobsByTag(tag);

        mAdapter.setJobs(newJobs);
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


        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Constants.PARENT_URL + "jobs", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.i(TAG, "SIZE OF JSON ARRAY IS " + response.length());

                Type listType = new TypeToken<List<Job>>() {}.getType();

                List<Job> jobs = new Gson().fromJson(String.valueOf(response), listType);


                for (Job j : jobs){
                    Boolean value = databaseHelper.hasJobBeenUpdated(j.getId(), j.getUpdated_at());

                    if(value == null) {
                        databaseHelper.addJob(j);

                    }
                    else{
                        if(value){
                            Log.i(TAG, "JOB WAS UPDATED");
                            if(databaseHelper.deleteJob(j.getId()))
                                databaseHelper.addJob(j);
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

        JOBS =  databaseHelper.getAllJobs();


        if(JOBS != null && JOBS.size() > 0) {

            if(refreshFilter) {

                mAdapter  = new JobsAdapter(getActivity(), JOBS);

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
                mAdapter.setOnItemClickListener(onItemClickListener);



                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if(mFilter.isActivated())
                            mFilter.deselectAll();

                        fetchData(false);
                    }
                });

            }else{

                mAdapter.setJobs(JOBS);
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







    private void calculateDiff(final List<Job> oldList, final List<Job> newList) {
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

            tags.add(new Tag(category.getName(), BaseFragment.getJobsColor(category.getName())));
        }


        return tags;
    }


    private List<Job> findJobsByTags(List<Tag> tags) {
        List<Job> jobs = new ArrayList<>();

        Log.i(TAG, "In finding Recommendations by tags ");
        Log.i(TAG, "Size of recommendations is  " + JOBS.size());


        for (Job _job : JOBS) {

            for (Tag tag : tags) {
                if (databaseHelper.getCategory(_job.getCategory().getId()).getName().equalsIgnoreCase(tag.getText()) && !jobs.contains(_job)) {
                    jobs.add(_job);

                    Log.i(TAG, _job.getTitle() + " has been added to the list!");
                }
            }
        }



/*
            for (Tag tag : tags) {

               mRecommendations.addAll(databaseHelper.getRecommendationsListByCategory(tag.getText()));

        }*/

        return jobs;
    }


    private List<Job> findJobsByTag(Tag tag) {
        List<Job> jobs = new ArrayList<>();

        Log.i(TAG, "In finding JOB by tag ");
        Log.i(TAG, "Size of Jobs is  " + JOBS.size());


        for (Job _job : JOBS) {

            if (databaseHelper.getCategory(_job.getCategory().getId()).getName().equalsIgnoreCase(tag.getText()) && !jobs.contains(_job)) {
                jobs.add(_job);

                Log.i(TAG, _job.getTitle() + " has been added to the list!");
            }

        }



        // mRecommendations.addAll(databaseHelper.getRecommendationsListByCategory(tag.getText()));


        return jobs;
    }


    private void startFadeInAnimation() {

        mRecycler.animate()
                .alpha(1)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(500)
                .setDuration(500)
                .start();
    }
















    /////////////////////////////////////////////////////////////////////////


    JobsAdapter.OnItemClickListener onItemClickListener = new JobsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            Log.i(TAG, "Item clicked at " + position);
            Intent intent = new Intent(getActivity(), DetailedJobListingActivity.class);

            intent.putExtra("job", new Gson().toJson(JOBS.get(position)));


            RelativeLayout parent_layout = (RelativeLayout) view.findViewById(R.id.parent_layout);

            TextView job_titleTextView = (TextView) view.findViewById(R.id.job_title);
            TextView company_nameTextView = (TextView) view.findViewById(R.id.company_name);
            TextView body =  view.findViewById(R.id.body);

            Pair<View, String> parent = Pair.create((View) parent_layout, "trex_parent");
            Pair<View, String> title = Pair.create((View) job_titleTextView, "trex_title");
            Pair<View, String> company_name = Pair.create((View) company_nameTextView, "trex_company_name");
            Pair<View, String> boby = Pair.create((View) body, "trex_body");


            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                     parent, company_name, title, boby);

            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());


        }
    };











}

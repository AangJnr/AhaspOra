package com.app.ahaspora.activities;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.ahaspora.BottomDialog;
import com.app.ahaspora.R;
import com.app.ahaspora.adapters.CommentsRecyclerAdapter;
import com.app.ahaspora.models.Author;
import com.app.ahaspora.models.Comment;
import com.app.ahaspora.models.Event;
import com.app.ahaspora.models.Recommendation;
import com.app.ahaspora.utilities.CommentBottomDialog;
import com.app.ahaspora.utilities.Constants;
import com.app.ahaspora.utilities.DateUtil;
import com.app.ahaspora.utilities.PermissionsUtil;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aangjnr on 21/02/2018.
 */

public class DetailedEventActivity extends BaseActivity{


    Event EVENT;
    CardView eventCardView;
    TextView title;
    TextView location;
    TextView dateTime;
    ImageView image;
    TextView comment;
    TextView body;
    TextView going;
    TextView notGoing;
    RecyclerView recyclerView;
    View mLayoutBottomSheet;
    BottomSheetBehavior mBottomSheetBehavior;
    Boolean isGoing;
    int i = 0;
    CommentsRecyclerAdapter commentsAdapter;
    TextView postButton;

    FloatingActionButton floatingActionButton;
    NestedScrollView nestedScrollView;
    EditText commentEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EVENT = new Gson().fromJson(getIntent().getStringExtra("event"), Event.class);
        setLightStatusBar(getWindow().getDecorView(), this);
        setContentView(R.layout.activity_detailed_event);

        mLayoutBottomSheet = findViewById(R.id.bottomSheet);


       // mLayoutBottomSheet.findViewById(R.id.comment_section).setBackgroundColor(ContextCompat.getColor(this, R.color.white2));
        mBottomSheetBehavior = BottomSheetBehavior.from(mLayoutBottomSheet);

        floatingActionButton = findViewById(R.id.fab);
        title = findViewById(R.id.title);
        location = findViewById(R.id.location);
        dateTime = findViewById(R.id.datetime);
        comment = findViewById(R.id.comment);
        body = findViewById(R.id.body);
        going = findViewById(R.id.going);
        notGoing = findViewById(R.id.not_going);
        eventCardView = findViewById(R.id.event_card_view);

        image = findViewById(R.id.image);
        recyclerView = findViewById(R.id.recycler_view);

        nestedScrollView = findViewById(R.id.scroll_view);


        if(EVENT != null){

           setUiViewAndClickListeners();


        }


    }




    @Override
    protected void onStop() {
        clearLightStatusBar(this, getWindow().getDecorView());
        super.onStop();
    }


    @Override
    public void onBackPressed() {

        if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        else super.onBackPressed();


    }

    void showBottomSheet() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }




    void setUiViewAndClickListeners(){


        if(EVENT.getImage() != null && !EVENT.getImage().isEmpty() && !EVENT.getImage().equalsIgnoreCase(""))
            Picasso.with(this).load(EVENT.getImage()).into(image);



        title.setText(EVENT.getTitle());
        title.setTextColor((EVENT.getColor() != 0) ? EVENT.getColor() : ContextCompat.getColor(this, R.color.colorAccent));
        location.setText(EVENT.getLocation().getName());
        dateTime.setText(DateUtil.reformatDate(EVENT.getTime().getStart()));

        eventCardView.setCardBackgroundColor((EVENT.getColor() != 0) ? EVENT.getColor() : ContextCompat.getColor(this, R.color.colorAccent));

        if(EVENT.getBody()!= null && !EVENT.getBody().isEmpty())
        body.setText(Html.fromHtml(EVENT.getBody()));


        isGoing = EVENT.getGoing();

        if(isGoing != null){
            if(isGoing) {
               going.setTextColor((EVENT.getColor() != 0) ? EVENT.getColor() : ContextCompat.getColor(this, R.color.white));

                going.setEnabled(false);

            }
            else {
               notGoing.setTextColor((EVENT.getColor() != 0) ? EVENT.getColor() : ContextCompat.getColor(this, R.color.white));

                notGoing.setEnabled(false);

            }
        }



        if(EVENT.getComments() != null && EVENT.getComments().size() > 0) {
            commentsAdapter = new CommentsRecyclerAdapter(this, EVENT.getComments());

        }else{

            commentsAdapter = new CommentsRecyclerAdapter(this, new ArrayList<Comment>());


        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentsAdapter);




        going.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EVENT.setGoing(true);
                //going.setTextColor((EVENT.getColor() != 0) ? EVENT.getColor() : ContextCompat.getColor(DetailedEventActivity.this, R.color.colorAccent));
                going.setTextColor(ContextCompat.getColor(DetailedEventActivity.this, R.color.white));

                going.setEnabled(false);

                notGoing.setTextColor(ContextCompat.getColor(DetailedEventActivity.this, R.color.text_white_75));
                notGoing.setEnabled(true);

            }
        });



        notGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EVENT.setGoing(true);
                notGoing.setTextColor(ContextCompat.getColor(DetailedEventActivity.this, R.color.white));
                notGoing.setEnabled(false);

                going.setTextColor(ContextCompat.getColor(DetailedEventActivity.this, R.color.text_white_75));
                going.setEnabled(true);

            }
        });





        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showBottomSheet();

            }
        });


        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                floatingActionButton.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }
        });




        commentEditText = mLayoutBottomSheet.findViewById(R.id.comment_edittext);
        postButton = mLayoutBottomSheet.findViewById(R.id.post);
        postButton.setEnabled(true);
        CircleImageView usersImage = mLayoutBottomSheet.findViewById(R.id.user_image);
        final ProgressBar progressBar = mLayoutBottomSheet.findViewById(R.id.progress_bar);


        selectedImage = mLayoutBottomSheet.findViewById(R.id.select_image);
        imageLayout = mLayoutBottomSheet.findViewById(R.id.imageRl);
        selectedImage = mLayoutBottomSheet.findViewById(R.id.selectedImage);
        removeImage = mLayoutBottomSheet.findViewById(R.id.removeImage);



        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageLayout.setVisibility(View.GONE);
                selectedImage.setImageBitmap(null);
                imageUri = null;

            }
        });







        findViewById(R.id.photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionsUtil.checkPermission(
                        DetailedEventActivity.this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                        },
                        new PermissionsUtil.OnPermissionCallback() {
                            @Override
                            public void onPermissionGranted() {

                                showBottomDialog();
                            }


                            @Override
                            public void onPermissionDenied() {

                            }
                        }
                );
            }
        });


        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                postButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);




                Comment comment = new Comment();
                comment.setCreatedAt("2018-02-21 7:05:19");
                comment.setId(++i);
                comment.setAuthor(new Author("Aang Jnr", "https://avatars3.githubusercontent.com/u/8545691?s=400&u=351462146b0ba9b01661b4857adb9b657be932d4&v=4"));
                comment.setImage(String.valueOf(imageUri));
                comment.setBody(commentEditText.getText().toString());

                List<Comment> commentList = commentsAdapter.getComments();
                commentList.add(comment);


                commentEditText.setText("");
                imageLayout.setVisibility(View.GONE);
                selectedImage.setImageBitmap(null);
                imageUri = null;



                commentsAdapter.setComments(commentList);
                commentsAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                postButton.setVisibility(View.VISIBLE);



                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


                    }
                },1000);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nestedScrollView.smoothScrollBy(0, View.FOCUS_DOWN);


                    }
                },1500);


            }
        });


        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {


                    postButton.setEnabled(true);

                    postButton.setTextColor(ContextCompat.getColor(DetailedEventActivity.this, R.color.colorAccent));



                } else if (s.length() <= 0) {



                    postButton.setEnabled(false);
                    postButton.setTextColor(ContextCompat.getColor(DetailedEventActivity.this, R.color.divider_2));

                }


            }
        });
    }

}

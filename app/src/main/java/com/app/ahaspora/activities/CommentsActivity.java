package com.app.ahaspora.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ahaspora.R;
import com.app.ahaspora.adapters.CommentsRecyclerAdapter;
import com.app.ahaspora.models.Author;
import com.app.ahaspora.models.Comment;
import com.app.ahaspora.models.Post;
import com.app.ahaspora.utilities.CustomToast;
import com.app.ahaspora.utilities.DateUtil;
import com.app.ahaspora.utilities.PermissionsUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aangjnr on 15/11/2017.
 */

public class CommentsActivity extends BaseActivity {


    Post feedItem = null;
    Boolean didIStarThisPost = false;
    EditText commentEditText;
    TextView postButton;
    CircleImageView usersImage;

    Boolean isCommenting;

    NestedScrollView nestedScrollView;
    ProgressBar progressBar;
    RecyclerView commentsRecycler;

    CommentsRecyclerAdapter commentsAdapter;
    int i = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);


        Intent intent = getIntent();

        isCommenting = intent.getBooleanExtra("isCommenting", false);

        feedItem = new Gson().fromJson(intent.getStringExtra("feedItem"), Post.class);

        if (feedItem != null) {
            setUpUi();

        }

    }


    void setUpUi() {

        imageLayout = (RelativeLayout) findViewById(R.id.imageRl);
        selectedImage = (ImageView) findViewById(R.id.selectedImage);
        removeImage = (ImageView) findViewById(R.id.removeImage);
        postButton = (TextView) findViewById(R.id.post);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        //progressBar.setVisibility(View.GONE);

        commentsRecycler = findViewById(R.id.comments_recycler);


        nestedScrollView = findViewById(R.id.nested_scroll);

        commentEditText = findViewById(R.id.comment_edittext);
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

                    if (!postButton.isEnabled()) {

                        postButton.setEnabled(true);
                        postButton.setTextColor(ContextCompat.getColor(CommentsActivity.this, R.color.colorAccent));

                    }

                } else if (s.length() <= 0) {

                    if (postButton.isEnabled()) {
                        postButton.setEnabled(false);
                        postButton.setTextColor(ContextCompat.getColor(CommentsActivity.this, R.color.divider_2));
                    }

                }


            }
        });


        usersImage = (CircleImageView) findViewById(R.id.user_image);




        TextView posteeName = (TextView) findViewById(R.id.posters_name);
        TextView datePosted = (TextView) findViewById(R.id.date_posted);
       /* TextView location = (TextView) findViewById(R.id.location);
        TextView follow = (TextView) findViewById(R.id.follow);*/
        TextView status = (TextView) findViewById(R.id.status);

        TextView numberOfComments = (TextView) findViewById(R.id.number_of_comments);
        TextView commentsText = (TextView) findViewById(R.id.textview2);

        //TextView numberOfStars = (TextView) findViewById(R.id.number_of_stars);


        final ImageView star = (ImageView) findViewById(R.id.star);
        //ImageView share = (ImageView) findViewById(R.id.share);
        ImageView postImage = (ImageView) findViewById(R.id.selected_image);
        CircleImageView postersImage = (CircleImageView) findViewById(R.id.postee_image_view);

       /* if (feedItem.getAuthor().getAvatar() != null && !feedItem.getAuthor().getAvatar().equalsIgnoreCase("users/default.png"))
            Picasso.with(this)
                .load(feedItem.getAuthor().getAvatar())
                .resize(100, 100)
                .centerCrop()
                .into(postersImage);
*/

        if (feedItem.getImage() != null) {
            postImage.setVisibility(View.VISIBLE);

            Picasso.with(this)
                    .load(feedItem.getImage())
                    .resize(400, 300)
                    .into(postImage);
        }


        posteeName.setText(feedItem.getAuthor().getName());
        datePosted.setText(DateUtil.convertStringToPrettyTime(feedItem.getCreatedAt()));
        status.setText(feedItem.getBody());


        if (feedItem.getComments() != null && feedItem.getComments().size() > 0) {

            int commentsNo = feedItem.getComments().size();
            numberOfComments.setText(commentsNo + "");
            commentsText.setText((commentsNo > 1) ? "c o m m e n t s" : "c o m m e n t");

        }



        if (isCommenting)
            commentEditText.requestFocus();


        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!didIStarThisPost) {


                    animateStarButton(star);
                    didIStarThisPost = !didIStarThisPost;
                    CustomToast.makeToast(CommentsActivity.this, "Item starred!", Toast.LENGTH_SHORT).show();

                } else {

                    star.setImageResource(R.drawable.ic_star_border_grey_400_24dp);
                    didIStarThisPost = !didIStarThisPost;
                }
            }
        });


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
                        CommentsActivity.this,
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



                postButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                hideSoftKeyboard();

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


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nestedScrollView.smoothScrollBy(0, View.FOCUS_DOWN);

                        progressBar.setVisibility(View.GONE);
                        postButton.setVisibility(View.VISIBLE);

                    }
                },1000);


            }
        });


        if(feedItem.getComments() != null && feedItem.getComments().size() > 0) {
            commentsAdapter = new CommentsRecyclerAdapter(this, feedItem.getComments());

        }else{
            commentsAdapter = new CommentsRecyclerAdapter(this, new ArrayList<Comment>());

        }

            commentsAdapter.setHasStableIds(true);
            commentsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
            commentsRecycler.setAdapter(commentsAdapter);
            commentsRecycler.scrollToPosition(0);




    }



    private void animateStarButton(final ImageView holder) {


        AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
        OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder, "rotation", 0f, 360f);
        rotationAnim.setDuration(300);
        rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(300);
        bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(300);
        bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                holder.setImageResource(R.drawable.ic_star_yellow_a700_18dp);
            }


            @Override
            public void onAnimationEnd(Animator animation) {
                //heartAnimationsMap.remove(holder);
                // dispatchChangeFinishedIfAllAnimationsEnded(holder);

                holder.clearAnimation();
            }
        });

        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
        animatorSet.start();

    }



}

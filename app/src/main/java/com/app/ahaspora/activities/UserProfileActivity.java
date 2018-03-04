package com.app.ahaspora.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ahaspora.R;
import com.app.ahaspora.models.Post;
import com.app.ahaspora.utilities.Constants;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aangjnr on 15/11/2017.
 */

public class UserProfileActivity extends BaseActivity {

     CircleImageView userImage;
    View background;
    View customTolbar;

    TextView rank;

    Post post;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();


        post = new Gson().fromJson(intent.getStringExtra("feedItem"), Post.class);


        if(post != null) {
            userImage = (CircleImageView) findViewById(R.id.user_image);
            background = findViewById(R.id.background);
            customTolbar = this.findViewById(R.id.appBar);
            rank = (TextView) findViewById(R.id.rank);

            TextView name = (TextView) findViewById(R.id.name);
            TextView tagline = (TextView) findViewById(R.id.tagline);


            name.setText(post.getAuthor().getName());


          /*  name.setText(user.getName());
            tagline.setText(user.getTagline());

            rank.setText(getLevel(user.getLevelNo()));


            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(10);
            drawable.setColor(getLevelColorBAckground(getLevel(user.getLevelNo())));

            rank.setBackground(drawable);*/


            Picasso.with(this).load(R.drawable.placeholder2)
                    .into(userImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            //use your bitmap or something

                            Bitmap photo = ((BitmapDrawable) userImage.getDrawable()).getBitmap();

                            Palette.Builder builder = new Palette.Builder(photo);
                            builder.generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {

                                    int mutedLight = palette.getDarkVibrantColor(getResources().getColor(R.color.colorPrimary));

                                    background.setBackgroundColor(mutedLight);
                                    customTolbar.setBackgroundColor(mutedLight);


                                    GradientDrawable drawable = new GradientDrawable();
                                    drawable.setCornerRadius(10);
                                    drawable.setColor(mutedLight);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        getWindow().setStatusBarColor(mutedLight);
                                        rank.setBackground(drawable);

                                    } else
                                        rank.setBackgroundDrawable(drawable);

                                }
                            });
                        }

                        @Override
                        public void onError() {

                        }
                    });

        }




    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

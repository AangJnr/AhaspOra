package com.app.ahaspora.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amirarcane.recentimages.RecentImages;
import com.amirarcane.recentimages.thumbnailOptions.ImageAdapter;
import com.app.ahaspora.R;
import com.app.ahaspora.models.Author;
import com.app.ahaspora.models.Post;
import com.app.ahaspora.utilities.Callbacks;
import com.app.ahaspora.utilities.Constants;
import com.app.ahaspora.utilities.CustomToast;
import com.app.ahaspora.utilities.PermissionsUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
 import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jess.ui.TwoWayAdapterView;
import com.jess.ui.TwoWayGridView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by aangjnr on 13/11/2017.
 */

public class StatusUpdateActivity extends BaseActivity {


    static Callbacks.UpdateFeedAdapterListener updateFeedAdapterListener;
    Button postButton;
    EditText statusText;
    ProgressBar progressBar;





    public static void OnFeedItemUpdatedistener(Callbacks.UpdateFeedAdapterListener listener) {

        updateFeedAdapterListener = listener;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_update);


        imageUri = null;
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        postButton = (Button) findViewById(R.id.postButton);
        postButton.setEnabled(false);
        imageLayout = (RelativeLayout) findViewById(R.id.imageRl);
        selectedImage =   findViewById(R.id.selectedImage);
        removeImage =   findViewById(R.id.removeImage);



        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                postFeedWithImage();


            }
        });


        findViewById(R.id.showimageGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PermissionsUtil.checkPermission(
                        StatusUpdateActivity.this,
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


        findViewById(R.id.ll2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PermissionsUtil.checkPermission(
                        StatusUpdateActivity.this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        new PermissionsUtil.OnPermissionCallback() {
                            @Override
                            public void onPermissionGranted() {

                                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                                if (!(manager != null && manager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {

                                    showAlertDialog(false, "GPS is disabled", "Would you like to enable it?", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 600);


                                        }
                                    }, "OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                        }
                                    }, "CANCEL", 0);


                                } else {
                                    showPlacesDialog();
                                }
                            }


                            @Override
                            public void onPermissionDenied() {


                            }
                        }
                );

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


        statusText = (EditText) findViewById(R.id.status_edittext);
        statusText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {

                    if (!postButton.isEnabled()) postButton.setEnabled(true);

                } else if (s.length() <= 0)
                    if (postButton.isEnabled()) postButton.setEnabled(false);


            }
        });


        showSoftKeyboard();



    }








    @Override
    public void onBackPressed() {

        if (postButton.isEnabled())

            showAlertDialog(true, "", "Discard post?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();

                }
            }, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            }, "Cancel", 0);


        else finish();
    }




    void postFeedWithImage() {

        Post post = new Post();
        post.setAuthor(new Author("Aang Jnr", "https://avatars3.githubusercontent.com/u/8545691?s=400&u=351462146b0ba9b01661b4857adb9b657be932d4&v=4"));
        post.setBody(statusText.getText().toString());
        post.setImage(String.valueOf(imageUri));
        post.setCreatedAt("2018-2-3 21:57:52");

        updateFeedAdapterListener.onFeedItemAdded(post);

        finish();








    }


}

package com.app.ahaspora.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amirarcane.recentimages.RecentImages;
import com.amirarcane.recentimages.thumbnailOptions.ImageAdapter;
import com.app.ahaspora.NetworkBroadcastReceiver;
import com.app.ahaspora.R;
 import com.app.ahaspora.utilities.Constants;
import com.app.ahaspora.utilities.CustomToast;
import com.app.ahaspora.utilities.DatabaseHelper;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.jess.ui.TwoWayAdapterView;
import com.jess.ui.TwoWayGridView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by aangjnr on 11/03/2017.
 */

public class BaseActivity extends AppCompatActivity {

    String TAG = BaseActivity.class.getSimpleName();
    DatabaseHelper databaseHelper;
    NetworkBroadcastReceiver networkChangeReceiver = new NetworkBroadcastReceiver();
    RecentImages recentImages;
    Bitmap postImageBitmap = null;
     ContentResolver contentResolver;
    File photoFile = null;
    RelativeLayout imageLayout;
    ImageView selectedImage;
    ImageView removeImage;
    Uri imageUri;






    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = DatabaseHelper.getInstance(this);
        //databaseHelper.deleteRecommendationsTable();
       // databaseHelper.deleteCategoriesTable();
    }

    public void showAlertDialog(@Nullable String title, @Nullable String message,
                                @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                @NonNull String positiveText,
                                @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                @NonNull String negativeText, @Nullable int icon_drawable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppDialog);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setIcon(icon_drawable);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        builder.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        supportFinishAfterTransition();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                // todo: goto back activity from here
                //super.onBackPressed();
                supportFinishAfterTransition();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    public void showAlertDialog(Boolean cancelable, @Nullable String title, @Nullable String message,
                                @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                @NonNull String positiveText,
                                @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                @NonNull String negativeText, @Nullable int icon_drawable) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppDialog);
        builder.setTitle(title);
        builder.setCancelable(cancelable);


        if (icon_drawable != 0) builder.setIcon(icon_drawable);
        builder.setMessage(message);

        if (onPositiveButtonClickListener != null)
            builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        if (onNegativeButtonClickListener != null)
            builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        builder.show();
    }



    public void showPlacesDialog() {


        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            this.startActivityForResult(builder.build(this), Constants.PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }






    public int dimen(@DimenRes int resId) {
        return (int) getResources().getDimension(resId);
    }

    public int color(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    public int integer(@IntegerRes int resId) {
        return getResources().getInteger(resId);
    }



    void showSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }



    public void onBackClicked(@Nullable View v){
        onBackPressed();
    }




    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "ON RESUME - setting broadcast");

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkChangeReceiver, filter);

        //showSnackBar();



    }


    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "ON PAUSE - removing  broadcast");

        unregisterReceiver(networkChangeReceiver);
    }


/*



    int method(String formula){

        List<String> plotResults = null;

         formula = "Collections.frequency(plotList, fail)";

         if(formula.startsWith("Collections.frequency(") && formula.contains("monitoring_result_plots_ghana")){

            for(Plot plot : plotList)  plotResults.add(plot.getResults());

            return Collections.frequency(plotResults, "fail");
         }else return -1;

    }
*/

    void showBottomDialog() {

        final View bottomSheet = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        final TwoWayGridView twoWayGridView = (TwoWayGridView) bottomSheet.findViewById(R.id.gridview);

        final Dialog mBottomSheetDialog = new Dialog(this, R.style.MaterialDialogSheet);

        mBottomSheetDialog.setContentView(bottomSheet);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

        contentResolver = this.getContentResolver();
        recentImages = new RecentImages();
        final ImageAdapter adapter = recentImages.getAdapter(this);


        mBottomSheetDialog.show();

        twoWayGridView.setAdapter(adapter);
        twoWayGridView.setOnItemClickListener(new TwoWayAdapterView.OnItemClickListener() {
            public void onItemClick(TwoWayAdapterView parent, View v, int position, long id) {
                imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                try {
                    int orientation = getOrientation(contentResolver, (int) id);
                    postImageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);
                    //d = getRotateDrawable(bitmap, orientation);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageLayout.animate().alpha(1f).setDuration(500).start();
                imageLayout.setVisibility(View.VISIBLE);
                Picasso.with(BaseActivity.this).load(imageUri).into(selectedImage);


                mBottomSheetDialog.dismiss();
            }
        });


        bottomSheet.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takeImage();
                mBottomSheetDialog.dismiss();


            }
        });


        bottomSheet.findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("imageView/*");
                startActivityForResult(photoPickerIntent, Constants.SELECT_PHOTO_REQUEST);
                mBottomSheetDialog.dismiss();


            }
        });

    }


    @Override
    protected void onDestroy() {

        if (recentImages != null)
            recentImages.cleanupCache();
        super.onDestroy();
    }


    private Drawable getRotateDrawable(final Bitmap b, final float angle) {
        final BitmapDrawable drawable = new BitmapDrawable(getResources(), b) {
            @Override
            public void draw(final Canvas canvas) {
                canvas.save();
                canvas.rotate(angle, b.getWidth() / 2, b.getHeight() / 2);
                super.draw(canvas);
                canvas.restore();
            }
        };
        return drawable;
    }


    private void takeImage() {
        Intent capturePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        capturePhotoIntent.putExtra("return-data", true);


        imageUri = null;


        try {
            //photoFile = createImageFile();

            imageUri = Uri.fromFile(createImageFile());


        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (imageUri != null) {


            capturePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            startActivityForResult(capturePhotoIntent, Constants.TAKE_PICTURE_REQUEST);
           /*
            Uri photoURI = FileProvider
                    .getUriForFile(StatusUpdateActivity.this, "com.piemicrosystems.hoodcop", photoFile);
           */


        }
    }


    private File createImageFile() throws IOException {
        // Create an imageView file name
        final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String timeStamp = DATE_FORMAT.format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File cacheDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                cacheDir      /* directory */
        );
    }


    private int getOrientation(ContentResolver cr, int id) {

        String photoID = String.valueOf(id);

        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.ORIENTATION}, MediaStore.Images.Media._ID + "=?",
                new String[]{"" + photoID}, null);
        int orientation = -1;

        if (cursor.getCount() != 1) {
            return -1;
        }

        if (cursor.moveToFirst()) {
            orientation = cursor.getInt(0);
        }
        cursor.close();
        return orientation;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.SELECT_PHOTO_REQUEST:
                    imageUri = data.getData();
                    if (imageUri != null) {

                        Log.d("SELECT PHOTO REQUEST ", imageUri.toString());


                        try {
                            Log.d("ImageURI", String.valueOf(imageUri));
                            postImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        imageLayout.animate().alpha(1f).setDuration(500).start();
                        imageLayout.setVisibility(View.VISIBLE);

                        Picasso.with(this).load(imageUri).into(selectedImage);

                        //selectedImage.setImageBitmap(bitmap);



                    } else
                        CustomToast.makeToast(this, "Could not load image, please try again", Toast.LENGTH_SHORT).show();

                    break;
                case Constants.TAKE_PICTURE_REQUEST:
                   /* Uri imageUri = null;

                    if (data.getData() != null) {
                        imageUri = data.getData();
                    }*/

                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        postImageBitmap = (Bitmap) extras.get("data");
                    }


                    if (imageUri != null) {

                        Log.d("CAMERA REQUEST ", imageUri.toString());


                        imageLayout.animate().alpha(1f).setDuration(500).start();
                        imageLayout.setVisibility(View.VISIBLE);

                        Picasso.with(this).load(imageUri).into(selectedImage);
                    } else
                        CustomToast.makeToast(this, "Could not load image, please try again", Toast.LENGTH_SHORT).show();

                    break;


                case Constants.PLACE_PICKER_REQUEST:


                    final Place place = PlacePicker.getPlace(this, data);


                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
                    View mView = layoutInflaterAndroid.inflate(R.layout.custom_edittext, null);
                    android.app.AlertDialog.Builder alertDialogBuilderUserInput = new android.app.AlertDialog.Builder(this, R.style.AppAlertDialog);
                    alertDialogBuilderUserInput.setView(mView);

                    alertDialogBuilderUserInput
                            .setCancelable(false);
                    final android.app.AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();


                    final EditText edittext = (EditText) mView.findViewById(R.id.name_edittext);
                    edittext.setHint(place.getName().toString());
                    TextView ok = (TextView) mView.findViewById(R.id.ok);
                    TextView cancel = (TextView) mView.findViewById(R.id.cancel);


                    alertDialogAndroid.show();


                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            String srt = edittext.getEditableText().toString();

                            String id = String.valueOf(System.currentTimeMillis());


                            Log.i("Base Activity", "Place name is " + place.getName());
/*

                            Address newAddress = new Address(id, (srt.isEmpty()) ? place.getName().toString() : srt, place.getAddress().toString(),
                                    String.valueOf(place.getLatLng().latitude), String.valueOf(place.getLatLng().longitude));

*/

                            alertDialogAndroid.dismiss();


                            TextView locationText = (TextView) findViewById(R.id.location);
                            //locationText.setText(newAddress.getName());

                            locationText.setTextColor(ContextCompat.getColor(BaseActivity.this, R.color.colorAccent));


                            ImageView locationIcon = (ImageView) findViewById(R.id.locationImage);
                            locationIcon.setColorFilter(ContextCompat.getColor(BaseActivity.this, R.color.colorAccent));


                        }
                    });


                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialogAndroid.dismiss();


                        }
                    });


                    break;

            }
        }
    }




    void startGoogleDirectionIntent(@Nullable LatLng source, LatLng destination, String nameOfPlace) {

/*

        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=" + source.latitude + "," + source.longitude + "+to:" + destination.latitude + ","
                + destination.longitude + " (%s)", 12f, 2f, "On Route");

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                CustomToast.makeToast(this, "Please install Google Maps from the PlayStore", Toast.LENGTH_SHORT).show();

            }
        }
*/






        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", destination.latitude, destination.longitude, nameOfPlace);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        try
        {
            startActivity(intent);
        }
        catch(ActivityNotFoundException ex)
        {
            try
            {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            }
            catch(ActivityNotFoundException innerEx)
            {
                Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }




    void startEmailIntent(String email, String jobType){

        try{


            Intent intent = new Intent(Intent.ACTION_SENDTO);

            intent.setType("text/plain");
            intent.setData(Uri.parse("mailto:" + email));
            intent.putExtra(Intent.EXTRA_EMAIL, email);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Application Letter for " + jobType);
            //intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

            startActivity(Intent.createChooser(intent, "Send Email"));

        } catch (ActivityNotFoundException e) {


            e.printStackTrace();
        }

    }


    void startBrowserIntent(String url){

        Log.i(TAG, "URL IS " + url);

        if (!url.startsWith("https://") && !url.startsWith("http://")){
            url = "http://" + url;
        }


        try{
            Intent i = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));
            startActivity(i);

        } catch (ActivityNotFoundException e) {

            e.printStackTrace();

        }




    }



    void startTwitterIntent(String twitterUrl){

        Log.i(TAG, "URL IS " + twitterUrl);

        Intent intent = null;
        try {
            // get the Twitter app if possible
            this.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 this.startActivity(intent);

        } catch (ActivityNotFoundException e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl));
            this.startActivity(intent);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }


    public static void clearLightStatusBar(Activity activity, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Window window = activity.getWindow();
            window.setStatusBarColor(ContextCompat
                    .getColor(activity,R.color.colorPrimaryDark));

        }
    }


    public static void setLightStatusBar(View view,Activity activity){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(Color.WHITE);
        }
    }

}

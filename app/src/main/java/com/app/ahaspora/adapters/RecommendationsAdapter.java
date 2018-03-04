package com.app.ahaspora.adapters;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.app.ahaspora.R;

import com.app.ahaspora.activities.MainActivity;
import com.app.ahaspora.fragments.BaseFragment;
import com.app.ahaspora.models.Recommendation;
import com.app.ahaspora.utilities.Constants;
import com.app.ahaspora.utilities.DatabaseHelper;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;
import java.util.List;


/**
 * Created by aangjnr on 01/03/2017.
 */


public class RecommendationsAdapter extends RecyclerView.Adapter<RecommendationsAdapter.RecommendationsViewHolder>   {

    static RecommendationsAdapter.OnItemClickListener mItemClickListener;

    String TAG = RecommendationsAdapter.class.getSimpleName();
    List<Recommendation> recommendations;
    Context context;
    DatabaseHelper databaseHelper;


    public void setOnItemClickListener(final RecommendationsAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }




    public RecommendationsAdapter(Context c,  List<Recommendation> _recommendations) {

        this.context = c;
        this.recommendations = _recommendations;


        databaseHelper = DatabaseHelper.getInstance(context);


    }

    public void setRecommendations(List<Recommendation> _recommendations) {
        this.recommendations = _recommendations;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }




    @Override
    public RecommendationsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recommedation_item_view, viewGroup, false);

        return new RecommendationsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecommendationsViewHolder holder, int position) {

        Recommendation recommendation = recommendations.get(position);

        holder.title.setText(Html.fromHtml(recommendation.getTitle()));

        if(recommendation.getTagline() != null)
            holder.tagline.setText(Html.fromHtml(recommendation.getTagline()));

        if(recommendation.getImage() != null && !recommendation.getImage().equalsIgnoreCase("null") && !recommendation.getImage().equalsIgnoreCase(""))
            Picasso.with(context)
                    .load(recommendation.getImage())
                    .resize(100, 100)
                    .centerCrop()
                    .into(holder.imageLogo);



        String categoryName =  recommendation.getCategory().getName();

        holder.genre.setText(categoryName);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(1000);
             drawable.setColor(BaseFragment.getColor(categoryName));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.genre.setBackground(drawable);
        }else {
            holder.genre.setBackgroundDrawable(drawable);
        }

       // holder.genre.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, BaseFragment.getDrawable(categoryName)), null, null, null );

        if(recommendation.getContact().getWebsite() == null || recommendation.getContact().getWebsite().isEmpty() || recommendation.getContact().getWebsite().equalsIgnoreCase(""))
            holder.website.setVisibility(View.GONE);

        if(recommendation.getContact().getEmail() == null || recommendation.getContact().getEmail().isEmpty() || recommendation.getContact().getEmail().equalsIgnoreCase(""))
            holder.email.setVisibility(View.GONE);


        if(recommendation.getContact().getPhone() == null || recommendation.getContact().getPhone().isEmpty() || recommendation.getContact().getPhone().equalsIgnoreCase(""))
            holder.call.setVisibility(View.GONE);




    }


    private int getColor(int color) {
        return ContextCompat.getColor(context, color);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int getItemCount() {
        return recommendations.size();

    }


    @Override
    public long getItemId(int position) {


        return position;


    }




    class RecommendationsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView title;
        TextView tagline;
        TextView genre;
        RelativeLayout rating;

        LikeButton like;

        ImageView call;
        ImageView email;
        ImageView website;

        ImageView imageLogo;

        RelativeLayout parentLayout;





        RecommendationsViewHolder(View itemView) {
            super(itemView);


            title =   itemView.findViewById(R.id.title);
            tagline =  itemView.findViewById(R.id.tagline);

            genre =  itemView.findViewById(R.id.genre);

            rating =  itemView.findViewById(R.id.layout_1);

            like =  itemView.findViewById(R.id.like);

            call =  itemView.findViewById(R.id.call);
            email =  itemView.findViewById(R.id.email);
            website =  itemView.findViewById(R.id.website);


            imageLogo =  itemView.findViewById(R.id.image_logo);

            parentLayout = itemView.findViewById(R.id.parent_layout);


            call.setOnClickListener(this);
            email.setOnClickListener(this);
            website.setOnClickListener(this);
            rating.setOnClickListener(this);

            parentLayout.setOnClickListener(this);


            // like.setOnClickListener(this);
            // dislike.setOnClickListener(this);



            like.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {

                }

                @Override
                public void unLiked(LikeButton likeButton)
                {

                 }
            });



        }

        @Override
        public void onClick(View view) {

            final Recommendation r = recommendations.get(getAdapterPosition());

            switch (view.getId()) {


                case (R.id.parent_layout):
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(itemView, getAdapterPosition());


                    }


                    break;

                case (R.id.email):

                    startEmailIntent(r.getContact().getEmail(), r.getTitle());
                    break;

                case (R.id.website):

                    startBrowserIntent(r.getContact().getWebsite());

                    break;

                case (R.id.layout_1):
                    showRatingDialog();
                    break;

                case R.id.call:
                    //Show place phone call dialog


                    showAlertDialog("Place a phone call?", "Charges do apply", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            placeCallIntent(r.getContact().getPhone());

                        }
                    }, "YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    }, "NO", R.drawable.ic_phone_custom_24dp);


                    break;
            }


        }


    }


    void showAlertDialog(@Nullable String title, @Nullable String message,
                         @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                         @NonNull String positiveText, @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener, @NonNull String negativeText,
                         @Nullable int icon_drawable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppDialog);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setIcon(icon_drawable);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        builder.show();
    }



    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Rate")
                .setNegativeButtonText("Cancel")
                //.setNeutralButtonText("Later")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(1)
                .setTitle("Rate this recommendation")
                .setDescription("Please select some stars and give your feedback")
                //.setDefaultComment("This app is pretty cool !")
                .setStarColor(R.color.colorAccent)
                .setNoteDescriptionTextColor(R.color.colorAccent)
                //.setTitleTextColor(R.color.titleTextColor)
                //.setDescriptionTextColor(R.color.contentTextColor)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.divider_2)
                .setCommentTextColor(R.color.text_black_87)
                //.setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .create((FragmentActivity) context)
                .show();
    }



    void startEmailIntent(String email, String jobType){

        try{


            Intent intent = new Intent(Intent.ACTION_SENDTO);

            intent.setType("text/plain");
            intent.setData(Uri.parse("mailto:" + email));
            intent.putExtra(Intent.EXTRA_EMAIL, email);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Application Letter for " + jobType);
            //intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

            context.startActivity(Intent.createChooser(intent, "Send Email"));

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
            context.startActivity(i);

        } catch (ActivityNotFoundException e) {

            e.printStackTrace();

        }


    }



    void placeCallIntent(String phoneNumber) {


        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        context.startActivity(intent);


    }





}







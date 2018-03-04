package com.app.ahaspora.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ahaspora.R;
import com.app.ahaspora.activities.CommentsActivity;
import com.app.ahaspora.activities.ImageViewActivity;
import com.app.ahaspora.activities.UserProfileActivity;
import com.app.ahaspora.models.Post;
import com.app.ahaspora.utilities.CustomToast;
import com.app.ahaspora.utilities.DateUtil;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aangjnr on 07/06/2017.
 */

public class PostsRecyclerAdapter extends RecyclerView.Adapter<PostsRecyclerAdapter.FeedItemsViewHolder> {

    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    static OnItemClickListener mItemClickListener;
    String TAG = PostsRecyclerAdapter.class.getSimpleName();
    List<Post> feedItems;
    Context mContext;
    boolean isStarred = false;
    Map<RecyclerView.ViewHolder, AnimatorSet> heartAnimationsMap = new HashMap<>();


    public PostsRecyclerAdapter(Context c, List<Post> _feedItems) {

        this.mContext = c;
        this.feedItems = _feedItems;

        hasStableIds();


    }

    @Override
    public PostsRecyclerAdapter.FeedItemsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feed_item, viewGroup, false);

        return new PostsRecyclerAdapter.FeedItemsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PostsRecyclerAdapter.FeedItemsViewHolder holder, int position) {

        Post feedItem = feedItems.get(position);





       /* if (feedItem.getAuthor().getAvatar() != null && !feedItem.getAuthor().getAvatar().equalsIgnoreCase("users/default.png"))
            Picasso.with(mContext)
                    .load(feedItem.getAuthor().getAvatar())
                    .resize(100, 100)
                    .centerCrop()
                    .into(holder.authorsAvatar);*/


        if (feedItem.getImage() != null) {
            holder.postImage.setVisibility(View.VISIBLE);

            Picasso.with(mContext)
                    .load(feedItem.getImage())
                    .resize(400, 300)
                    .into(holder.postImage);
        }


        holder.posteeName.setText(feedItem.getAuthor().getName());
        holder.datePosted.setText(DateUtil.convertStringToPrettyTime(feedItem.getCreatedAt()));
        if(feedItem.getBody() != null)
            holder.status.setText(Html.fromHtml(feedItem.getBody()));


        if (feedItem.getComments() != null && feedItem.getComments().size() > 0) {

            int commentsNo = feedItem.getComments().size();
            holder.numberOfComments.setText(commentsNo + "");
            holder.commentsText.setText((commentsNo > 1) ? "c o m m e n t s" : "c o m m e n t");

        }




            //holder.numberOfStars.setText("2");






    }


    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getItemCount() {
        return feedItems.size();

    }


////////////////////////////////////////////////////////////////////////////////////////////////////



    /*@Override
    public long getItemId(int position) {


        return position;


    }*/




    private void animateStarButton(final ImageView holder) {
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
                holder.setImageResource(R.drawable.ic_star_black_24dp);
               // holder.setColorFilter(ContextCompat.getColor(mContext, R.color.colorAccent));
            }


            @Override
            public void onAnimationEnd(Animator animation) {
                //heartAnimationsMap.remove(holder);
                // dispatchChangeFinishedIfAllAnimationsEnded(holder);
            }
        });

        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
        animatorSet.start();

    }


    void showAlertDialog(@Nullable String title, @Nullable String message,
                         @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                         @NonNull String positiveText,
                         @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                         @NonNull String negativeText, @Nullable int icon_drawable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AppDialog);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setIcon(icon_drawable);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        builder.show();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class FeedItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView posteeName;
        TextView datePosted;
        TextView location;
        TextView follow;
        TextView status;
        TextView numberOfComments;
        TextView commentsText;
        TextView numberOfStars;
        ImageView star;
        ImageView comments;
        ImageView postImage;
        CircleImageView authorsAvatar;
        private boolean didIStarThisPost = false;

        FeedItemsViewHolder(View mView) {
            super(mView);

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CommentsActivity.class);


                    intent.putExtra("isCommenting", false);
                    intent.putExtra("feedItem", new Gson().toJson(feedItems.get(getAdapterPosition())));
                    mContext.startActivity(intent);
                }
            });

             posteeName = (TextView) mView.findViewById(R.id.posters_name);
             datePosted = (TextView) mView.findViewById(R.id.date_posted);
             location = (TextView) mView.findViewById(R.id.location);
             follow = (TextView) mView.findViewById(R.id.follow);
             status = (TextView) mView.findViewById(R.id.status);

             numberOfComments = (TextView) mView.findViewById(R.id.number_of_comments);
             commentsText = (TextView) mView.findViewById(R.id.textview2);

             numberOfStars = (TextView) mView.findViewById(R.id.no_of_stars);


            star = (ImageView) mView.findViewById(R.id.star);
            comments = (ImageView) mView.findViewById(R.id.comment);
            postImage = (ImageView) mView.findViewById(R.id.selected_image);
            authorsAvatar =   mView.findViewById(R.id.author_image_view);



            mView.findViewById(R.id.number_of_comments_layout).setOnClickListener(this);
            comments.setOnClickListener(this);
            follow.setOnClickListener(this);
            postImage.setOnClickListener(this);
            authorsAvatar.setOnClickListener(this);
            star.setOnClickListener(this);






        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(mContext, CommentsActivity.class);

            switch (view.getId()) {

                case R.id.star:

                    if (!didIStarThisPost) {

                        starPost(star);


                    } else {

                        removeStarFromPost(star);
                    }

                    didIStarThisPost = !didIStarThisPost;


                    break;


                case R.id.number_of_comments_layout:

                    intent.putExtra("isCommenting", false);
                    intent.putExtra("feedItem", new Gson().toJson(feedItems.get(getAdapterPosition())));
                    mContext.startActivity(intent);


                    break;
                case R.id.comment:

                    intent.putExtra("isCommenting", true);
                    intent.putExtra("feedItem", new Gson().toJson(feedItems.get(getAdapterPosition())));
                    mContext.startActivity(intent);

                    break;


                case R.id.author_image_view:

                    Intent intent3 = new Intent(mContext, UserProfileActivity.class);
                    intent3.putExtra("feedItem", new Gson().toJson(feedItems.get(getAdapterPosition())));
                    mContext.startActivity(intent3);
                    break;



                case R.id.follow:

                    CustomToast.makeToast(mContext, "Follow this dude!!", Toast.LENGTH_SHORT).show();
                    break;


                case R.id.selected_image:
                Intent intent2 = new Intent(mContext, ImageViewActivity.class);
                intent2.putExtra("feedItem", new Gson().toJson(feedItems.get(getAdapterPosition())));
                mContext.startActivity(intent2);

                    break;


            }

        }


    }


    public List<Post> getPosts(){
        return feedItems;
    }


    public void setPosts(List<Post> posts){
        this.feedItems = posts;

    }



    void starPost(ImageView v) {
        animateStarButton(v);

    }

    void removeStarFromPost(ImageView star) {

        star.setImageResource(R.drawable.ic_star_border_grey_400_24dp);


    }

}



package com.app.ahaspora.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ahaspora.R;
import com.app.ahaspora.activities.CommentsActivity;
import com.app.ahaspora.activities.ImageViewActivity;
import com.app.ahaspora.models.Comment;
import com.app.ahaspora.models.Post;
import com.app.ahaspora.utilities.CustomToast;
import com.app.ahaspora.utilities.DateUtil;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aangjnr on 07/06/2017.
 */

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.FeedItemsViewHolder> {

    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    static OnItemClickListener mItemClickListener;
    String TAG = CommentsRecyclerAdapter.class.getSimpleName();
    List<Comment> commentsList;
    Context mContext;
    boolean isStarred = false;
    Map<RecyclerView.ViewHolder, AnimatorSet> heartAnimationsMap = new HashMap<>();


    public CommentsRecyclerAdapter(Context c, List<Comment> comments) {

        this.mContext = c;
        this.commentsList = comments;

        hasStableIds();


    }

    @Override
    public CommentsRecyclerAdapter.FeedItemsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comments_item_view, viewGroup, false);

        return new CommentsRecyclerAdapter.FeedItemsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentsRecyclerAdapter.FeedItemsViewHolder holder, int position) {

        Comment comment = commentsList.get(position);





        if (comment.getAuthor().getAvatar() != null && !comment.getAuthor().getAvatar().equalsIgnoreCase("users/default.png")
                && !comment.getAuthor().getAvatar().equalsIgnoreCase(""))
            Picasso.with(mContext)
                    .load(comment.getAuthor().getAvatar())
                    .resize(100, 100)
                    .centerCrop()
                    .into(holder.authorsAvatar);


        if (comment.getImage() != null && !comment.getImage().equalsIgnoreCase("") && !comment.getImage().equalsIgnoreCase("null")) {
            holder.postImage.setVisibility(View.VISIBLE);

            Picasso.with(mContext)
                    .load(comment.getImage())
                    .into(holder.postImage);


        }


        holder.posteeName.setText(comment.getAuthor().getName());
        holder.datePosted.setText(DateUtil.convertStringToPrettyTime(comment.getCreatedAt()));
        holder.body.setText(comment.getBody());

      /*  if (comment.getStars() != null && comment.getStars().size() > 0)
            numberOfStars.setText(comment.getStars().size());
        else numberOfStars.setVisibility(View.GONE);

*/



          //  holder.numberOfStars.setText("2");






    }


    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getItemCount() {
        return commentsList.size();

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
                holder.setColorFilter(ContextCompat.getColor(mContext, R.color.colorAccent));
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
        TextView body;
        TextView numberOfComments;
        TextView commentsText;
        ImageView star;
        ImageView comments;
        ImageView postImage;
        CircleImageView authorsAvatar;
        private boolean didIStarThisPost = false;
        TextView numberOfStars;

        FeedItemsViewHolder(View mView) {
            super(mView);


              //numberOfStars = (TextView) mView.findViewById(R.id.comment_stars_count);

             posteeName =   mView.findViewById(R.id.comment_posters_name);
            postImage =   mView.findViewById(R.id.post_image);

            datePosted =   mView.findViewById(R.id.comment_posted_time);

             body =   mView.findViewById(R.id.comment_posters_comment);

             /*numberOfStars = (TextView) mView.findViewById(R.id.no_of_stars);


              star = (ImageView) mView.findViewById(R.id.star);
*/
            authorsAvatar =   mView.findViewById(R.id.authors_image_view);



           // mView.findViewById(R.id.number_of_comments_layout).setOnClickListener(this);
            /*comments.setOnClickListener(this);
            follow.setOnClickListener(this);
            postImage.setOnClickListener(this);*/
            authorsAvatar.setOnClickListener(this);


            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    showCommentOptionsDialog();
                }
            });




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

                case R.id .postee_image_view:
/*

                    User user = mfeedItem.getPostee();

                    Intent intent3 = new Intent(mContext, UserProfileActivity.class);
                    intent3.putExtra("user", new Gson().toJson(user));

                    mContext.startActivity(intent3);
*/


                    break;



            }

        }


    }


    public List<Comment> getComments(){
        return commentsList;
    }


    public void setComments(List<Comment> posts){
        this.commentsList = posts;

    }



    void starPost(ImageView v) {
        animateStarButton(v);

    }

    void removeStarFromPost(ImageView star) {

        star.setImageResource(R.drawable.ic_star_border_grey_400_24dp);


    }




    void showCommentOptionsDialog() {
        List<String> options = new ArrayList<>();
        options.add("Star");
        options.add("Report this");


        AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext, R.style.AppDialog);
        builderSingle.setTitle("Comment Options");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_selectable_list_item, options);


        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String selection = arrayAdapter.getItem(which);

                if (selection.equals("Star comment")) {
                    //starComment();

                    CustomToast.makeToast(mContext, "Star comment", Toast.LENGTH_SHORT).show();


                } else if (selection.equals("Remove star")) {

                    CustomToast.makeToast(mContext, "Remove star comment", Toast.LENGTH_SHORT).show();

                    //removeStarFromComment();
                }


            }
        });
        builderSingle.show();


    }
}



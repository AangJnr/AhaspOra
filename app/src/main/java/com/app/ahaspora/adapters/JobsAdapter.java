package com.app.ahaspora.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
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
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ahaspora.R;
import com.app.ahaspora.fragments.BaseFragment;
import com.app.ahaspora.models.Job;
import com.app.ahaspora.models.Tag;
import com.app.ahaspora.utilities.CustomToast;
import com.app.ahaspora.utilities.DateUtil;
import com.app.ahaspora.utilities.ExpandableItemLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aangjnr on 07/06/2017.
 */

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.JobsViewHolder> {

    static OnItemClickListener mItemClickListener;
    String TAG = JobsAdapter.class.getSimpleName();
    List<Job> jobs;
    Context context;
    boolean isStarred = false;
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    Map<RecyclerView.ViewHolder, AnimatorSet> heartAnimationsMap = new HashMap<>();





    public JobsAdapter(Context c,  List<Job> _jobs) {

        this.context = c;
        this.jobs = _jobs;

        hasStableIds();


    }

    @Override
    public JobsAdapter.JobsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.job_item_view, viewGroup, false);

        return new JobsAdapter.JobsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(JobsAdapter.JobsViewHolder holder, int position) {

        Job job = jobs.get(position);

        holder.job_title.setText(job.getTitle());
        holder.company_name.setText(job.getCompany().getName());
        holder.locationName.setText(job.getLocation().getName());
/*

        if(job.getUrgency() != null && job.getUrgency()) holder.urgency.setBackgroundColor(getColor(R.color.mb_red));
        else holder.urgency.setBackgroundColor(getColor(R.color.transparent));
*/

        if(job.getTime().getDeadline() != null)
        holder.deadline.setText(DateUtil.reformatDateYYYYMMDD(job.getTime().getDeadline()));

        holder.date_posted.setText(DateUtil.convertStringToPrettyTime(job.getCreatedAt()));

        if(job.getBody() != null && !job.getBody().isEmpty())
        holder.body.setText(Html.fromHtml(job.getBody()));



        holder.tag.setText(job.getCategory().getName());

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(10);
        drawable.setColor(BaseFragment.getJobsColor(job.getCategory().getName()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.tag.setBackground(drawable);
        }else {
            holder.tag.setBackgroundDrawable(drawable);
        }

        if(job.getCompany().getEmail() == null|| job.getCompany().getEmail().isEmpty() || job.getCompany().getEmail().equalsIgnoreCase(""))
            holder.email.setEnabled(false);
    }


    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }



////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int getItemCount() {
        return jobs.size();

    }


    /*@Override
    public long getItemId(int position) {


        return position;


    }*/


    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
     }

    public List<Job> getJobs() {
        return jobs;
    }




    class JobsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        RelativeLayout parent_layout;
        TextView job_title;
        TextView company_name;
        TextView locationName;
        View urgency;
        TextView deadline;
        TextView date_posted;
        TextView body;
        TextView tag;
        ImageView starred;
        ImageView info;
        ImageView email;


        JobsViewHolder(View itemView) {
            super(itemView);


            parent_layout = (RelativeLayout) itemView.findViewById(R.id.parent_layout);
            job_title = (TextView) itemView.findViewById(R.id.job_title);
            body = (TextView) itemView.findViewById(R.id.body);

            company_name = (TextView) itemView.findViewById(R.id.company_name);
            locationName = (TextView) itemView.findViewById(R.id.location);
            urgency = (View) itemView.findViewById(R.id.urgency_indicator);
            deadline = (TextView) itemView.findViewById(R.id.deadline);
            date_posted = (TextView) itemView.findViewById(R.id.date_posted);
             tag = (TextView) itemView.findViewById(R.id.tag);

            starred = (ImageView) itemView.findViewById(R.id.starred);
            info = (ImageView) itemView.findViewById(R.id.info);
            email = (ImageView) itemView.findViewById(R.id.email);




            parent_layout.setOnClickListener(this);
            email.setOnClickListener(this);
            starred.setOnClickListener(this);
         }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {


                case R.id.parent_layout:

                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(itemView, getAdapterPosition());


                    }


                    break;


                case R.id.starred:


                    if (!isStarred) {


                        animateStarButton(starred);
                        isStarred = !isStarred;


                        CustomToast.makeToast(context, "Item added to Favorites!", Toast.LENGTH_SHORT).show();


                    } else {

                        starred.setImageResource(R.drawable.ic_star_border_grey_500_18dp);
                        CustomToast.makeToast(context, "Item removed from Favorites!", Toast.LENGTH_SHORT).show();

                        isStarred = !isStarred;

                    }



                    break;

                case R.id.email:

                    showAlertDialog("Send email?", "This will open your default mail app", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                            startEmailIntent(jobs.get(getAdapterPosition()).getCompany().getEmail(), jobs.get(getAdapterPosition()).getTitle());

                        }
                    }, "YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    }, "NO", R.drawable.ic_email_custom_24dp);


                    break;



            }

        }


    }





    private int getColor(int color) {
        return ContextCompat.getColor(context, color);
    }

    private void animateStarButton(final ImageView  holder) {
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
                holder.setImageResource(R.drawable.ic_star_custom_18dp);
            }


            @Override
            public void onAnimationEnd(Animator animation) {
                //heartAnimationsMap.remove(holder);
                // dispatchChangeFinishedIfAllAnimationsEnded(holder);
            }
        });

        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
        animatorSet.start();

        //heartAnimationsMap.put(holder, animatorSet);
    }


    void showAlertDialog(@Nullable String title, @Nullable String message,
                         @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                         @NonNull String positiveText,
                         @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                         @NonNull String negativeText, @Nullable int icon_drawable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppDialog);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setIcon(icon_drawable);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        builder.show();
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
}



package com.app.ahaspora.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ahaspora.R;
import com.app.ahaspora.fragments.BaseFragment;
import com.app.ahaspora.models.Event;
import com.app.ahaspora.models.Job;
import com.app.ahaspora.models.Tag;
import com.app.ahaspora.utilities.DateUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by aangjnr on 07/06/2017.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.JobsViewHolder> {

    static OnItemClickListener mItemClickListener;
    String TAG = EventsAdapter.class.getSimpleName();
    List<Event> events;
    Context context;
    boolean isStarred = false;
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    Map<RecyclerView.ViewHolder, AnimatorSet> heartAnimationsMap = new HashMap<>();
    private int[] mColors;





    public EventsAdapter(Context c, List<Event> _events) {

        this.context = c;
        this.events = _events;
        mColors = context.getResources().getIntArray(R.array.jobs_colors);

        hasStableIds();


    }

    @Override
    public EventsAdapter.JobsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.events_item_view, viewGroup, false);

        return new EventsAdapter.JobsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final EventsAdapter.JobsViewHolder holder, int position) {

        final Event event = events.get(position);


        if(event.getImage() != null && !event.getImage().isEmpty()) {

            Picasso.with(context).load(event.getImage()).into(holder.image, new Callback() {
                @Override
                public void onSuccess() {
                    //use your bitmap or something

                    Bitmap photo = ((BitmapDrawable) holder.image.getDrawable()).getBitmap();

                    Palette.Builder builder = new Palette.Builder(photo);
                    builder.generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(@NonNull Palette palette) {

                            //filter.setVisibility(View.GONE);
                            int mutedLight = palette.getDarkVibrantColor(context.getResources().getColor(R.color.colorAccentBlue));
                            event.setColor(mutedLight);

                           // holder.filter.setBackgroundColor(ColorUtils.setAlphaComponent(mutedLight, 130));


                            holder.title.setTextColor(mutedLight);


                            GradientDrawable drawable = new GradientDrawable();
                            drawable.setCornerRadius(1000);
                            drawable.setColor(mutedLight);

                            holder.attendeesCount.setBackground(drawable);


                            if(event.getGoing() != null)
                            if(event.getGoing()){

                                holder.going.setTextColor(mutedLight);
                                holder.going.setEnabled(false);

                            }else {
                                holder.notGoing.setTextColor(mutedLight);
                                holder.notGoing.setEnabled(false);

                            }

                        }
                    });
                }

                @Override
                public void onError() {

                }
            });
        }else{

            int randomColor = mColors[new Random().nextInt(mColors.length)];

            event.setColor(randomColor);
            holder.image.setBackgroundColor(randomColor);
           // holder.filter.setBackgroundColor(ColorUtils.setAlphaComponent(randomColor, 180));
           // holder.title.setTextColor(randomColor);

            holder.title.setTextColor(randomColor);


            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(1000);
            drawable.setColor(randomColor);

            holder.attendeesCount.setBackground(drawable);


            if(event.getGoing() != null)
                if(event.getGoing()){

                    holder.going.setTextColor(randomColor);
                    holder.going.setEnabled(false);

                }else {
                    holder.notGoing.setTextColor(randomColor);
                    holder.notGoing.setEnabled(false);

                }
        }


        if(event.getTime() != null)
            if(event.getTime().getStart().length() >= 19)
                holder.date.setText(DateUtil.reformatDate(event.getTime().getStart()));

        holder.title.setText(event.getTitle());

        if(event.getSubtitle() != null)
        holder.desc.setText(Html.fromHtml(event.getSubtitle()));




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
        return events.size();

    }


    /*@Override
    public long getItemId(int position) {


        return position;


    }*/


    public void setEvents(List<Event> events) {
        this.events = events;
     }

    public List<Event> getEvents() {
        return events;
    }




    class JobsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        RelativeLayout parent_layout;
        TextView title;
        TextView date;
        TextView desc;

        TextView going;
        TextView notGoing;
        TextView attendeesCount;


        ImageView starred;
        ImageView info;
        ImageView image;

       // View filter;


        JobsViewHolder(View itemView) {
            super(itemView);


            parent_layout = (RelativeLayout) itemView.findViewById(R.id.parent_layout);
            title = itemView.findViewById(R.id.event_title);
            date = itemView.findViewById(R.id.event_date);
            desc = itemView.findViewById(R.id.event_desc);
            attendeesCount = itemView.findViewById(R.id.events_attendees_text_view);

            going = itemView.findViewById(R.id.going);
            notGoing = itemView.findViewById(R.id.not_going);

           // filter = itemView.findViewById(R.id.filter);

            starred = itemView.findViewById(R.id.starred);
            info = itemView.findViewById(R.id.info);

            image = itemView.findViewById(R.id.events_image_view);



            parent_layout.setOnClickListener(this);
            starred.setOnClickListener(this);

            going.setOnClickListener(this);
            notGoing.setOnClickListener(this);


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


                        Toast.makeText(context, "Item added to Favorites!", Toast.LENGTH_SHORT).show();


                    } else {

                        starred.setImageResource(R.drawable.ic_star_border_white_18dp);
                        Toast.makeText(context, "Item removed from Favorites!", Toast.LENGTH_SHORT).show();

                        isStarred = !isStarred;

                    }
                    break;

                case R.id.going:

                    Event event = events.get(getAdapterPosition());
                    event.setGoing(true);

                    going.setTextColor(event.getColor());
                    notGoing.setTextColor(ContextCompat.getColor(context, R.color.gray));
                    going.setEnabled(false);
                    notGoing.setEnabled(true);

                    break;


                case R.id.not_going:

                    Event event2 = events.get(getAdapterPosition());
                    event2.setGoing(false);

                    notGoing.setTextColor(event2.getColor());
                    going.setTextColor(ContextCompat.getColor(context, R.color.gray));

                    going.setEnabled(true);
                    notGoing.setEnabled(false);

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
}



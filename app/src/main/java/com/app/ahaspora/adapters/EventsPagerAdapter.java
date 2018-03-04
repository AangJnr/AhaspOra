package com.app.ahaspora.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.ahaspora.R;
import com.app.ahaspora.models.Event;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by aangjnr on 14/10/2017.
 */

public class EventsPagerAdapter extends PagerAdapter {


    static EventsPagerAdapter.OnItemClickListener mItemClickListener;



    private Context mContext;
     List<Event> eventList;




    public EventsPagerAdapter(final Context context, List<Event> events) {
        mContext = context;
        this.eventList = events;
     }


    public void setOnItemClickListener(final EventsPagerAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view;

        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);

        view = mLayoutInflater.inflate(R.layout.event_view_pager_itemview, container, false);
        setupItem(view, position);
        container.addView(view);
        return view;
    }







    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }


    @Override
    public void destroyItem(@NonNull final ViewGroup container, final int position, @NonNull final Object object) {
        container.removeView((View) object);
    }



    private void setupItem(View v, final int position){

        Event e = eventList.get(position);

        Button statusButton = v.findViewById(R.id.status_button);
        final TextView body = v.findViewById(R.id.body_text);
        //final View filter = v.findViewById(R.id.filter_view);
        final ImageView image = v.findViewById(R.id.image);


        body.setText(e.getBody());

        if(e.getImage() != null && !e.getImage().isEmpty())
        {

            Picasso.with(mContext).load(e.getImage()).into(image, new Callback() {
                @Override
                public void onSuccess() {
                    //use your bitmap or something

                    Bitmap photo = ((BitmapDrawable) image.getDrawable()).getBitmap();

                    Palette.Builder builder = new Palette.Builder(photo);
                    builder.generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(@NonNull Palette palette) {

                            //filter.setVisibility(View.GONE);
                            int mutedLight = palette.getDarkVibrantColor(mContext.getResources().getColor(R.color.gray));
                            body.setBackgroundColor(ColorUtils.setAlphaComponent(mutedLight, 180));


                        }
                    });
                }

                @Override
                public void onError() {

                }
            });
        }


    }



    public List<Event> getData(){return eventList;}
    public void setData(List<Event> events){this.eventList = events;}

}

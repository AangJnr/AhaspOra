package com.app.ahaspora.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.ahaspora.R;
import com.app.ahaspora.models.PaymentOption;
import com.app.ahaspora.utilities.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by AangJnr on 8/10/16.
 */
public class SetupPaymentOptionsAdapter extends RecyclerView.Adapter<SetupPaymentOptionsAdapter.ViewHolder> {

    private static final int ANIMATED_ITEMS_COUNT = 2;
    static OnItemClickListener mItemClickListener;
    final ContextThemeWrapper ctx;
    List<PaymentOption> options;
    private Context context;
    private int lastAnimatedPosition = -1;


    /**
     * Constructor
     *
     * @param options
     **/

    public SetupPaymentOptionsAdapter(Context context, List<PaymentOption> options) {
        this.options = options;
        this.context = context;

        ctx = new ContextThemeWrapper(context, R.style.AppTheme);
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return options.size();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.payment_option_cardview, viewGroup, false);






        return new ViewHolder(v);
    }


    private void runEnterAnimation(View view, int position) {
        if (position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(Utils.getScreenHeight(context));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        PaymentOption po = options.get(position);

        viewHolder.name.setText(po.getType());
        Picasso.with(context).load(po.getIcon()).into(viewHolder.photo);


    }


    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;

        TextView name;
        ImageView photo;
        RelativeLayout mainLayout;


        ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.type);
            photo = (ImageView) itemView.findViewById(R.id.photo);
            mainLayout = (RelativeLayout) itemView.findViewById(R.id.card_view);


            mainLayout.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getAdapterPosition());


            }

        }

    }



    class VisaViewHolder extends RecyclerView.ViewHolder {



        VisaViewHolder(View itemView) {
            super(itemView);



         }


    }

}



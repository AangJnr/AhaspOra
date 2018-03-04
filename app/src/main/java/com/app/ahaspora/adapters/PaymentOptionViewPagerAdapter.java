package com.app.ahaspora.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.ahaspora.R;
import com.app.ahaspora.models.PaymentOption;
import com.app.ahaspora.utilities.Constants;

import java.util.List;

import movile.com.creditcardguide.model.IssuerCode;
import movile.com.creditcardguide.view.CreditCardView;


/**
 * Created by aangjnr on 04/09/2017.
 */

public class PaymentOptionViewPagerAdapter extends PagerAdapter {

    Context context;
    List<PaymentOption> paymentOptions;
    LayoutInflater layoutInflater;
    static PaymentOptionViewPagerAdapter.OnItemClickListener mItemClickListener;
    boolean isFlippedBack = false;




    public void setOnItemClickListener(final PaymentOptionViewPagerAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public PaymentOptionViewPagerAdapter(Context c, List<PaymentOption> orders){
        this.context = c;
        this.paymentOptions = orders;
        this.layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
    }


    @Override
    public int getCount() {
        return paymentOptions.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((View) object);

    }



    @Override
    public Object instantiateItem(ViewGroup container, final int position){

        PaymentOption paymentOption = paymentOptions.get(position);

        final View view;

        if(paymentOption.getType().equals(Constants.VISA)){



            view = this.layoutInflater.inflate(R.layout.visa_card_item, container, false);

            final CreditCardView creditCardView = (CreditCardView) view.findViewById(R.id.creditcard_view);



            creditCardView.chooseFlag(IssuerCode.VISACREDITO);
            creditCardView.setTextExpDate(paymentOption.getMonth() + "/" + paymentOption.getMonth());
            creditCardView.setTextNumber(paymentOption.getValue().trim());
            creditCardView.setTextOwner(paymentOption.getUserName());
            creditCardView.setTextCVV(paymentOption.getPin().trim());

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (!isFlippedBack) {
                        creditCardView.flipToBack();
                        isFlippedBack = true;

                    }else{
                        creditCardView.flipToFront();
                        isFlippedBack = false;
                    }

                    return true;
                }
            });


          /*  ccv.setCVV(paymentOption.getPin().trim());
            ccv.setCardHolderName(paymentOption.getUserName());
            ccv.setCardExpiry(paymentOption.getMonth() + "/" + paymentOption.getMonth());
            ccv.setCardNumber(paymentOption.getValue().trim());*/

        }else {

            view = this.layoutInflater.inflate(R.layout.select_payment_item, container, false);

            CardView cardView = (CardView) view.findViewById(R.id.card_view);
            LinearLayout expiryLayout = (LinearLayout) view.findViewById(R.id.expiry_layout);
            TextView expiry = (TextView) view.findViewById(R.id.expiry);
            TextView value = (TextView) view.findViewById(R.id.value);
            ImageView image = (ImageView) view.findViewById(R.id.image);


            value.setText(paymentOption.getValue());
            image.setImageResource(getImageResource(paymentOption.getType()));


            if (paymentOption.getType().equalsIgnoreCase(Constants.VISA)) {
                expiryLayout.setVisibility(View.VISIBLE);
                expiry.setText(paymentOption.getMonth() + "/" + paymentOption.getYear());

            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(view, position);


                }


            }
        });

        container.addView(view);
        return view;
    }





    void showAlertDialog(@Nullable String title, @Nullable String message,
                         @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                         @NonNull String positiveText,
                         @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                         @NonNull String negativeText, @Nullable int icon_drawable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppDialog);
        builder.setTitle(title);
        builder.setCancelable(false);
        if(icon_drawable != 0)
        builder.setIcon(icon_drawable);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        builder.show();
    }



    int getImageResource(String type){

        switch (type){

            case Constants.VISA:
                return R.drawable.visa_card;

            case Constants.MTN_MOBILE_MONEY:
                return R.drawable.mtn;

            case Constants.AIRTEL_MONEY:
                return R.drawable.airtel_mm;
            case Constants.VODAFONE_CASH:
                return R.drawable.vodafonecash;

            default:
                return 0;
        }



    }


}

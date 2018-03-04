package com.app.ahaspora.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.app.ahaspora.R;
import com.app.ahaspora.adapters.PaymentOptionViewPagerAdapter;
import com.app.ahaspora.adapters.SetupPaymentOptionsAdapter;
import com.app.ahaspora.models.PaymentOption;
import com.app.ahaspora.utilities.Constants;
import com.app.ahaspora.utilities.CustomProgressDialog;
import com.app.ahaspora.utilities.CustomToast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aangjnr on 30/06/2017.
 */

public class SetupPaymentsActivity extends BaseActivity {

    SetupPaymentOptionsAdapter mAdapter;
    List<PaymentOption> options = new ArrayList<>();
    RecyclerView mRecycler;
    String TAG = SetupPaymentsActivity.class.getSimpleName();

    List<PaymentOption> paymentOptions;

    Button payButton;

    LinearLayout selectPaymentLayout;
    LinearLayout addPaymentLAyout;
    protected LinearLayoutManager productsGridLayoutManager;

    ViewPager mViewPager;


    FrameLayout bottomSheetFrame;
    BottomSheetBehavior bottomSheetBehavior;



    PaymentOption PAYMENT_OPTION;



    SetupPaymentOptionsAdapter.OnItemClickListener onItemClickListener = new SetupPaymentOptionsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            switch (options.get(position).getType()) {

                case "VISA":
                    showVisaView();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.right_slide, R.anim.slide_out_left);
        setContentView(R.layout.activity_payments_new);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_white_24dp);
            getSupportActionBar().setTitle("Payments");

        }

        payButton = (Button) findViewById(R.id.pay_button);
        selectPaymentLayout = (LinearLayout) findViewById(R.id.selectPaymentLayout);

        addPaymentLAyout = (LinearLayout) findViewById(R.id.addPaymentLayout);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);


      /*  if (getIntent() != null && getIntent().getBooleanExtra("hideNextButton", false)) {

            findViewById(R.id.next_button_layout).setVisibility(View.GONE);

        }*/

        paymentOptions = new ArrayList<>();
        paymentOptions.add(new PaymentOption("1", Constants.VISA, "A User's Name", "************", "5623"));


        if (paymentOptions != null && paymentOptions.size() > 0) {
            selectPaymentLayout.setVisibility(View.VISIBLE);


            PaymentOptionViewPagerAdapter mViewPagerAdapter = new PaymentOptionViewPagerAdapter(this, paymentOptions);
            mViewPager.setPadding(32, 0, 32, 32);
            mViewPager.setClipToPadding(false);
            mViewPager.setOffscreenPageLimit(4);
            mViewPager.setPageMargin(16);

            mViewPager.setAdapter(mViewPagerAdapter);
            mViewPagerAdapter.notifyDataSetChanged();

            mViewPagerAdapter.setOnItemClickListener(new PaymentOptionViewPagerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    PAYMENT_OPTION = paymentOptions.get(position);
                    authorizePayment();

                }
            });

        }



        options.add(new PaymentOption(Constants.VISA, R.drawable.visa_card));
        //options.add(new PaymentOption(Constants.SLYDEPAY, R.drawable.slydepay));
        options.add(new PaymentOption(Constants.VODAFONE_CASH, R.drawable.vodafonecash));
        options.add(new PaymentOption(Constants.AIRTEL_MONEY, R.drawable.airtel_mm));
        options.add(new PaymentOption(Constants.MTN_MOBILE_MONEY, R.drawable.mtn));


        mRecycler = (RecyclerView) findViewById(R.id.po_recyclerView);


        productsGridLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(productsGridLayoutManager);

        // SpacesGridItemDecoration decoration = new SpacesGridItemDecoration(8);
        //mRecycler.addItemDecoration(decoration);


        mAdapter = new SetupPaymentOptionsAdapter(this, options);
        mRecycler.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(onItemClickListener);

        startFadeInAnimation(mRecycler);







        bottomSheetFrame = (FrameLayout) findViewById(R.id.frame_layout);
        bottomSheetBehavior = (BottomSheetBehavior)  BottomSheetBehavior.from(bottomSheetFrame);


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_DRAGGING)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });





    }



    void authorizePayment() {
        final LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);

        View enterPinView = layoutInflaterAndroid.inflate(R.layout.enter_pin_dialog2, null);

        final PinEntryEditText pinEditText = (PinEntryEditText) enterPinView.findViewById(R.id.code_input);

        final AlertDialog.Builder alertDialogPinInput = new AlertDialog.Builder(this, R.style.DialogTheme);
        alertDialogPinInput.setView(enterPinView);
        alertDialogPinInput
                .setCancelable(true);
        alertDialogPinInput.setTitle("Enter a new pin for this card.\nConsider finger print auth for supported ones");


        alertDialogPinInput.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        final AlertDialog enterPinDialog = alertDialogPinInput.create();

        enterPinDialog.show();


        pinEditText.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {


                String comparingKey = str.toString();


                     enterPinDialog.dismiss();


            }
        });
    }



    void completeOrder(){


        final CustomProgressDialog progressDialog = new CustomProgressDialog(this);
        progressDialog.showCustomLoadingDialog("Verifying card", "Please wait...", false);



        //Todo, upload order details to server
        Long millis = System.currentTimeMillis();


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        progressDialog.dismiss();

                    }
                }, 5000);


        CustomToast.makeToast(SetupPaymentsActivity.this, "Done", Toast.LENGTH_SHORT).show();

    }


    void showVisaView(){

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        final TextInputLayout cardNoLayout = (TextInputLayout) findViewById(R.id.cardNoLayout);

        final TextInputLayout cardNameLayout = (TextInputLayout) findViewById(R.id.nameLayout);
        final TextInputLayout ccvLayout = (TextInputLayout) findViewById(R.id.cvvLayout);

        final TextInputLayout monthLayout = (TextInputLayout) findViewById(R.id.monthLayout);
        final TextInputLayout yearLayout = (TextInputLayout) findViewById(R.id.yearLayout);


        payButton.setEnabled(false);


        TextWatcher textWatcher_cardNo = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() == 0 || s.length() < 16){
                    payButton.setEnabled(false);
                    cardNoLayout.setError("Please enter a valid card number");

                }else {
                    payButton.setEnabled(true);
                    cardNoLayout.setError(null);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length() == 0 || s.length() < 16){
                    payButton.setEnabled(false);
                    cardNoLayout.setError("Please enter a valid card number");

                }else {
                    payButton.setEnabled(true);
                    cardNoLayout.setError(null);

                }
            }
        };

        TextWatcher textWatcher_cvvNo = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(s.length() == 0 || s.length() < 3){
                    payButton.setEnabled(false);
                    ccvLayout.setError("Please enter a valid cvv no.");

                }else {
                    payButton.setEnabled(true);
                    ccvLayout.setError(null);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length() == 0 || s.length() < 3){
                    payButton.setEnabled(false);
                    ccvLayout.setError("Please enter a valid cvv no.");

                }else {
                    payButton.setEnabled(true);
                    ccvLayout.setError(null);

                }


            }
        };

        TextWatcher textWatcher_name = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0 ){
                    payButton.setEnabled(false);
                    cardNameLayout.setError("Name cannot be empty");

                }else {
                    payButton.setEnabled(true);
                    cardNameLayout.setError(null);

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length() == 0 ){
                    payButton.setEnabled(false);
                    cardNameLayout.setError("Name cannot be empty");

                }else {
                    payButton.setEnabled(true);
                    cardNameLayout.setError(null);

                }


            }
        };

        cardNoLayout.getEditText().addTextChangedListener(textWatcher_cardNo);
        cardNameLayout.getEditText().addTextChangedListener(textWatcher_name);
        ccvLayout.getEditText().addTextChangedListener(textWatcher_cvvNo);



        LinearLayout saveCardLayout = (LinearLayout) findViewById(R.id.saveCard);
        final CheckBox saveCardCheckBox = (CheckBox) findViewById(R.id.saveCardCheckBox);

        saveCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(saveCardCheckBox.isChecked())
                    saveCardCheckBox.setChecked(false);
                else saveCardCheckBox.setChecked(true);

            }
        });




        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PaymentOption po = new PaymentOption(cardNoLayout.getEditText().getText().toString().substring(11), Constants.VISA,
                        cardNameLayout.getEditText().getText().toString().trim(),
                        cardNoLayout.getEditText().getText().toString().trim(),
                        ccvLayout.getEditText().getText().toString().trim(),
                        monthLayout.getEditText().getText().toString().trim(),
                        yearLayout.getEditText().getText().toString().trim());

                PAYMENT_OPTION = po;

                //

                completeOrder();


            }
        });




    }


    @Override
    public void onBackPressed() {

        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_DRAGGING){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        }else {

            super.onBackPressed();



        }


    }


    public void startFadeInAnimation(View v) {

        v.animate()
                .alpha(1)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(500)
                .setDuration(500)
                .start();
    }
}

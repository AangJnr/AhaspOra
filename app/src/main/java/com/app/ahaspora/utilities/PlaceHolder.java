package com.app.ahaspora.utilities;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.ahaspora.R;


/**
 * Created by aangjnr on 13/10/2017.
 */

public class PlaceHolder {

     View view = null;
    AppCompatActivity context;
    static String TAG = "Place Holder";



    public PlaceHolder(AppCompatActivity activity){

        this.context = activity;


    }


    public  void showLoadingView(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        if (view !=  null)((ViewGroup) view.getParent()).removeView(view);

        //view = inflater.inflate(R.layout.loading_view, null);


    context.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));


     //   AnimatedLoadingIndicator animatedLoadingIndicator = (AnimatedLoadingIndicator) view.findViewById(R.id.animated_loading_indicator);
      //  animatedLoadingIndicator.smoothToShow();

    }


    public void hideLoadingView(){
new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {


        if(view != null)
            ((ViewGroup) view.getParent()).removeView(view);
    }
}, 1000);

    }




}

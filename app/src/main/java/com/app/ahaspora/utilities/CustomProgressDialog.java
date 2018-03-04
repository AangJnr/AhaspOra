package com.app.ahaspora.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.app.ahaspora.R;


public class CustomProgressDialog {

    AlertDialog b;

    Context context;

    public CustomProgressDialog(Context c){
        this.context = c;

    }



    public void showCustomLoadingDialog(String title, String message, boolean cancelable) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog_one, null);


        AnimatedLoadingIndicator animatedLoadingIndicator = (AnimatedLoadingIndicator) dialogView.findViewById(R.id.animated_loading_indicator);
        animatedLoadingIndicator.smoothToShow();
        dialogBuilder.setView(dialogView);

        dialogBuilder.setCancelable(cancelable);
        dialogBuilder.setMessage(message);
        dialogBuilder.setTitle(title);
        b = dialogBuilder.create();

        b.show();



    }



    public void dismiss(){

        if(b != null)
            b.dismiss();

    }




}

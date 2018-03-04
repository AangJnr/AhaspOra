package com.app.ahaspora;

import android.app.Application;
import android.os.StrictMode;

/**
 * Created by aangjnr on 02/06/2017.
 */

public class AhasporaApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());



    }




}

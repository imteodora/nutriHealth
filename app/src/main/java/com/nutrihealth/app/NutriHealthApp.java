package com.nutrihealth.app;

import android.app.Application;
import android.content.Context;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Teo on 3/23/2018.
 */

public class NutriHealthApp extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Raleway-Medium.ttf")
                .build());

        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}

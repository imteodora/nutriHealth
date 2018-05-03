package com.nutrihealth.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Teo on 3/23/2018.
 */

public class PrefsManager {

    private static final String PREFS_NAME = "nutri_health_prefs";
    private static final String KEY_IS_FIRST_LAUNCH = "is_first_launch";
    private static final String KEY_IS_USER_LOGGED_IN = "is_user_logged_in";

    private static final String KEY_PICTURE = "key_picture";
    private static final String KEY_KCAL_PER_DAY = "key_kcal_per_day";





    private static PrefsManager instance = null;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    private PrefsManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public static synchronized PrefsManager getInstance(Context context) {
        if (instance == null) {
            instance = new PrefsManager(context);
        }
        return instance;
    }

    public boolean isFirstLaunch() {
        boolean isFirstLaunch = sharedPreferences.getBoolean(KEY_IS_FIRST_LAUNCH, true);
        if (isFirstLaunch) {
            editor.putBoolean(KEY_IS_FIRST_LAUNCH, false).commit();
        }
        return isFirstLaunch;
    }

    public void putKeyPicture(String picture) {
        editor.putString(KEY_PICTURE, picture);
        editor.commit();
    }

    public String getKeyPicture() {
        return sharedPreferences.getString(KEY_PICTURE, "");
    }

    public void putKeyIsUserLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_USER_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public boolean getKeyIsUserLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_USER_LOGGED_IN, false);
    }



    public int getKeyKcalPerDay() {
        return sharedPreferences.getInt(KEY_KCAL_PER_DAY, 0);
    }

    public void setKeyKcalPerDay(int kcalPerDay){
        editor.putInt(KEY_KCAL_PER_DAY, kcalPerDay);
        editor.commit();
    }


}

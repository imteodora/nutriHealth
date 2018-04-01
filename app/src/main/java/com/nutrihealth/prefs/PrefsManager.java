package com.nutrihealth.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Teo on 3/23/2018.
 */

public class PrefsManager {

    private static final String PREFS_NAME = "nutri_health_prefs";
    private static final String KEY_IS_FIRST_LAUNCH = "is_first_launch";
    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_PICTURE = "key_picture";
    private static final String KEY_ACTUAL_WEIGHT = "key_actual_age";
    private static final String KEY_GENDER = "key_gender";
    private static final String KEY_HEIGHT = "key_height";
    private static final String KEY_AGE = "key_age";
    private static final String KEY_ACTIVITY_LVL = "key_activity_lvl";
    private static final String KEY_IDEAL_WEIGHT = "key_ideal_weight";
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

    public void putKeyName(String name) {
        editor.putString(KEY_USERNAME, name);
        editor.commit();
    }

    public String getKeyName() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public void putKeyActualWeight(int weight) {
        editor.putInt(KEY_ACTUAL_WEIGHT, weight);
        editor.commit();
    }

    public int getKeyActualWeight() {
        return sharedPreferences.getInt(KEY_ACTUAL_WEIGHT, 0);
    }

    public void putKeyGender(String gender) {
        editor.putString(KEY_GENDER, gender);
        editor.commit();
    }

    public String getKeyGender() {
        return sharedPreferences.getString(KEY_GENDER, "");
    }

    public void putKeyHeight(int height) {
        editor.putInt(KEY_HEIGHT, height);
        editor.commit();
    }

    public int getKeyHeight() {
        return sharedPreferences.getInt(KEY_HEIGHT, 0);
    }

    public void putKeyAge(int age) {
        editor.putInt(KEY_AGE, age);
        editor.commit();
    }

    public int getKeyAge() {
        return sharedPreferences.getInt(KEY_AGE, 0);
    }


    public void putKeyActivityLvl(int lvl) {
        editor.putInt(KEY_ACTIVITY_LVL, lvl);
        editor.commit();
    }

    public int getKeyActivityLvl() {
        return sharedPreferences.getInt(KEY_ACTIVITY_LVL, 0);
    }

    public void putKeyIdealWeight(int idealWeight) {
        editor.putInt(KEY_IDEAL_WEIGHT, idealWeight);
        editor.commit();
    }

    public int getKeyIdealWeight() {
        return sharedPreferences.getInt(KEY_IDEAL_WEIGHT, 0);
    }

    public void putKeyKcalPerDay(int kcal) {
        editor.putInt(KEY_KCAL_PER_DAY, kcal);
        editor.commit();
    }

    public int getKeyKcalPerDay() {
        return sharedPreferences.getInt(KEY_KCAL_PER_DAY, 0);
    }


}

package com.nutrihealth.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Teo on 3/23/2018.
 */

public class PrefsManager {

    private static final String PREFS_NAME = "nutri_health_prefs";

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
}

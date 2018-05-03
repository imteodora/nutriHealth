package com.nutrihealth.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.nutrihealth.R;
import com.nutrihealth.activities.AlarmsActivity;
import com.nutrihealth.activities.CaloriesHistoryActivity;
import com.nutrihealth.activities.HomeActivity;
import com.nutrihealth.activities.LoginActivity;
import com.nutrihealth.activities.ProfileActivity;
import com.nutrihealth.activities.RegisterActivity;
import com.nutrihealth.constants.Constants;

/**
 * Created by Teo on 3/23/2018.
 */

public class IntentStarter {

    public static void gotoHomeActivity(Context context, final boolean shouldClearAllPreviousScreens) {
        Intent intent = new Intent(context, HomeActivity.class);
        if (shouldClearAllPreviousScreens) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void gotoProfileActivity(Context context, final boolean shouldClearAllPreviousScreens) {
        Intent intent = new Intent(context, ProfileActivity.class);
        if (shouldClearAllPreviousScreens) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void gotoProfileActivityToEditInfo(Context context, final boolean shouldClearAllPreviousScreens) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(Constants.EDIT_INFO_CODE,"");
        if (shouldClearAllPreviousScreens) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.activity_slide_in_down, R.anim.activity_slide_out_down);
    }

    public static void gotoAlarmsActivity(Context context, final boolean shouldClearAllPreviousScreens) {
        Intent intent = new Intent(context, AlarmsActivity.class);
        if (shouldClearAllPreviousScreens) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void gotoCaloriesHistoryActivity(Context context, final boolean shouldClearAllPreviousScreens) {
        Intent intent = new Intent(context, CaloriesHistoryActivity.class);
        if (shouldClearAllPreviousScreens) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void gotoRegisterActivity(Context context, final boolean shouldClearAllPreviousScreens) {
        Intent intent = new Intent(context, RegisterActivity.class);
        if (shouldClearAllPreviousScreens) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void gotoLoginActivity(Context context, final boolean shouldClearAllPreviousScreens) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (shouldClearAllPreviousScreens) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
}

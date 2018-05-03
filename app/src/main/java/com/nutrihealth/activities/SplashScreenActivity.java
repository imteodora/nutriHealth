package com.nutrihealth.activities;

import android.os.Handler;
import android.os.Bundle;

import com.nutrihealth.R;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.prefs.PrefsManager;
import com.nutrihealth.utils.IntentStarter;

public class SplashScreenActivity extends BaseActivity {

    private static final long SPLASH_SCREEN_DURATION = 2 * 1000;
    private Handler switchScreenHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        setStatusBarColor(R.color.black);

        switchScreenHandler = new Handler();
        switchScreenHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToNextScreen();
            }
        }, SPLASH_SCREEN_DURATION);
    }

    private void goToNextScreen() {
        if (PrefsManager.getInstance(SplashScreenActivity.this).getKeyIsUserLoggedIn()) {
            IntentStarter.gotoHomeActivity(this, true);
        } else {
            IntentStarter.gotoLoginActivity(this, true);
        }

    }
}

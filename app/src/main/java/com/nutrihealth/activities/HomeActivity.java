package com.nutrihealth.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;

import com.nutrihealth.R;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.databinding.ActivityHomeBinding;

/**
 * Created by Teo on 3/23/2018.
 */

public class HomeActivity extends BaseActivity {

    private ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(HomeActivity.this,R.layout.activity_splash_screen);

        binding.setHomeActivity(this);
        binding.setShowProgressBar(false);

    }
}

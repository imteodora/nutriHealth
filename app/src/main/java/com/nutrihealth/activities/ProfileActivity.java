package com.nutrihealth.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.nutrihealth.R;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.databinding.ActivityHomeBinding;
import com.nutrihealth.databinding.ActivityProfileBinding;

/**
 * Created by Teo on 3/30/2018.
 */

public class ProfileActivity extends BaseActivity {

    private ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ProfileActivity.this, R.layout.activity_profile);

        binding.setProfileActivity(this);
        binding.setShowProgressBar(false);

        //setupUI(binding.profileParent);

       setUpViews();

    }

    private void setUpViews() {

        setStatusBarColor(R.color.black);
    }

    public void onEditProfileImagePressed(View view){

    }

    public void onSaveInfosPressed(View view){

    }
}

package com.nutrihealth.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nutrihealth.R;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.databinding.ActivityAlarmsBinding;
import com.nutrihealth.databinding.ActivityCaloriesHistoryBinding;
import com.nutrihealth.listeners.ISectionSelectListener;

/**
 * Created by Teodora on 12.04.2018.
 */

public class CaloriesHistoryActivity extends BaseActivity implements ISectionSelectListener {

    @Override
    public void onSectionSelected(Section section) {

    }

    public enum Section {
        TODAY,
        HISTORY
    }

    private ActivityCaloriesHistoryBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(CaloriesHistoryActivity.this, R.layout.activity_calories_history);
        binding.setCaloriesHistoryActivity(this);
        binding.setSectionSelectListener(this);
        binding.setSelectedSection(Section.TODAY);
        setUpViews();
    }

    private void setUpViews() {



    }

}

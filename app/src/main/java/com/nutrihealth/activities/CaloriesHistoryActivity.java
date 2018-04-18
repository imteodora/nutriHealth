package com.nutrihealth.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.nutrihealth.R;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.base.BaseFragment;
import com.nutrihealth.databinding.ActivityAlarmsBinding;
import com.nutrihealth.databinding.ActivityCaloriesHistoryBinding;
import com.nutrihealth.fragments.HistoryListFragment;
import com.nutrihealth.fragments.TodayListFragment;
import com.nutrihealth.listeners.ISectionSelectListener;
import com.nutrihealth.views.CustomToolbar;

/**
 * Created by Teodora on 12.04.2018.
 */

public class CaloriesHistoryActivity extends BaseActivity implements ISectionSelectListener {


    private ActivityCaloriesHistoryBinding binding;
    private Section selectedSection;

    @Override
    public void onSectionSelected(Section section) {
        if (selectedSection != null && selectedSection == section) {
            return;
        }

        switch (section) {
            case TODAY:
                switchToFragment(new TodayListFragment());
                break;
            case HISTORY:
                switchToFragment(new HistoryListFragment());
                break;
            default:
                break;
        }

        selectedSection = section;
        binding.setSelectedSection(section);
    }

    public enum Section {
        TODAY,
        HISTORY
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(CaloriesHistoryActivity.this, R.layout.activity_calories_history);
        binding.setCaloriesHistoryActivity(this);
        binding.setSectionSelectListener(this);
        setUpViews();
    }

    private void setUpViews() {

        setStatusBarColor(R.color.black);
        binding.caloriesHistoryToolbar.setOnBackButtonPressedListener(new CustomToolbar.OnBackButtonPressedListener() {
            @Override
            public void onBackButtonPressed() {
                finish();
            }
        });
        binding.caloriesHistoryToolbar.showLogo();
        onSectionSelected(Section.TODAY);

    }

    private void switchToFragment(final BaseFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragments_container, fragment, fragment.getFragmentTag())
                .commit();
    }

}

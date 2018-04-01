package com.nutrihealth.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.View;

import com.nutrihealth.R;
import com.nutrihealth.adapters.NavigationDrawerAdapter;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.databinding.ActivityHomeBinding;
import com.nutrihealth.listeners.DrawerItemListener;
import com.nutrihealth.model.HomeInfos;
import com.nutrihealth.repository.Resource;
import com.nutrihealth.utils.IntentStarter;
import com.nutrihealth.viewModels.HomeViewModel;
import com.nutrihealth.viewModels.ProfileViewModel;

/**
 * Created by Teo on 3/23/2018.
 */

public class HomeActivity extends BaseActivity implements DrawerItemListener {

    private ActivityHomeBinding binding;
    private HomeViewModel viewModel;

    private NavigationDrawerAdapter adapter;
    private ActionBarDrawerToggle drawerToggle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(HomeActivity.this,R.layout.activity_home);
        viewModel = ViewModelProviders.of(HomeActivity.this).get(HomeViewModel.class);

        binding.setHomeActivity(this);
        binding.setShowProgressBar(false);

        listenForLiveData();
        setUpViews();
        

    }

    private void listenForLiveData() {
        
        viewModel.getHomeUserInfosLiveData().observe(HomeActivity.this, new Observer<Resource<HomeInfos>>() {
            @Override
            public void onChanged(@Nullable Resource<HomeInfos> homeInfosResource) {
                if (homeInfosResource.getState() == Resource.State.LOADING) {
                    binding.setShowProgressBar(true);
                } else if (homeInfosResource.getState() == Resource.State.ERROR) {
                    binding.setShowProgressBar(false);
                    showCustomDialog(getResources().getString(R.string.error_title), homeInfosResource.getMessage(), DialogType.ERROR, null);
                } else if (homeInfosResource.getState() == Resource.State.SUCCESS) {
                    binding.setShowProgressBar(false);
                    populateViews(homeInfosResource.getData());

                }
            }
        });
    }

    private void populateViews(HomeInfos data) {
    }

    @Override
    protected void onResume() {
        viewModel.returnHomeUserInfos(HomeActivity.this);
        setupDrawer();
        setupDrawerList();
        super.onResume();
    }

    private void setUpViews() {

        setStatusBarColor(R.color.black);
    }

    public void onEditInfoPressed(View view){
        IntentStarter.gotoProfileActivityToEditInfo(HomeActivity.this, false);
    }

    public void onMenuButtonPressed(View view){
        binding.drawerLayout.openDrawer(Gravity.END);
    }

    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(HomeActivity.this, binding.drawerLayout, R.string.drawer_open, R.string.drawer_close);
        binding.drawerLayout.addDrawerListener(drawerToggle);
    }

    private void setupDrawerList() {

        adapter = new NavigationDrawerAdapter(HomeActivity.this, R.layout.item_navigation_drawer, HomeActivity.this);
        binding.drawerListview.setAdapter(adapter);


    }

    @Override
    public void onDrawerItemPressed(NavigationDrawerAdapter.DrawerItem drawerItem) {
        adapter.setSelectedItemIndex(drawerItem.ordinal());
        switch (drawerItem) {
            case ALARMS:
                break;

        }
        binding.drawerLayout.closeDrawer(Gravity.END);
    }
}

package com.nutrihealth.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nutrihealth.R;
import com.nutrihealth.adapters.NavigationDrawerAdapter;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.databinding.ActivityHomeBinding;
import com.nutrihealth.listeners.DrawerItemListener;
import com.nutrihealth.model.HomeInfos;
import com.nutrihealth.prefs.PrefsManager;
import com.nutrihealth.repository.Resource;
import com.nutrihealth.utils.BitmapUtils;
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
    private FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;

    private boolean doubleBackToExitPressedOnce = false;
    private Animation slideLeftEnter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(HomeActivity.this,R.layout.activity_home);
        viewModel = ViewModelProviders.of(HomeActivity.this).get(HomeViewModel.class);
        auth = FirebaseAuth.getInstance();
        slideLeftEnter = AnimationUtils.loadAnimation(this, R.anim.anim_left_enter);


        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {

                    PrefsManager.getInstance(HomeActivity.this).putKeyIsUserLoggedIn(false);
                    IntentStarter.gotoLoginActivity(HomeActivity.this, true);

                }
            }
        };

        auth.addAuthStateListener(authListener);

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

        binding.userProfileIv.setImageBitmap(BitmapUtils.decodeBase64(data.getImage()));
        binding.idealWeightTv.setText(Integer.toString(data.getIdealWeight()) + " kg");
        binding.weightToLoseTv.setText(Integer.toString(data.getActualWeight() - data.getIdealWeight()) + " kg");
        binding.calPerDayTv.setText(Integer.toString(data.getKcalPerDay()));


        binding.firstContainer.setVisibility(View.VISIBLE);
        binding.secondContainer.setVisibility(View.VISIBLE);
        binding.thirdContainer.setVisibility(View.VISIBLE);


        binding.firstContainer.startAnimation(slideLeftEnter);
        binding.secondContainer.startAnimation(slideLeftEnter);
        binding.thirdContainer.startAnimation(slideLeftEnter);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.back_to_exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 3000);
    }

    @Override
    protected void onResume() {
        binding.setShowProgressBar(true);
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
                IntentStarter.gotoAlarmsActivity(HomeActivity.this, false);
                break;
            case PROFILE:
                IntentStarter.gotoProfileActivityToEditInfo(HomeActivity.this, false);
                break;
            case HISTORY:
                IntentStarter.gotoCaloriesHistoryActivity(HomeActivity.this,false);
                break;
            case MEAL:
                IntentStarter.gotoMealPlanningActivity(HomeActivity.this,false);

        }
        binding.drawerLayout.closeDrawer(Gravity.END);
    }

    public void onLogoutPressed(View view){

        auth.signOut();
        PrefsManager.getInstance(HomeActivity.this).putKeyIsUserLoggedIn(false);
        IntentStarter.gotoLoginActivity(HomeActivity.this, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authListener != null){
            auth.removeAuthStateListener(authListener);
        }
    }
}

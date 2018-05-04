package com.nutrihealth.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nutrihealth.R;
import com.nutrihealth.adapters.DayDetailsAdapter;
import com.nutrihealth.adapters.TodayPlannerAdapter;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.constants.Constants;
import com.nutrihealth.databinding.ActivityDayDetailsBinding;
import com.nutrihealth.fragments.TodayListFragment;
import com.nutrihealth.model.Product;
import com.nutrihealth.prefs.PrefsManager;
import com.nutrihealth.repository.Resource;
import com.nutrihealth.viewModels.DayDetailsViewModel;
import com.nutrihealth.viewModels.TodayViewModel;
import com.nutrihealth.views.CustomToolbar;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Teo on 5/4/2018.
 */

public class DayDetailsActivity extends BaseActivity {

    private ActivityDayDetailsBinding binding;
    private DayDetailsViewModel viewModel;

    private DayDetailsAdapter adapter;
    private int perKcal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(DayDetailsActivity.this).get(DayDetailsViewModel.class);
        binding = DataBindingUtil.setContentView(DayDetailsActivity.this, R.layout.activity_day_details);
        binding.setDayDetailsActivity(this);
        binding.setShowProgressBar(false);

        perKcal = PrefsManager.getInstance(DayDetailsActivity.this).getKeyKcalPerDay();
        listenToLiveData();
        setUpViews();

        if(getIntent().hasExtra(Constants.DAY_DETAILS_EXTRA_CODE)){
            String date = getIntent().getStringExtra(Constants.DAY_DETAILS_EXTRA_CODE);
            viewModel.getAllProductsForToday(date);
        }

    }

    private void setUpViews() {
        setStatusBarColor(R.color.black);
        binding.dayDetailsToolbar.setOnBackButtonPressedListener(new CustomToolbar.OnBackButtonPressedListener() {
            @Override
            public void onBackButtonPressed() {
                finish();
            }
        });
        binding.dayDetailsToolbar.showLogo();
    }

    private void listenToLiveData() {
        viewModel.getTodayHistoryLiveData().observe(DayDetailsActivity.this, new Observer<Resource<List<Product>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Product>> listResource) {
                if (listResource.getState() == Resource.State.LOADING) {
                } else if (listResource.getState() == Resource.State.ERROR) {
                    showCustomDialog(getResources().getString(R.string.error_title), "", BaseActivity.DialogType.ERROR, null);
                } else if (listResource.getState() == Resource.State.SUCCESS) {

                    setUpRecyclerView(listResource.getData());


                }
            }
        });
    }

    private void setUpRecyclerView(List<Product> productList) {
        List<Product> breakfastList = new ArrayList<>();
        List<Product> firstSnackList = new ArrayList<>();
        List<Product> lunchList = new ArrayList<>();
        List<Product> secondSnackList = new ArrayList<>();
        List<Product> dinnerList = new ArrayList<>();

        int allKcal = 0;

        for (Product p : productList) {
            int cal = Integer.parseInt(p.getKcal());
            allKcal += cal;
            switch (p.getType()) {
                case 0:
                    breakfastList.add(p);
                    break;
                case 1:
                    firstSnackList.add(p);
                    break;
                case 2:
                    lunchList.add(p);
                    break;
                case 3:
                    secondSnackList.add(p);
                    break;
                case 4:
                    dinnerList.add(p);
                    break;
            }
        }

        adapter = new DayDetailsAdapter(this, breakfastList, firstSnackList, lunchList, secondSnackList, dinnerList, allKcal ,perKcal);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.productsRv.setLayoutManager(llm);
        binding.productsRv.setAdapter(adapter);
    }
}

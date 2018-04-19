package com.nutrihealth.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nutrihealth.R;
import com.nutrihealth.adapters.HistoryPlannerAdapter;
import com.nutrihealth.adapters.TodayPlannerAdapter;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.base.BaseFragment;
import com.nutrihealth.databinding.FragmentHistoryBinding;
import com.nutrihealth.model.HistoryDay;
import com.nutrihealth.model.Product;
import com.nutrihealth.prefs.PrefsManager;
import com.nutrihealth.repository.Resource;
import com.nutrihealth.viewModels.HistoryViewModel;
import com.nutrihealth.viewModels.TodayViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Teodora on 12.04.2018.
 */

public class HistoryListFragment extends BaseFragment {
    private FragmentHistoryBinding binding;
    private HistoryViewModel viewModel;
    private HistoryPlannerAdapter adapter;
    private List<HistoryDay> historyDayList;

    public static final String TAG = "HistoryFragment";

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHistoryFragment(this);
        viewModel = ViewModelProviders.of(HistoryListFragment.this).get(HistoryViewModel.class);


        listenForLiveData();
        viewModel.sendGetHistoryRequest();


    }

    private void listenForLiveData() {

        viewModel.getHistoryLiveData().observe(HistoryListFragment.this, new Observer<Resource<List<HistoryDay>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<HistoryDay>> listResource) {
                if (listResource.getState() == Resource.State.LOADING) {
                } else if (listResource.getState() == Resource.State.ERROR) {
                    showCustomDialog(getResources().getString(R.string.error_title), "", BaseActivity.DialogType.ERROR, null);
                } else if (listResource.getState() == Resource.State.SUCCESS) {
                    setUpRecyclerView(listResource.getData());
                }
            }
        });
    }

    private void setUpViews() {

    }

    private void setUpRecyclerView(List<HistoryDay> historyDayList) {

        int perKcal = PrefsManager.getInstance(getContext()).getKeyKcalPerDay();
        adapter = new HistoryPlannerAdapter(getActivity(), historyDayList, perKcal + "");
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.historyRv.setLayoutManager(llm);
        binding.historyRv.setAdapter(adapter);
    }


}

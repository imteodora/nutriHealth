package com.nutrihealth.fragments;

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
import com.nutrihealth.base.BaseFragment;
import com.nutrihealth.databinding.FragmentHistoryBinding;
import com.nutrihealth.model.HistoryDay;
import com.nutrihealth.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Teodora on 12.04.2018.
 */

public class HistoryListFragment extends BaseFragment {
    private FragmentHistoryBinding binding;
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
        setUpViews();


    }

    private void setUpViews() {

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {

        historyDayList = new ArrayList<>();
        historyDayList.add(new HistoryDay("date","1800"));
        historyDayList.add(new HistoryDay("date","2000"));
        historyDayList.add(new HistoryDay("date","1700"));
        historyDayList.add(new HistoryDay("date","1500"));
        historyDayList.add(new HistoryDay("date","2200"));

        adapter = new HistoryPlannerAdapter(getActivity(),historyDayList,"1900");
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.historyRv.setLayoutManager(llm);
        binding.historyRv.setAdapter(adapter);
    }


}

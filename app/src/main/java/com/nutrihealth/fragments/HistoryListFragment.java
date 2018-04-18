package com.nutrihealth.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nutrihealth.R;
import com.nutrihealth.base.BaseFragment;
import com.nutrihealth.databinding.FragmentHistoryBinding;

/**
 * Created by Teodora on 12.04.2018.
 */

public class HistoryListFragment extends BaseFragment {
    private FragmentHistoryBinding binding;

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
    }

}

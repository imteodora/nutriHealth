package com.nutrihealth.fragments;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nutrihealth.R;
import com.nutrihealth.activities.ProfileActivity;
import com.nutrihealth.adapters.TodayPlannerAdapter;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.base.BaseFragment;
import com.nutrihealth.databinding.FragmentTodayBinding;
import com.nutrihealth.model.BaseResponse;
import com.nutrihealth.model.Product;
import com.nutrihealth.prefs.PrefsManager;
import com.nutrihealth.repository.Resource;
import com.nutrihealth.utils.InputValidator;
import com.nutrihealth.viewModels.ProfileViewModel;
import com.nutrihealth.viewModels.TodayViewModel;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Teodora on 12.04.2018.
 */

public class TodayListFragment extends BaseFragment implements TodayPlannerAdapter.AddProductListener {

    private FragmentTodayBinding binding;
    private TodayViewModel viewModel;
    TodayPlannerAdapter adapter;
    private int perKcal = 0;
    private boolean firstTime = true;


    public static final String TAG = "TodayFragment";

    @Override
    public void onAddProductButtonPrssed(MealsSection section) {
        showAddProductDialog(section);
    }

    private void showAddProductDialog(MealsSection section) {
        final Dialog addProductDialog = new Dialog(getActivity());
        addProductDialog.setContentView(R.layout.dialog_add_product);

        Button saveBtn = addProductDialog.findViewById(R.id.choose_button);
        Button canceBtn = addProductDialog.findViewById(R.id.cancel_button);
        final RadioGroup mealRg = addProductDialog.findViewById(R.id.meal_group);
        RadioButton breackfastRb = addProductDialog.findViewById(R.id.breakfast_rb);
        RadioButton firstSnackRb = addProductDialog.findViewById(R.id.first_snack_rb);
        final RadioButton lunchRb = addProductDialog.findViewById(R.id.lunch_rb);
        final RadioButton secondSnackRb = addProductDialog.findViewById(R.id.second_snack_rb);
        RadioButton dinerRb = addProductDialog.findViewById(R.id.diner_rb);
        final TextInputEditText productNameEt = addProductDialog.findViewById(R.id.product_name_et);
        final TextInputEditText calEt = addProductDialog.findViewById(R.id.cal_number_et);

        switch (section) {
            case BREAKFAST:
                breackfastRb.setChecked(true);
                break;
            case FIRST_SNACK:
                firstSnackRb.setChecked(true);
                break;
            case LUNCH:
                lunchRb.setChecked(true);
                break;
            case SECOND_SNACK:
                secondSnackRb.setChecked(true);
                break;
            case DINNER:
                dinerRb.setChecked(true);
                break;

        }


        canceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductDialog.dismiss();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productNameEt.setError(null);
                calEt.setError(null);

                String productName = productNameEt.getText().toString();
                String calNr = calEt.getText().toString();

                if (InputValidator.isInputEmpty(productName)) {
                    productNameEt.setError(getResources().getString(R.string.error_no_input));
                    return;
                }
                if (InputValidator.isInputEmpty(calNr)) {
                    calEt.setError(getResources().getString(R.string.error_no_input));
                    return;
                }

                int idRadioButton = mealRg.getCheckedRadioButtonId();
                View radioButton = mealRg.findViewById(idRadioButton);
                int idx = mealRg.indexOfChild(radioButton);


                //get firebase user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                switch (idx) {
                    case 0:
                        viewModel.addProduct(new Product(productName, calNr, 0, getTodayInFOrmat(),user.getUid()));
                        break;
                    case 1:
                        viewModel.addProduct(new Product(productName, calNr, 1, getTodayInFOrmat(),user.getUid()));
                        break;
                    case 2:
                        viewModel.addProduct(new Product(productName, calNr, 2, getTodayInFOrmat(),user.getUid()));
                        break;
                    case 3:
                        viewModel.addProduct(new Product(productName, calNr, 3, getTodayInFOrmat(),user.getUid()));
                        break;
                    case 4:
                        viewModel.addProduct(new Product(productName, calNr, 4, getTodayInFOrmat(),user.getUid()));
                        break;
                }

                addProductDialog.dismiss();


            }
        });
        Window window = addProductDialog.getWindow();
        window.setGravity(Gravity.TOP);
        window.getAttributes().windowAnimations = R.style.DialogAnimationUpToDown;
        window.setBackgroundDrawableResource(android.R.color.white);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        addProductDialog.show();
    }

    private void updateLists(List<Product> productList) {

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
        adapter.updateLists(breakfastList, firstSnackList, lunchList, secondSnackList, dinnerList, allKcal ,perKcal);
    }

    public enum MealsSection {
        BREAKFAST,
        FIRST_SNACK,
        LUNCH,
        SECOND_SNACK,
        DINNER
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_today, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setTodayFragment(this);
        viewModel = ViewModelProviders.of(TodayListFragment.this).get(TodayViewModel.class);
        perKcal = PrefsManager.getInstance(getContext()).getKeyKcalPerDay();

        listenToLiveData();

        viewModel.getAllProductsForToday(getTodayInFOrmat());
        setUpViews();


    }

    private void listenToLiveData() {

        viewModel.getTodayHistoryLiveData().observe(TodayListFragment.this, new Observer<Resource<List<Product>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Product>> listResource) {
                if (listResource.getState() == Resource.State.LOADING) {
                } else if (listResource.getState() == Resource.State.ERROR) {
                    showCustomDialog(getResources().getString(R.string.error_title), "", BaseActivity.DialogType.ERROR, null);
                } else if (listResource.getState() == Resource.State.SUCCESS) {
                    if(firstTime){
                        setUpRecyclerView(listResource.getData());
                        firstTime = false;
                        return;
                    }
                    updateLists(listResource.getData());

                }
            }
        });

        viewModel.getSetProductLiveData().observe(TodayListFragment.this, new Observer<Resource<BaseResponse>>() {
            @Override
            public void onChanged(@Nullable Resource<BaseResponse> baseResponseResource) {
                if (baseResponseResource.getState() == Resource.State.LOADING) {
                } else if (baseResponseResource.getState() == Resource.State.ERROR) {
                    showCustomDialog(getResources().getString(R.string.error_title), "", BaseActivity.DialogType.ERROR, null);
                } else if (baseResponseResource.getState() == Resource.State.SUCCESS) {
                    showCustomDialog(getResources().getString(R.string.success), baseResponseResource.getData().getMessage(), BaseActivity.DialogType.SUCCESS, null);

                    viewModel.getAllProductsForToday(getTodayInFOrmat());
                }
            }
        });
    }

    private String getTodayInFOrmat() {
        Format formatter = new SimpleDateFormat("dd-MMM-yy");
        String today = formatter.format(new Date());
        return today;
    }

    private void setUpViews() {

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

        adapter = new TodayPlannerAdapter(getContext(), breakfastList, firstSnackList, lunchList, secondSnackList, dinnerList, allKcal ,perKcal);
        adapter.setAddProductListener(this);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.productsRv.setLayoutManager(llm);
        binding.productsRv.setAdapter(adapter);
    }

}

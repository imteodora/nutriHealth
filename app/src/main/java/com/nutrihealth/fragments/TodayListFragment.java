package com.nutrihealth.fragments;

import android.app.Dialog;
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

import com.nutrihealth.R;
import com.nutrihealth.activities.ProfileActivity;
import com.nutrihealth.adapters.TodayPlannerAdapter;
import com.nutrihealth.base.BaseFragment;
import com.nutrihealth.databinding.FragmentTodayBinding;
import com.nutrihealth.model.Product;
import com.nutrihealth.utils.InputValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Teodora on 12.04.2018.
 */

public class TodayListFragment extends BaseFragment implements TodayPlannerAdapter.AddProductListener {

    private FragmentTodayBinding binding;
    TodayPlannerAdapter adapter;

    List<Product> breakfastList = new ArrayList<>();
    List<Product> firstSnackList = new ArrayList<>();
    List<Product> lunchList = new ArrayList<>();
    List<Product> secondSnackList = new ArrayList<>();
    List<Product> dinnerList = new ArrayList<>();


    public static final String TAG = "TodayFragment";

    @Override
    public void onAddProductButtonPrssed(MealsSection section) {
        showAddProductDialog(section);
    }

    private void showAddProductDialog(MealsSection section) {
        final Dialog addProductDialog = new Dialog(getActivity());
        addProductDialog.setContentView(R.layout.dialog_add_product);

        Button saveBtn = addProductDialog.findViewById(R.id.choose_button);
        Button canceBtn =  addProductDialog.findViewById(R.id.cancel_button);
        final RadioGroup mealRg = addProductDialog.findViewById(R.id.meal_group);
        RadioButton breackfastRb = addProductDialog.findViewById(R.id.breakfast_rb);
        RadioButton firstSnackRb = addProductDialog.findViewById(R.id.first_snack_rb);
        final RadioButton lunchRb = addProductDialog.findViewById(R.id.lunch_rb);
        final RadioButton secondSnackRb = addProductDialog.findViewById(R.id.second_snack_rb);
        RadioButton dinerRb = addProductDialog.findViewById(R.id.diner_rb);
        final TextInputEditText productNameEt =  addProductDialog.findViewById(R.id.product_name_et);
        final TextInputEditText calEt =  addProductDialog.findViewById(R.id.cal_number_et);

        switch (section){
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

                if(InputValidator.isInputEmpty(productName)){
                    productNameEt.setError(getResources().getString(R.string.error_no_input));
                    return;
                }
                if(InputValidator.isInputEmpty(calNr)){
                    calEt.setError(getResources().getString(R.string.error_no_input));
                    return;
                }

                int idRadioButton = mealRg.getCheckedRadioButtonId();
                View radioButton = mealRg.findViewById(idRadioButton);
                int idx = mealRg.indexOfChild(radioButton);

                switch (idx){
                    case 0:
                        breakfastList.add(new Product(productName,calNr));
                        break;
                    case 1:
                        firstSnackList.add(new Product(productName,calNr));
                        break;
                    case 2:
                        lunchList.add(new Product(productName,calNr));
                        break;
                    case 3:
                        secondSnackList.add(new Product(productName,calNr));
                        break;
                    case 4:
                        dinnerList.add(new Product(productName,calNr));
                        break;
                }

                updateLists();
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

    private void updateLists() {
        adapter.updateLists(breakfastList,firstSnackList,lunchList,secondSnackList,dinnerList,"1800");
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
        setUpViews();


    }

    private void setUpViews() {


        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        breakfastList.add(new Product("omleta","150"));
        breakfastList.add(new Product("capsuni","70"));
        breakfastList.add(new Product("shaorma","800"));

        firstSnackList.add(new Product("omleta","150"));
        firstSnackList.add(new Product("capsuni","70"));
        firstSnackList.add(new Product("shaorma","800"));


        secondSnackList.add(new Product("omleta","150"));
        secondSnackList.add(new Product("capsuni","70"));
        secondSnackList.add(new Product("shaorma","800"));

        dinnerList.add(new Product("omleta","150"));
        dinnerList.add(new Product("capsuni","70"));
        dinnerList.add(new Product("shaorma","800"));

        adapter = new TodayPlannerAdapter(getContext(),breakfastList,firstSnackList,lunchList,secondSnackList,dinnerList,"1800");
        adapter.setAddProductListener(this);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.productsRv.setLayoutManager(llm);
        binding.productsRv.setAdapter(adapter);
    }

}

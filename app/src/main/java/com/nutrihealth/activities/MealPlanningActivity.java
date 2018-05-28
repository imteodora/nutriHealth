package com.nutrihealth.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.nutrihealth.R;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.databinding.ActivityMealPlanningBinding;
import com.nutrihealth.databinding.ActivityProfileBinding;
import com.nutrihealth.utils.InputValidator;
import com.nutrihealth.views.CustomToolbar;

/**
 * Created by Teo on 5/28/2018.
 */

public class MealPlanningActivity extends BaseActivity{

    private ActivityMealPlanningBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MealPlanningActivity.this, R.layout.activity_meal_planning);

        binding.setMealPlanningActivity(this);
        binding.setShowProgressBar(false);

        setUpViews();

    }

    private void setUpViews() {

        setStatusBarColor(R.color.black);
        setupUI(binding.parent);
        binding.mealPlanningToolbar.setOnBackButtonPressedListener(new CustomToolbar.OnBackButtonPressedListener() {
            @Override
            public void onBackButtonPressed() {
                finish();

            }
        });
    }

    public void onSearchPressed(View view){

        String firstNutrient = binding.firstNutrientEt.getText().toString();
        String secondNutrient = binding.secondNutrientEt.getText().toString();
        String thirdNutrient = binding.thirdNutrientEt.getText().toString();

        binding.firstNutrientEt.setError(null);
        binding.secondNutrientEt.setError(null);
        binding.thirdNutrientEt.setError(null);

        if(InputValidator.isInputEmpty(firstNutrient)){
            binding.firstNutrientEt.setError(getResources().getString(R.string.error_no_input));
            return;
        }

        if(InputValidator.isInputEmpty(secondNutrient)){
            binding.secondNutrientEt.setError(getResources().getString(R.string.error_no_input));
            return;
        }

        if(InputValidator.isInputEmpty(thirdNutrient)){
            binding.thirdNutrientEt.setError(getResources().getString(R.string.error_no_input));
            return;
        }

    }
}

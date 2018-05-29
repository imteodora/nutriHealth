package com.nutrihealth.activities;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nutrihealth.R;
import com.nutrihealth.adapters.MealPlanAdapter;
import com.nutrihealth.adapters.SearchProductsAdapter;
import com.nutrihealth.api.response.MealPlanFood;
import com.nutrihealth.api.response.MealPlanResponse;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.constants.Constants;
import com.nutrihealth.databinding.ActivityMealPlanningBinding;
import com.nutrihealth.databinding.ActivityProfileBinding;
import com.nutrihealth.fragments.TodayListFragment;
import com.nutrihealth.retrofit.RetrofitClient;
import com.nutrihealth.utils.InputValidator;
import com.nutrihealth.views.CustomToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Teo on 5/28/2018.
 */

public class MealPlanningActivity extends BaseActivity implements MealPlanAdapter.FoodListener {

    private ActivityMealPlanningBinding binding;

    private List<MealPlanFood> foodList;
    private double carbohydrate;
    private double protein;
    private double fat;


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

    public void onSearchPressed(View view) {

        String firstNutrient = binding.firstNutrientEt.getText().toString();
        String secondNutrient = binding.secondNutrientEt.getText().toString();
        String thirdNutrient = binding.thirdNutrientEt.getText().toString();

        binding.firstNutrientEt.setError(null);
        binding.secondNutrientEt.setError(null);
        binding.thirdNutrientEt.setError(null);

        if (InputValidator.isInputEmpty(firstNutrient)) {
            binding.firstNutrientEt.setError(getResources().getString(R.string.error_no_input));
            return;
        }

        if (InputValidator.isInputEmpty(secondNutrient)) {
            binding.secondNutrientEt.setError(getResources().getString(R.string.error_no_input));
            return;
        }

        if (InputValidator.isInputEmpty(thirdNutrient)) {
            binding.thirdNutrientEt.setError(getResources().getString(R.string.error_no_input));
            return;
        }

        carbohydrate = Double.parseDouble(firstNutrient);
        protein = Double.parseDouble(secondNutrient);
        fat = Double.parseDouble(thirdNutrient);

        if (foodList == null) {
            getNutrientsReport();
        } else {
            generateMealPlan();
        }

    }

    private void getNutrientsReport() {

        binding.setShowProgressBar(true);
        Call<MealPlanResponse> nutrientsReportRequest = RetrofitClient.getWebServices().sendNutrientsReportRequest("json", Constants.API_KEY, "100", "c", "205", "204", "203");
        nutrientsReportRequest.enqueue(new Callback<MealPlanResponse>() {
            @Override
            public void onResponse(Call<MealPlanResponse> call, Response<MealPlanResponse> response) {
                if (response.isSuccessful() && response.body().report != null) {
                    foodList = new ArrayList<>();
                    foodList = response.body().report.mealPlanFoodList;
                    generateMealPlan();
                }
            }

            @Override
            public void onFailure(Call<MealPlanResponse> call, Throwable t) {
                binding.setShowProgressBar(true);
                showCustomDialog(getResources().getString(R.string.error_title), "", DialogType.ERROR, null);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void generateMealPlan() {

        binding.setShowProgressBar(true);

        final AsyncTask<Void, Void, List<MealPlanFood>> task = new AsyncTask<Void, Void, List<MealPlanFood>>() {
            @Override
            protected List<MealPlanFood> doInBackground(Void... booleen) {
                Random rand = new Random();
                double[] remainingNutrients = {carbohydrate, protein, fat};
                int[] foodRejaction = {0, 0, 0};

                List<MealPlanFood> finalFoodList = new ArrayList<>();
                MealPlanFood currentFood;

                for (int i = 0; i < 3; i++) {

                    while (remainingNutrients[i] >= 10.0) {

                        if (isCancelled())
                            return null;


                        int randomPoz = rand.nextInt(100);
                        currentFood = foodList.get(randomPoz);

                        if (!findFoodInList(currentFood, finalFoodList)) {

                            if ((remainingNutrients[0] - currentFood.nutrientList.get(2).valuePer100g) >= 0.0) {

                                if ((remainingNutrients[1] - currentFood.nutrientList.get(0).valuePer100g) >= 0.0) {

                                    if ((remainingNutrients[2] - currentFood.nutrientList.get(1).valuePer100g) >= 0.0) {

                                        finalFoodList.add(currentFood);
                                        remainingNutrients[0] = remainingNutrients[0] - currentFood.nutrientList.get(2).valuePer100g;
                                        remainingNutrients[1] = remainingNutrients[1] - currentFood.nutrientList.get(0).valuePer100g;
                                        remainingNutrients[2] = remainingNutrients[2] - currentFood.nutrientList.get(1).valuePer100g;

                                    } else {
                                        foodRejaction[2]++;
                                        if (foodRejaction[2] == 3) {
                                            foodRejaction[2] = 0;
                                            finalFoodList = deleteMaxFat(finalFoodList);
                                            if(deletedFood != null){
                                                remainingNutrients[0] = remainingNutrients[0] + deletedFood.nutrientList.get(2).valuePer100g;
                                                remainingNutrients[1] = remainingNutrients[1] + deletedFood.nutrientList.get(0).valuePer100g;
                                                remainingNutrients[2] = remainingNutrients[2] + deletedFood.nutrientList.get(1).valuePer100g;
                                                deletedFood = null;
                                            }
                                        }
                                    }

                                } else {
                                    foodRejaction[1]++;
                                    if (foodRejaction[1] == 3) {
                                        foodRejaction[1] = 0;
                                        finalFoodList = deleteMaxProtein(finalFoodList);
                                        if(deletedFood != null){
                                            remainingNutrients[0] = remainingNutrients[0] + deletedFood.nutrientList.get(2).valuePer100g;
                                            remainingNutrients[1] = remainingNutrients[1] + deletedFood.nutrientList.get(0).valuePer100g;
                                            remainingNutrients[2] = remainingNutrients[2] + deletedFood.nutrientList.get(1).valuePer100g;
                                            deletedFood = null;
                                        }
                                    }
                                }


                            } else {
                                foodRejaction[0]++;
                                if (foodRejaction[0] == 3) {
                                    foodRejaction[0] = 0;
                                    finalFoodList = deleteMaxCarb(finalFoodList);
                                    if(deletedFood != null){
                                        remainingNutrients[0] = remainingNutrients[0] + deletedFood.nutrientList.get(2).valuePer100g;
                                        remainingNutrients[1] = remainingNutrients[1] + deletedFood.nutrientList.get(0).valuePer100g;
                                        remainingNutrients[2] = remainingNutrients[2] + deletedFood.nutrientList.get(1).valuePer100g;
                                        deletedFood = null;
                                    }
                                }
                            }

                        }

                    }

                }
               return finalFoodList;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(List<MealPlanFood> foodList) {
                super.onPostExecute(foodList);

                binding.setShowProgressBar(false);

                if( foodList != null && foodList.size() > 0){
                    binding.foodRv.setVisibility(View.VISIBLE);
                    RecyclerView.LayoutManager llm = new LinearLayoutManager(MealPlanningActivity.this, LinearLayoutManager.VERTICAL, false);
                    MealPlanAdapter adapter = new MealPlanAdapter(MealPlanningActivity.this, foodList );
                    adapter.setListener(MealPlanningActivity.this);
                    binding.foodRv.setLayoutManager(llm);
                    binding.foodRv.setAdapter(adapter);
                }else{
                    binding.foodRv.setVisibility(View.GONE);
                    showCustomDialog(getResources().getString(R.string.error_title), "Please try again with other values", DialogType.ERROR, null);
                }

            }
        };
        task.execute();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if( task.getStatus() != AsyncTask.Status.FINISHED){
                    binding.setShowProgressBar(false);
                    task.cancel(true);
                    binding.foodRv.setVisibility(View.GONE);
                    showCustomDialog(getResources().getString(R.string.error_title), "Please try again with other values", DialogType.ERROR, null);
                }
            }
        }, 20000);






    }

    private MealPlanFood deletedFood;

    private List<MealPlanFood> deleteMaxCarb(List<MealPlanFood> finalFoodList) {

        double max = 0;
        int pos = -1;
        for (int i = 0; i < finalFoodList.size(); i++) {
            if (finalFoodList.get(i).nutrientList.get(2).valuePer100g > max) {
                max = finalFoodList.get(i).nutrientList.get(2).valuePer100g;
                pos = i;
            }
        }

        if (pos != -1) {
            deletedFood = finalFoodList.get(pos);
            finalFoodList.remove(pos);

        }

        return finalFoodList;

    }


    private List<MealPlanFood> deleteMaxProtein(List<MealPlanFood> finalFoodList) {

        double max = 0;
        int pos = -1;
        for (int i = 0; i < finalFoodList.size(); i++) {
            if (finalFoodList.get(i).nutrientList.get(0).valuePer100g > max) {
                max = finalFoodList.get(i).nutrientList.get(0).valuePer100g;
                pos = i;
            }
        }

        if (pos != -1) {
            deletedFood = finalFoodList.get(pos);
            finalFoodList.remove(pos);
        }

        return finalFoodList;

    }


    private List<MealPlanFood> deleteMaxFat(List<MealPlanFood> finalFoodList) {

        double max = 0;
        int pos = -1;
        for (int i = 0; i < finalFoodList.size(); i++) {
            if (finalFoodList.get(i).nutrientList.get(1).valuePer100g > max) {
                max = finalFoodList.get(i).nutrientList.get(1).valuePer100g;
                pos = i;
            }
        }

        if (pos != -1) {
            deletedFood = finalFoodList.get(pos);
            finalFoodList.remove(pos);
        }

        return finalFoodList;

    }

    private boolean findFoodInList(MealPlanFood food, List<MealPlanFood> foodList) {

        for (MealPlanFood f : foodList) {
            if (f.id.equals(food.id)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onProductPressed(int position) {

    }
}

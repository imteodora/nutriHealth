package com.nutrihealth.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Teo on 5/29/2018.
 */

public class MealPlanNutrient {

    @SerializedName("nutrient_id")
    public String nutrientsId;

    @SerializedName("gm")
    public double valuePer100g;

}

package com.nutrihealth.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Teo on 5/29/2018.
 */

public class MealPlanFood {

    @SerializedName("ndbno")
    public String id;

    @SerializedName("name")
    public String foodName;

    @SerializedName("nutrients")
    public List<MealPlanNutrient> nutrientList;

}

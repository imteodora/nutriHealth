package com.nutrihealth.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Teo on 5/12/2018.
 */

public class Nutrient {

    @SerializedName("name")
    public String nutrientName;

    @SerializedName("unit")
    public String unit;

    @SerializedName("value")
    public String value;

}

package com.nutrihealth.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Teo on 5/12/2018.
 */

public class ProductItem {

    @SerializedName("name")
    public String name;

    @SerializedName("ndbno")
    public String number;
}

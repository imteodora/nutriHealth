package com.nutrihealth.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Teo on 5/12/2018.
 */

public class SearchList {

    @SerializedName("q")
    public String productSearched;

    @SerializedName("item")
    public List<ProductItem> productsList;
}

package com.nutrihealth.api;

import com.nutrihealth.api.response.MealPlanResponse;
import com.nutrihealth.api.response.NutrientsResponse;
import com.nutrihealth.api.response.SearchProductResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Teo on 5/11/2018.
 */

public interface NutriHealthApiServices {


    @GET("nutrients")
    Call<MealPlanResponse> sendNutrientsReportRequest(@Query("format") String format,@Query("api_key") String apiKey, @Query("max") String max, @Query("sort") String sort, @Query("nutrients") String nutrient1, @Query("nutrients") String nutrient2, @Query("nutrients") String nutrients3 );

    @GET("search")
    Call<SearchProductResponse> sendSearchProductRequest(@Query("format") String format, @Query("q") String productName,@Query("ds") String ds, @Query("sort") String sort, @Query("max") String maxItems,@Query("offset") String offset, @Query("api_key") String apiKey);

    @GET("V2/reports")
    Call<NutrientsResponse> sendGetNutrientsRequest(@Query("ndbno") String number, @Query("type") String type, @Query("format") String format, @Query("api_key") String apiKey);
}

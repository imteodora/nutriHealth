package com.nutrihealth.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Teo on 5/1/2018.
 */

public interface TestApiServices {

    @GET("discover/movie")
    Call<MovieResponse> getMovies(@Query("page") int page,
                                  @Query("api_key") String apiKey);
}

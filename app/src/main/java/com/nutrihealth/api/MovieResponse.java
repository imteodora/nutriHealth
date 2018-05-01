package com.nutrihealth.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Teo on 5/1/2018.
 */

public class MovieResponse {
    @SerializedName("page")
    private int page;

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("results")
    private List<Movie> movies;


    public int getPage() {
        return page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<Movie> getResults() {
        return movies;
    }
}

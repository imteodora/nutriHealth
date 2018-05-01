package com.nutrihealth.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Teo on 5/1/2018.
 */

public class Movie {

    @SerializedName("title")
    public String title;

    @SerializedName("overview")
    public String description;


    @SerializedName("poster_path")
    public String imageURL;

    @SerializedName("vote_count")
    public Integer nrLikes;

    @SerializedName("id")
    public int id;

    public Movie(String title, String description, Integer nrLikes,int id){
        this.title = title;
        this.description = description;
        this.nrLikes = nrLikes;
        this.id = id;
    }

    public String getImageUrl(){
        return "https://image.tmdb.org/t/p/w500/" + imageURL;
    }


}

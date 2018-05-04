package com.nutrihealth.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Teo on 5/3/2018.
 */
@IgnoreExtraProperties
public class Alarm {

    private String breakfast;
    private String firstSnack;
    private String lunch;
    private String secondSnack;
    private String dinner;
    private String water;

    public Alarm() {
    }

    public String getBreakfast() {
        return breakfast;
    }

    public String getFirstSnack() {
        return firstSnack;
    }

    public String getLunch() {
        return lunch;
    }

    public String getSecondSnack() {
        return secondSnack;
    }

    public String getDinner() {
        return dinner;
    }

    public String getWater() {
        return water;
    }

    public Alarm(String breakfast, String firstSnack, String lunch, String secondSnack, String dinner, String water) {
        this.breakfast = breakfast;
        this.firstSnack = firstSnack;
        this.lunch = lunch;
        this.secondSnack = secondSnack;
        this.dinner = dinner;
        this.water = water;
    }
}

package com.nutrihealth.model;

/**
 * Created by Teo on 4/1/2018.
 */

public class HomeInfos {

    private String image;
    private int actualWeight;
    private int idealWeight;
    private int kcalPerDay;

    public HomeInfos(String image, int actualWeight, int idealWeight, int kcalPerDay) {
        this.image = image;
        this.actualWeight = actualWeight;
        this.idealWeight = idealWeight;
        this.kcalPerDay = kcalPerDay;
    }

    public String getImage() {
        return image;
    }

    public int getActualWeight() {
        return actualWeight;
    }

    public int getIdealWeight() {
        return idealWeight;
    }

    public int getKcalPerDay() {
        return kcalPerDay;
    }
}

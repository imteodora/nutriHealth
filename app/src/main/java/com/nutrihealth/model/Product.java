package com.nutrihealth.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Teo on 4/18/2018.
 */

@IgnoreExtraProperties
public class Product {

    private int type;
    private String name;
    private String kcal;
    private String date;
    private String userId;

    public Product() {
    }


    public Product(String name, String kcal, int type, String date, String userId) {
        this.name = name;
        this.kcal = kcal;
        this.type = type;
        this.date = date;
        this.userId = userId;
    }



    public int getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getKcal() {
        return kcal;
    }

    public String getUserId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }
}

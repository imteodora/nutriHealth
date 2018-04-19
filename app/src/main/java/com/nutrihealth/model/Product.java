package com.nutrihealth.model;

/**
 * Created by Teo on 4/18/2018.
 */

public class Product {

    private int type;
    private String name;
    private String kcal;
    private String date;


    public Product(String name, String kcal,int type, String date) {
        this.name = name;
        this.kcal = kcal;
        this.type = type;
        this.date = date;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }
}

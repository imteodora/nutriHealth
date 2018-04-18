package com.nutrihealth.model;

/**
 * Created by Teo on 4/18/2018.
 */

public class Product {

    private String name;
    private String kcal;

    public Product(String name, String kcal) {
        this.name = name;
        this.kcal = kcal;
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

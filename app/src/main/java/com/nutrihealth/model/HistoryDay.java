package com.nutrihealth.model;

/**
 * Created by Teo on 4/18/2018.
 */

public class HistoryDay {

    private String date;
    private String kcal;

    public HistoryDay(String date, String kcal) {
        this.date = date;
        this.kcal = kcal;
    }

    public String getDate() {
        return date;
    }

    public String getKcal() {
        return kcal;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }
}

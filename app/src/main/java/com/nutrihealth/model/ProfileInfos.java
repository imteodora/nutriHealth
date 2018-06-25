package com.nutrihealth.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Teo on 3/31/2018.
 */

@IgnoreExtraProperties
public class ProfileInfos {

    private String picture;
    private String name;
    private int actualWeight;
    private String gender;
    private int height;
    private int age;
    private int activityLvl;
    private int idealWeight;
    private int kcalPerDay;

    public String getPicture() {
        return picture;
    }

    public int getIdealWeight() {
        return idealWeight;
    }

    public int getKcalPerDay() {
        return kcalPerDay;
    }

    public ProfileInfos(String picture, String name, int actualWeight, String gender, int height, int age, int activityLvl, int idealWeight, int kcalPerDay) {
        this.picture = picture;
        this.name = name;
        this.actualWeight = actualWeight;
        this.gender = gender;
        this.height = height;
        this.age = age;
        this.activityLvl = activityLvl;
        this.idealWeight = idealWeight;
        this.kcalPerDay = kcalPerDay;
    }

    public ProfileInfos() {
    }

    public ProfileInfos(String name, int actualWeight, String gender, int height, int age, int activityLvl) {
        this.name = name;
        this.actualWeight = actualWeight;
        this.gender = gender;
        this.height = height;
        this.age = age;
        this.activityLvl = activityLvl;
    }

    public String getName() {
        return name;
    }

    public int getActualWeight() {
        return actualWeight;
    }

    public String getGender() {
        return gender;
    }

    public int getHeight() {
        return height;
    }

    public int getAge() {
        return age;
    }

    public int getActivityLvl() {
        return activityLvl;
    }
}

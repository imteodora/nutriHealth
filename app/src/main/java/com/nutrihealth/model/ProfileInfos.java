package com.nutrihealth.model;

/**
 * Created by Teo on 3/31/2018.
 */

public class ProfileInfos {

    private String name;
    private int actualWeight;
    private String gender;
    private int height;
    private int age;

    public ProfileInfos(String name, int actualWeight, String gender, int height, int age) {
        this.name = name;
        this.actualWeight = actualWeight;
        this.gender = gender;
        this.height = height;
        this.age = age;
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
}
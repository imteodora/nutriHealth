package com.nutrihealth.utils;

import com.nutrihealth.constants.Constants;

/**
 * Created by Teo on 3/31/2018.
 */

public class WeightUtils {

    public static int calculateIdealWeight(int height, int age, String gender) {
        double idealWeight = 0;

        if (gender.equals(Constants.GENDER_M)) {
            idealWeight = height - 100 - ((height - 150) / 4 + (age - 20) / 4);
        }

        if (gender.equals(Constants.GENDER_F)) {
            idealWeight = height - 100 - ((height - 150) / (2.5) + (age - 20) / 6);
        }

        return (int) idealWeight;
    }

    public static int calculateCalPerDay(int height, int age, int idealWeight, String gender, double sport) {
        double idealCal = 0;

        if (gender.equals(Constants.GENDER_M)) {
            idealCal = 10 * idealWeight + 6.25 * height - 5 * age + 5;
        }

        if (gender.equals(Constants.GENDER_F)) {
            idealCal = 10 * idealWeight + 6.25 * height - 5 * age - 161;
        }

        return (int) (idealCal * sport);
    }
}

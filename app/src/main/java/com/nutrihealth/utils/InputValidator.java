package com.nutrihealth.utils;

import android.util.Patterns;

/**
 * Created by Teodora on 02.11.2017.
 */

public class InputValidator {


    public static boolean isInputEmpty(String input) {
        return input == null || input.equalsIgnoreCase("");
    }

    public static boolean isValidEmail(String target) {
        return (!isInputEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }



}

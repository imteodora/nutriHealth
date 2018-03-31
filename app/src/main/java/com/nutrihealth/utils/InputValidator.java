package com.nutrihealth.utils;

/**
 * Created by Teodora on 02.11.2017.
 */

public class InputValidator {

    private static final String EMAIL_PATTERN = "^([\\w\\.-]+@([\\w-]+\\.)+[\\w-]{2,4})?$";

    public static boolean isInputEmpty(String input) {
        return input == null || input.equalsIgnoreCase("");
    }



}

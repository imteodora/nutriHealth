package com.nutrihealth.model;

/**
 * Created by Teo on 3/31/2018.
 */

public class BaseResponse {

    private String message;

    public BaseResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

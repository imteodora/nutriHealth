package com.nutrihealth.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by erkut on 10/17/17.
 */
public class Resource<T> {

    public enum State {
        SUCCESS,
        LOADING,
        ERROR
    }

    @NonNull
    private final State mState;
    @Nullable
    private final T mData;
    @Nullable
    private final String mMessage;

    private Resource(@NonNull State state, T data, String message) {
        mState = state;
        mData = data;
        mMessage = message;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(State.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String message) {
        return new Resource<>(State.ERROR, null, message);
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(State.LOADING, null, null);
    }

    @NonNull
    public State getState() {
        return mState;
    }

    @Nullable
    public T getData() {
        return mData;
    }

    @Nullable
    public String getMessage() {
        return mMessage;
    }
}

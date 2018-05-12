package com.nutrihealth.retrofit;


import com.google.gson.GsonBuilder;
import com.nutrihealth.api.NutriHealthApiServices;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Teodora on 31.10.2017.
 */

public class RetrofitClient {

    public static final String NUTRIHEALTH_BASE_URL = "https://api.nal.usda.gov/ndb/";

    private static NutriHealthApiServices webServices;

    public static NutriHealthApiServices getWebServices() {
        if (webServices == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(loggingInterceptor)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS);
            OkHttpClient client = httpClient.build();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NUTRIHEALTH_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                    .client(client)
                    .build();

            webServices = retrofit.create(NutriHealthApiServices.class);
        }

        return webServices;
    }
}

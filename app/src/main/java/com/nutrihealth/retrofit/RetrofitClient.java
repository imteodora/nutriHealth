package com.nutrihealth.retrofit;

import com.google.gson.GsonBuilder;
import com.nutrihealth.api.TestApiServices;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Teo on 5/1/2018.
 */

public class RetrofitClient {


    public static final String TEST_BASE_URL = "https://api.themoviedb.org/3/";

    private static TestApiServices webServices;

    public static TestApiServices getWebServices() {
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
                    .baseUrl(TEST_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                    .client(client)
                    .build();

            webServices = retrofit.create(TestApiServices.class);
        }

        return webServices;
    }
}

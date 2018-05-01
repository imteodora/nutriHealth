package com.nutrihealth.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.widget.Toast;

import com.nutrihealth.R;
import com.nutrihealth.adapters.TestAdapter;
import com.nutrihealth.api.Movie;
import com.nutrihealth.api.MovieResponse;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.databinding.ActivityAlarmsBinding;
import com.nutrihealth.databinding.ActivityTestBinding;
import com.nutrihealth.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Teo on 5/1/2018.
 */

public class TestActivity extends BaseActivity {

    private ActivityTestBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(TestActivity.this, R.layout.activity_test);
        getMovies(1, "1906833e4f7429ebcd27cb7a04da7c0a");


    }

    private void getMovies(int page, String apiKey) {

        Call<MovieResponse> call = RetrofitClient.getWebServices().getMovies(page,apiKey);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null) {
                        Toast.makeText(TestActivity.this, "Response is succesfull", Toast.LENGTH_SHORT).show();
                        initializeRecyclerView(movieResponse.getResults());
                    }
                } else {
                    Toast.makeText(TestActivity.this, "Response is not succesfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });

    }

    public void initializeRecyclerView(List<Movie> movieList) {

        GridLayoutManager gridLayoutMan;
        gridLayoutMan = new GridLayoutManager(TestActivity.this, 2);
        gridLayoutMan.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if ((position % 3) == 0) return 2;
                else return 1;
            }
        });
        binding.moviesRv.setLayoutManager(gridLayoutMan);

        TestAdapter rcAdapter = new TestAdapter(movieList);
        binding.moviesRv.setAdapter(rcAdapter);
        binding.moviesRv.setHasFixedSize(true);

    }
}

package com.example.projectfinal.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();


    public Retrofit getRetrofit() {
        return retrofit;
    }



}

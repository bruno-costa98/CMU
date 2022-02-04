package com.example.projectfinal.Interfaces;

import com.example.projectfinal.Models.Results;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlacesApi {

//    @GET("place/nearbysearch/json?location=41.125851,-8.597474&radius=500&types=gym&?")
//    Call<Results> getPlaces(@Query("key") String key);

    @GET("place/nearbysearch/json?")
    Call<Results> getPlaces(@Query("location")String location,
                            @Query("radius") int radius,
                            @Query("types") String types,
                            @Query("key") String key);
}

package com.example.hospitalwaka.Network;

import com.example.hospitalwaka.Models.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("place/nearbysearch/json")
    Call<Model> getNearBy(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("keyword") String keyword,
            @Query("key") String key
    );

}

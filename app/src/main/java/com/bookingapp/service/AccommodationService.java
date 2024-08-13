package com.bookingapp.service;

import com.bookingapp.model.Accommodation;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface AccommodationService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("api/accommodations")
    Call<ArrayList<Accommodation>> getAll();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET("api/accommodations/{id}")
    Call<Accommodation> getById(@Path("id") Long id);

}

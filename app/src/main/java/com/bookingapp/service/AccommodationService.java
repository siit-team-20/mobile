package com.bookingapp.service;

import com.bookingapp.model.Accommodation;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface AccommodationService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("accommodations")
    Call<ArrayList<Accommodation>> getAll();
}

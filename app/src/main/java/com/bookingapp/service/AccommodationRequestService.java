package com.bookingapp.service;

import com.bookingapp.model.Accommodation;
import com.bookingapp.model.AccommodationRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccommodationRequestService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("api/accommodations/requests")
    Call<AccommodationRequest> add(@Body AccommodationRequest accommodationRequest);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("api/accommodations/requests")
    Call<ArrayList<AccommodationRequest>> getAll();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @DELETE("api/accommodations/requests/{id}")
    Call<AccommodationRequest> delete(@Path("id") Long id);
}

package com.bookingapp.service;

import com.bookingapp.model.Accommodation;
import com.bookingapp.model.AccommodationRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AccommodationRequestService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("api/accommodations/requests")
    Call<AccommodationRequest> add(@Body AccommodationRequest accommodationRequest);
}

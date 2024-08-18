package com.bookingapp.service;

import com.bookingapp.model.Accommodation;
import com.bookingapp.model.AccommodationReview;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface AccommodationReviewService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("api/accommodations/reviews")
    Call<ArrayList<AccommodationReview>> get(@Query("accommodationId") Long accommodationId, @Query("onlyNotApproved") boolean onlyNotApproved);
}

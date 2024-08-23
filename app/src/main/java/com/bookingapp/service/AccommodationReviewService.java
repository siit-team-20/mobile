package com.bookingapp.service;

import com.bookingapp.model.Accommodation;
import com.bookingapp.model.AccommodationReview;
import com.bookingapp.model.ReservationWithAccommodation;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccommodationReviewService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("api/accommodations/reviews")
    Call<ArrayList<AccommodationReview>> get(@Query("accommodationId") Long accommodationId, @Query("onlyNotApproved") boolean onlyNotApproved);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("api/accommodations/reviews")
    Call<AccommodationReview> add(@Body AccommodationReview accommodationReview);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @DELETE("api/accommodations/reviews/{id}")
    Call<AccommodationReview> delete(@Path("id") Long id);
}

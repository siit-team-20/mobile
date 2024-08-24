package com.bookingapp.service;

//import com.bookingapp.model.AccommodationReview;

import com.bookingapp.model.AccommodationReview;
import com.bookingapp.model.OwnerReview;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OwnerReviewService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("api/ownerReviews")
    Call<ArrayList<OwnerReview>> get(@Query("ownerEmail") String ownerEmail, @Query("isReported") boolean isReported);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("api/ownerReviews")
    Call<ArrayList<OwnerReview>> get(@Query("isReported") boolean isReported);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("api/ownerReviews")
    Call<OwnerReview> add(@Body OwnerReview ownerReview);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @PUT("api/ownerReviews/{id}")
    Call<OwnerReview> update(@Path("id") Long id, @Body OwnerReview ownerReview);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @DELETE("api/ownerReviews/{id}")
    Call<OwnerReview> delete(@Path("id") Long id);
}

package com.bookingapp.service;

import com.bookingapp.model.Accommodation;
import com.bookingapp.model.Reservation;

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

public interface AccommodationService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("api/accommodations")
    Call<ArrayList<Accommodation>> getAll();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("api/accommodations")
    Call<ArrayList<Accommodation>> get(@Query("ownerEmail") String ownerEmail);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @PUT("api/accommodations/{id}")
    Call<Accommodation> update(@Path("id") Long id, @Body Accommodation accommodation);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @DELETE("api/accommodations")
    Call<ArrayList<Accommodation>> delete(@Query("ownerEmail") String ownerEmail);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("api/accommodations")
    Call<ArrayList<Accommodation>> get(@Query("onlyApproved") boolean onlyApproved);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET("api/accommodations/{id}")
    Call<Accommodation> getById(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("api/accommodations")
    Call<Accommodation> add(@Body Accommodation accommodation);

}

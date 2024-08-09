package com.bookingapp.service;

import com.bookingapp.model.Accommodation;
import com.bookingapp.model.Reservation;
import com.bookingapp.model.ReservationWithAccommodation;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ReservationService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accommodations/reservations")
    Call<Reservation> add(@Body Reservation reservation);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("accommodations/reservations")
    Call<ArrayList<ReservationWithAccommodation>> getAll();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("accommodations/reservations")
    Call<ArrayList<ReservationWithAccommodation>> get(@Query("accommodationId") Long accommodationId, @Query("status") String status);
}

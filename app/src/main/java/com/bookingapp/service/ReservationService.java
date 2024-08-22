package com.bookingapp.service;

import com.bookingapp.model.Accommodation;
import com.bookingapp.model.Reservation;
import com.bookingapp.model.ReservationWithAccommodation;

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

public interface ReservationService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("api/accommodations/reservations")
    Call<Reservation> add(@Body Reservation reservation);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("api/accommodations/reservations")
    Call<ArrayList<ReservationWithAccommodation>> getAll();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("api/accommodations/reservations")
    Call<ArrayList<ReservationWithAccommodation>> getByGuestEmail(@Query("guestEmail") String guestEmail);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("api/accommodations/reservations")
    Call<ArrayList<ReservationWithAccommodation>> getByOwnerEmail(@Query("ownerEmail") String ownerEmail);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("api/accommodations/reservations")
    Call<ArrayList<ReservationWithAccommodation>> get(@Query("accommodationId") Long accommodationId, @Query("status") String status);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("api/accommodations/reservations")
    Call<ArrayList<ReservationWithAccommodation>> get(@Query("guestEmail") String guestEmail, @Query("status") String status);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("api/accommodations/reservations")
    Call<ArrayList<ReservationWithAccommodation>> getGuestReservations(@Query("guestEmail") String guestEmail, @Query("status") String status);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("api/accommodations/reservations")
    Call<ArrayList<ReservationWithAccommodation>> getOwnerReservations(@Query("ownerEmail") String ownerEmail, @Query("status") String status);


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @DELETE("api/accommodations/reservations")
    Call<ArrayList<ReservationWithAccommodation>> deleteGuestReservations(@Query("guestEmail") String guestEmail, @Query("status") String status);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @DELETE("api/accommodations/reservations")
    Call<ArrayList<ReservationWithAccommodation>> deleteOwnerReservations(@Query("ownerEmail") String ownerEmail, @Query("status") String status);


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @PUT("api/accommodations/reservations/{id}")
    Call<Reservation> update(@Path("id") Long id, @Body Reservation accommodationReservation);

}

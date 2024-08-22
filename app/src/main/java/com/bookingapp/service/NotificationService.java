package com.bookingapp.service;

import com.bookingapp.model.Notification;
import com.bookingapp.model.Reservation;
import com.bookingapp.model.ReservationWithAccommodation;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NotificationService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("api/notifications")
    Call<Notification> add(@Body Notification notification);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("api/notifications")
    Call<ArrayList<Notification>> getAll();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("api/notifications")
    Call<ArrayList<Notification>> get(@Query("userEmail") String userEmail);
}

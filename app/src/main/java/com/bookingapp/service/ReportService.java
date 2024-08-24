package com.bookingapp.service;

import com.bookingapp.model.AccommodationRequest;
import com.bookingapp.model.Notification;
import com.bookingapp.model.Report;
import com.bookingapp.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReportService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("api/reports")
    Call<Report> add(@Body Report report);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("api/reports")
    Call<ArrayList<Report>> getAll();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @DELETE("api/reports/{id}")
    Call<Report> delete(@Path("id") Long id);
}

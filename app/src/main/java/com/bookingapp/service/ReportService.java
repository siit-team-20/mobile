package com.bookingapp.service;

import com.bookingapp.model.Notification;
import com.bookingapp.model.Report;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ReportService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("api/reports")
    Call<Report> add(@Body Report report);
}

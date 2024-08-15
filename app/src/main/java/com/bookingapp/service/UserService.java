package com.bookingapp.service;

import com.bookingapp.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("users/{email}")
    Call<User> getUser(@Path("email") String email);
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("account/{email}")
    Call<User> update(@Query("signUpDTO") User user, @Path("email") String email);
}

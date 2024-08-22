package com.bookingapp.service;

import com.bookingapp.model.Credentials;
import com.bookingapp.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @POST("login")
    Call<User> login(@Body Credentials credentials);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @POST("register")
    Call<User> register(@Body User user);

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
    @PUT("account/{email}")
    Call<User> update(@Path("email") String email, @Body User user);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @DELETE("account/{email}")
    Call<User> delete(@Path("email") String email);
}

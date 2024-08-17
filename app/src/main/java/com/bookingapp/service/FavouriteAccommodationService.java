package com.bookingapp.service;

import com.bookingapp.model.Accommodation;
import com.bookingapp.model.FavouriteAccommodationWithAccommodation;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FavouriteAccommodationService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @GET("api/accommodations/favourites")
    Call<ArrayList<FavouriteAccommodationWithAccommodation>> get(@Query("guestEmail") String guestEmail);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
    })
    @POST("api/accommodations/favourites")
    Call<FavouriteAccommodationWithAccommodation> add(@Body FavouriteAccommodationWithAccommodation favouriteAccommodation);
}

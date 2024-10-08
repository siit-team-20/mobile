package com.bookingapp.service;

import com.bookingapp.BuildConfig;
import com.bookingapp.model.AccommodationReview;
import com.bookingapp.model.FavouriteAccommodationWithAccommodation;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceUtils {
    public static final String SERVICE_API_PATH = "http://" + BuildConfig.IP_ADDR + ":8080/";

    public static String getAuthorizationToken() {
        try {
            if (UserInfo.getType() == null)
                return "";
            return UserInfo.getToken();
        }
        catch (Exception e) {
            return "";
        }
    }

    public static OkHttpClient test() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @Override public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + getAuthorizationToken()).build();
                        return chain.proceed(request);
                    }
                })
                .build();

        return client;
    }

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create())
            .client(test())
            .build();

    public static AccommodationService accommodationService = retrofit.create(AccommodationService.class);
    public static ReservationService reservationService = retrofit.create(ReservationService.class);
    public static UserService userService = retrofit.create(UserService.class);
    public static FavouriteAccommodationService favouriteAccommodationService = retrofit.create(FavouriteAccommodationService.class);
    public static AccommodationReviewService accommodationReviewService = retrofit.create(AccommodationReviewService.class);
    public static NotificationService notificationService = retrofit.create(NotificationService.class);
    public static OwnerReviewService ownerReviewService = retrofit.create(OwnerReviewService.class);
    public static AccommodationRequestService accommodationRequestService = retrofit.create(AccommodationRequestService.class);
    public static ReportService reportService = retrofit.create(ReportService.class);
}

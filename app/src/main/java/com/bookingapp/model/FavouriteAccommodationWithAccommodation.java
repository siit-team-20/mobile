package com.bookingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class FavouriteAccommodationWithAccommodation implements Parcelable {
    private Long id;
    private String guestEmail;
    private Accommodation accommodation;

    public FavouriteAccommodationWithAccommodation() {

    }

    public FavouriteAccommodationWithAccommodation(Long id, String guestEmail, Accommodation accommodation) {
        this.id = id;
        this.guestEmail = guestEmail;
        this.accommodation = accommodation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    protected FavouriteAccommodationWithAccommodation(Parcel in) {
        id = in.readLong();
        guestEmail = in.readString();
        accommodation = in.readParcelable(Accommodation.class.getClassLoader(), Accommodation.class);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(guestEmail);
        dest.writeParcelable(accommodation, 0);
    }

    public static final Parcelable.Creator<Reservation> CREATOR = new Parcelable.Creator<Reservation>() {
        @Override
        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        @Override
        public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };
}

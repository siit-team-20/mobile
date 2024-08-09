package com.bookingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ReservationWithAccommodation implements Parcelable {
    private Long id;
    private String guestEmail;
    private Accommodation accommodation;
    private List<Integer> date;
    private int days;
    private int guestNumber;
    private double price;
    private ReservationStatus status;

    public ReservationWithAccommodation() {

    }

    public ReservationWithAccommodation(Long id, String guestEmail, Accommodation accommodation, List<Integer> date, int days, int guestNumber, double price, ReservationStatus status) {
        super();
        this.id = id;
        this.guestEmail = guestEmail;
        this.accommodation = accommodation;
        this.date = date;
        this.days = days;
        this.guestNumber = guestNumber;
        this.price = price;
        this.status = status;
    }

    protected ReservationWithAccommodation(Parcel in) {
        id = in.readLong();
        guestEmail = in.readString();
        accommodation = in.readParcelable(Accommodation.class.getClassLoader(), Accommodation.class);
        date = new ArrayList<Integer>();
        in.readList(date, Integer.class.getClassLoader(), Integer.class);
        days = in.readInt();
        guestNumber = in.readInt();
        price = in.readDouble();
        status = ReservationStatus.valueOf(in.readString());
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
        dest.writeList(date);
        dest.writeInt(guestNumber);
        dest.writeDouble(price);
        dest.writeString(status.name());
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

    public List<Integer> getDate() {
        return date;
    }

    public void setDate(List<Integer> date) {
        this.date = date;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
    public int getGuestNumber() {
        return guestNumber;
    }

    public void setGuestNumber(int guestNumber) {
        this.guestNumber = guestNumber;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public ReservationStatus getStatus() {
        return status;
    }
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}

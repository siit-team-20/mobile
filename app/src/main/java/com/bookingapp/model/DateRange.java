package com.bookingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DateRange implements Parcelable {
    private Long id;
    private List<Integer> startDate;
    private List<Integer> endDate;
    private double price;
    private Long accommodationId;

    public DateRange(Long id, List<Integer> startDate, List<Integer> endDate, double price,
                     Long accommodationId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.accommodationId = accommodationId;
    }

    public DateRange() {
    }

    protected DateRange(Parcel in) {
        id = in.readLong();
        startDate = new ArrayList<Integer>();
        in.readList(startDate, Integer.class.getClassLoader());
        endDate = new ArrayList<Integer>();
        in.readList(endDate, Integer.class.getClassLoader());
        price = in.readDouble();
        accommodationId = in.readLong();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public List<Integer> getStartDate() {
        return startDate;
    }
    public LocalDate getStartDateAsDate() {
        return LocalDate.of(startDate.get(0), startDate.get(1), startDate.get(2));
    }
    public void setStartDate(List<Integer> startDate) {
        this.startDate = startDate;
    }

    public List<Integer> getEndDate() {
        return endDate;
    }
    public LocalDate getEndDateAsDate() {
        return LocalDate.of(endDate.get(0), endDate.get(1), endDate.get(2));
    }
    public void setEndDate(List<Integer> endDate) {
        this.endDate = endDate;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }
    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeList(startDate);
        dest.writeList(endDate);
        dest.writeDouble(price);
        dest.writeLong(accommodationId);
    }

    public static final Creator<DateRange> CREATOR = new Creator<DateRange>() {
        @Override
        public DateRange createFromParcel(Parcel in) {
            return new DateRange(in);
        }

        @Override
        public DateRange[] newArray(int size) {
            return new DateRange[size];
        }
    };
}

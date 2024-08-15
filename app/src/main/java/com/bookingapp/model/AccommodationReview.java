package com.bookingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccommodationReview implements Parcelable {
    private Long id;
    private String guestEmail;
    private Long accommodationId;
    private String comment;
    private Rating rating;
    private boolean isApproved;
    private List<Integer> submitDate;

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

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public List<Integer> getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(List<Integer> submitDate) {
        this.submitDate = submitDate;
    }


    public AccommodationReview(){}
    public AccommodationReview(Long id, String guestEmail, Long accommodationId, String comment, Rating rating, boolean isApproved, List<Integer> submitDate ){
        this.id = id;
        this.guestEmail = guestEmail;
        this.accommodationId = accommodationId;
        this.comment = comment;
        this.rating = rating;
        this.isApproved = isApproved;
        this.submitDate = submitDate;
    }

    protected AccommodationReview(Parcel in) {
        id = in.readLong();
        guestEmail = in.readString();
        accommodationId = in.readLong();
        comment = in.readString();
        rating = Rating.valueOf(in.readString());
        isApproved = in.readBoolean();
        submitDate = new ArrayList<Integer>();
        in.readList(submitDate, Integer.class.getClassLoader(), Integer.class);
    }

    public static final Creator<AccommodationReview> CREATOR = new Creator<AccommodationReview>() {
        @Override
        public AccommodationReview createFromParcel(Parcel in) {
            return new AccommodationReview(in);
        }

        @Override
        public AccommodationReview[] newArray(int size) {
            return new AccommodationReview[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(guestEmail);
        dest.writeLong(accommodationId);
        dest.writeString(comment);
        dest.writeString(rating.name());
        dest.writeBoolean(isApproved);
        dest.writeList(submitDate);

    }
}

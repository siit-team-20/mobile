package com.bookingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OwnerReview implements Parcelable {


    private Long id;
    private String ownerEmail;
    private String guestEmail;
    private Rating rating;
    private String comment;
    private boolean isReported;
    private List<Integer> submitDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Integer> getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(List<Integer> submitDate) {
        this.submitDate = submitDate;
    }

    public boolean getIsReported() {
        return isReported;
    }

    public void setIsReported(boolean isReported) {
        this.isReported = isReported;
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

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public OwnerReview(Long id, String ownerEmail, String guestEmail, Rating rating, String comment, boolean isReported, List<Integer> submitDate) {
        this.id = id;
        this.ownerEmail = ownerEmail;
        this.guestEmail = guestEmail;
        this.rating = rating;
        this.comment = comment;
        this.isReported = isReported;
        this.submitDate = submitDate;
    }

    public OwnerReview() {
    }

    protected OwnerReview(Parcel in) {
        id = in.readLong();
        ownerEmail = in.readString();
        guestEmail = in.readString();
        rating = Rating.valueOf(in.readString());
        comment = in.readString();
        isReported = in.readBoolean();
        submitDate = new ArrayList<Integer>();
        in.readList(submitDate, Integer.class.getClassLoader(), Integer.class);

    }

    public static final Creator<OwnerReview> CREATOR = new Creator<OwnerReview>() {
        @Override
        public OwnerReview createFromParcel(Parcel in) {
            return new OwnerReview(in);
        }

        @Override
        public OwnerReview[] newArray(int size) {
            return new OwnerReview[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(ownerEmail);
        dest.writeString(guestEmail);
        dest.writeString(rating.name());
        dest.writeString(comment);
        dest.writeBoolean(isReported);
        dest.writeList(submitDate);

    }
}

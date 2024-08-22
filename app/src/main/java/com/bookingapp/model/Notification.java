package com.bookingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Notification implements Parcelable {
    private Long id;
    private NotificationType type;
    private String userEmail;
    private String otherUserEmail;
    private List<Integer> createdAt;

    public Notification() {

    }

    public Notification(Long id, NotificationType type, String userEmail, String otherUserEmail, List<Integer> createdAt) {
        this.id = id;
        this.type = type;
        this.userEmail = userEmail;
        this.otherUserEmail = otherUserEmail;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getOtherUserEmail() {
        return otherUserEmail;
    }

    public void setOtherUserEmail(String otherUserEmail) {
        this.otherUserEmail = otherUserEmail;
    }

    public List<Integer> getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(List<Integer> createdAt) {
        this.createdAt = createdAt;
    }
    public Calendar getCreatedAtAsCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getCreatedAt().get(0), getCreatedAt().get(1) - 1,  getCreatedAt().get(2),  getCreatedAt().get(3),  getCreatedAt().get(4));
        return calendar;
    }

    protected Notification(Parcel in) {
        id = in.readLong();
        type = NotificationType.valueOf(in.readString());
        userEmail = in.readString();
        otherUserEmail = in.readString();
        createdAt = new ArrayList<Integer>();
        in.readList(createdAt, Integer.class.getClassLoader(), Integer.class);
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
        dest.writeString(type.toString());
        dest.writeString(userEmail);
        dest.writeString(otherUserEmail);
        dest.writeList(createdAt);
    }
}

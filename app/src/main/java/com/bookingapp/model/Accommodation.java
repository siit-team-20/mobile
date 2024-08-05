package com.bookingapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Accommodation implements Parcelable {
    private Long id;
    private String ownerEmail;
    private String name;
    private String description;
    private String location;
    private int minGuests;
    private int maxGuests;
    private AccommodationType accommodationType;
    private List<String> benefits;
    private List<DateRange> availabilityDates;
    private boolean isApproved;
    private boolean isPriceByGuest;
    private boolean isAutomaticAcceptance;
    private int reservationCancellationDeadline;

    public Accommodation(Long id, String ownerEmail, String name, String description, String location,
                         int minGuests, int maxGuests, AccommodationType accommodationType,
                         List<String> benefits, List<DateRange> availabilityDates, boolean isApproved,
                         boolean isPriceByGuest, boolean isAutomaticAcceptance, int reservationCancellationDeadline) {
        this.id = id;
        this.ownerEmail = ownerEmail;
        this.name = name;
        this.description = description;
        this.location = location;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.accommodationType = accommodationType;
        this.benefits = benefits;
        this.availabilityDates = availabilityDates;
        this.isApproved = isApproved;
        this.isPriceByGuest = isPriceByGuest;
        this.isAutomaticAcceptance = isAutomaticAcceptance;
        this.reservationCancellationDeadline = reservationCancellationDeadline;
    }

    public Accommodation() { }

    protected Accommodation(Parcel in) {
        id = in.readLong();
        ownerEmail = in.readString();
        name = in.readString();
        description = in.readString();
        location = in.readString();
        minGuests = in.readInt();
        maxGuests = in.readInt();
        accommodationType = AccommodationType.valueOf(in.readString());
        benefits = new ArrayList<String>();
        in.readList(benefits, String.class.getClassLoader());
        availabilityDates = new ArrayList<DateRange>();
        in.readList(availabilityDates, DateRange.class.getClassLoader());
        isApproved = in.readBoolean();
        isPriceByGuest = in.readBoolean();
        isAutomaticAcceptance = in.readBoolean();
        reservationCancellationDeadline = in.readInt();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }
    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public int getMinGuests() {
        return minGuests;
    }
    public void setMinGuests(int minGuests) {
        this.minGuests = minGuests;
    }

    public int getMaxGuests() {
        return maxGuests;
    }
    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public AccommodationType getAccommodationType() {
        return accommodationType;
    }
    public void setAccommodationType(AccommodationType accommodationType) {
        this.accommodationType = accommodationType;
    }

    public List<String> getBenefits() {
        return benefits;
    }
    public void setBenefits(List<String> benefits) {
        this.benefits = benefits;
    }

    public boolean getIsApproved() {
        return isApproved;
    }
    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public boolean getIsPriceByGuest() {
        return isPriceByGuest;
    }
    public void setIsPriceByGuest(boolean isPriceByGuest) {
        this.isPriceByGuest = isPriceByGuest;
    }

    public boolean getIsAutomaticAcceptance() {
        return isAutomaticAcceptance;
    }
    public void setIsAutomaticAcceptance(boolean isAutomaticAcceptance) {
        this.isAutomaticAcceptance = isAutomaticAcceptance;
    }

    public List<DateRange> getAvailabilityDates() {
        return availabilityDates;
    }
    public void setAvailabilityDates(List<DateRange> availabilityDates) {
        this.availabilityDates = availabilityDates;
    }

    public int getReservationCancellationDeadline() {
        return reservationCancellationDeadline;
    }
    public void setReservationCancellationDeadline(int reservationCancellationDeadline) {
        this.reservationCancellationDeadline = reservationCancellationDeadline;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(ownerEmail);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeInt(minGuests);
        dest.writeInt(maxGuests);
        dest.writeString(accommodationType.name());
        dest.writeList(benefits);
        dest.writeList(availabilityDates);
        dest.writeBoolean(isApproved);
        dest.writeBoolean(isPriceByGuest);
        dest.writeBoolean(isAutomaticAcceptance);
        dest.writeInt(reservationCancellationDeadline);
    }

    public static final Creator<Accommodation> CREATOR = new Creator<Accommodation>() {
        @Override
        public Accommodation createFromParcel(Parcel in) {
            return new Accommodation(in);
        }

        @Override
        public Accommodation[] newArray(int size) {
            return new Accommodation[size];
        }
    };


}

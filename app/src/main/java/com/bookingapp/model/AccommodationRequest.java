package com.bookingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class AccommodationRequest implements Parcelable {
    private Long id;
    private Accommodation oldAccommodation;
    private Accommodation newAccommodation;
    private AccommodationRequestType type;

    public AccommodationRequest(Long id, Accommodation oldAccommodation, Accommodation newAccommodation, AccommodationRequestType type){
        this.id = id;
        this.oldAccommodation = oldAccommodation;
        this.newAccommodation = newAccommodation;
        this.type = type;
    }
    public AccommodationRequest(){}
    protected AccommodationRequest(Parcel in){
        id = in.readLong();
        oldAccommodation = in.readParcelable(Accommodation.class.getClassLoader(), Accommodation.class);
        newAccommodation = in.readParcelable(Accommodation.class.getClassLoader(), Accommodation.class);
        type = AccommodationRequestType.valueOf(in.readString());


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeParcelable(oldAccommodation, 0);
        dest.writeParcelable(newAccommodation, 0);
        dest.writeString(type.name());

    }

    public static final Creator<AccommodationRequest> CREATOR = new Creator<AccommodationRequest>() {
        @Override
        public AccommodationRequest createFromParcel(Parcel in) {
            return new AccommodationRequest(in);
        }

        @Override
        public AccommodationRequest[] newArray(int size) {
            return new AccommodationRequest[size];
        }
    };

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public Accommodation getOldAccommodation(){
        return oldAccommodation;
    }
    public void setOldAccommodation(Accommodation oldAccommodation){
        this.oldAccommodation = oldAccommodation;
    }
    public Accommodation getNewAccommodation(){
        return newAccommodation;
    }
    public void setNewAccommodation(Accommodation newAccommodation){
        this.newAccommodation = newAccommodation;
    }
    public AccommodationRequestType getType(){
        return type;
    }
    public void setType(AccommodationRequestType type){
        this.type = type;
    }

}

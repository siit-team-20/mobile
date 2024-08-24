package com.bookingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Report implements Parcelable {
    private Long id;
    private String reporterEmail;
    private String reportedEmail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public void setReporterEmail(String reporterEmail) {
        this.reporterEmail = reporterEmail;
    }

    public String getReportedEmail() {
        return reportedEmail;
    }

    public void setReportedEmail(String reportedEmail) {
        this.reportedEmail = reportedEmail;
    }
    public Report(){

    }
    public Report(Long id, String reporterEmail, String reportedEmail){
         super();
         this.id = id;
         this.reporterEmail = reporterEmail;
         this.reportedEmail = reportedEmail;
    }
    protected Report(Parcel in){
        id = in.readLong();
        reporterEmail = in.readString();
        reportedEmail = in.readString();
    }
    @Override
    public int describeContents(){return 0;}
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags){
        dest.writeLong(id);
        dest.writeString(reporterEmail);
        dest.writeString(reportedEmail);
    }
    public static final Parcelable.Creator<Report> CREATOR = new Parcelable.Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel in) {
            return new Report(in);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };

}

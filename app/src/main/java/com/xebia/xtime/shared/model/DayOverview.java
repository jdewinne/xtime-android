package com.xebia.xtime.shared.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DayOverview implements Parcelable {

    public static final Creator<DayOverview> CREATOR = new Creator<DayOverview>() {
        @Override
        public DayOverview createFromParcel(Parcel parcel) {
            return new DayOverview(parcel);
        }

        @Override
        public DayOverview[] newArray(int size) {
            return new DayOverview[size];
        }
    };
    private Date mDate;
    private double mTotalHours;
    private List<TimeRegistration> mTimeRegistrations;

    protected DayOverview(Parcel parcel) {
        mDate = new Date(parcel.readLong());
        mTotalHours = parcel.readDouble();
        mTimeRegistrations = new ArrayList<TimeRegistration>();
        parcel.readTypedList(mTimeRegistrations, TimeRegistration.CREATOR);
    }

    public DayOverview(Date date, double hours) {
        mDate = date;
        mTotalHours = hours;
        mTimeRegistrations = new ArrayList<TimeRegistration>();
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public double getTotalHours() {
        return mTotalHours;
    }

    public void setTotalHours(double totalHours) {
        mTotalHours = totalHours;
    }

    public List<TimeRegistration> getTimeRegistrations() {
        return mTimeRegistrations;
    }

    public void setTimeRegistrations(List<TimeRegistration> timeRegistrations) {
        mTimeRegistrations = timeRegistrations;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(mDate.getTime());
        parcel.writeDouble(mTotalHours);
        parcel.writeTypedList(mTimeRegistrations);
    }
}

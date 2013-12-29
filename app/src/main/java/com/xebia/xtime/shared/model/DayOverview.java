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
    private List<TimeSheetEntry> mTimeSheetEntries;

    protected DayOverview(Parcel parcel) {
        mDate = new Date(parcel.readLong());
        mTotalHours = parcel.readDouble();
        mTimeSheetEntries = new ArrayList<TimeSheetEntry>();
        parcel.readTypedList(mTimeSheetEntries, TimeSheetEntry.CREATOR);
    }

    public DayOverview(Date date) {
        mDate = date;
        mTimeSheetEntries = new ArrayList<TimeSheetEntry>();
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

    public List<TimeSheetEntry> getTimeSheetEntries() {
        return mTimeSheetEntries;
    }

    public void setTimeSheetEntries(List<TimeSheetEntry> timeSheetEntries) {
        mTimeSheetEntries = timeSheetEntries;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(mDate.getTime());
        parcel.writeDouble(mTotalHours);
        parcel.writeTypedList(mTimeSheetEntries);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DayOverview) {
            return mDate.equals(((DayOverview) o).getDate()) && mTotalHours == ((DayOverview) o)
                    .getTotalHours() && mTimeSheetEntries.equals(((DayOverview) o)
                    .getTimeSheetEntries());
        }
        return super.equals(o);
    }
}

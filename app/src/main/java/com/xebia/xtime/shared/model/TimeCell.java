package com.xebia.xtime.shared.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Represents a single cell in the time registration form.
 * <p/>
 * Each cell is related to a certain day that the work was performed on,
 * and also contains the number of hours worked.
 */
public class TimeCell implements Parcelable {

    public static final Creator<TimeCell> CREATOR = new Creator<TimeCell>() {

        @Override
        public TimeCell createFromParcel(Parcel parcel) {
            return new TimeCell(parcel);
        }

        @Override
        public TimeCell[] newArray(int size) {
            return new TimeCell[size];
        }
    };
    private boolean mApproved;
    private Date mEntryDate;
    private double mHours;

    /**
     * Constructor.
     *
     * @param entryDate Date of the day that the work was performed
     * @param hours     Amount of hours worked
     * @param approved  <code>true</code> if the time cell is approved and cannot be changed anymore
     */
    public TimeCell(Date entryDate, double hours, boolean approved) {
        mApproved = approved;
        mEntryDate = entryDate;
        mHours = hours;
    }

    protected TimeCell(Parcel parcel) {
        mApproved = parcel.readInt() > 0;
        mEntryDate = new Date(parcel.readLong());
        mHours = parcel.readDouble();
    }

    /**
     * @return Date of the day that the work was performed
     */
    public Date getEntryDate() {
        return mEntryDate;
    }

    public void setEntryDate(Date entryDate) {
        mEntryDate = entryDate;
    }

    /**
     * @return Amount of hours worked
     */
    public double getHours() {
        return mHours;
    }

    public void setHours(double hours) {
        mHours = hours;
    }

    /**
     * @return <code>true</code> if the time cell is approved and cannot be changed anymore
     */
    public boolean isApproved() {
        return mApproved;
    }

    public void setApproved(boolean approved) {
        mApproved = approved;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(mApproved ? 1 : 0);
        parcel.writeLong(mEntryDate.getTime());
        parcel.writeDouble(mHours);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TimeCell) {
            return mApproved == ((TimeCell) o).isApproved() &&
                    mEntryDate.equals(((TimeCell) o).getEntryDate()) &&
                    mHours == ((TimeCell) o).getHours();
        }
        return super.equals(o);
    }
}

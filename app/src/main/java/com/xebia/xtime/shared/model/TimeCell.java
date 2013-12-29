package com.xebia.xtime.shared.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

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
    private boolean mFromAfas;
    private double mHours;
    private boolean mTransferredToAfas;

    public TimeCell(boolean approved, Date entryDate, boolean fromAfas, double hours,
                    boolean transferredToAfas) {
        setApproved(approved);
        setEntryDate(entryDate);
        setFromAfas(fromAfas);
        setHours(hours);
        setTransferredToAfas(transferredToAfas);
    }

    protected TimeCell(Parcel parcel) {
        mApproved = parcel.readInt() > 0;
        mEntryDate = new Date(parcel.readLong());
        mFromAfas = parcel.readInt() > 0;
        mHours = parcel.readDouble();
        mTransferredToAfas = parcel.readInt() > 0;
    }

    public boolean isApproved() {
        return mApproved;
    }

    public void setApproved(boolean approved) {
        mApproved = approved;
    }

    public Date getEntryDate() {
        return mEntryDate;
    }

    public void setEntryDate(Date entryDate) {
        mEntryDate = entryDate;
    }

    public boolean isFromAfas() {
        return mFromAfas;
    }

    public void setFromAfas(boolean fromAfas) {
        mFromAfas = fromAfas;
    }

    public double getHours() {
        return mHours;
    }

    public void setHours(double hours) {
        mHours = hours;
    }

    public boolean isTransferredToAfas() {
        return mTransferredToAfas;
    }

    public void setTransferredToAfas(boolean transferredToAfas) {
        mTransferredToAfas = transferredToAfas;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(mApproved ? 1 : 0);
        parcel.writeLong(mEntryDate.getTime());
        parcel.writeInt(mFromAfas ? 1 : 0);
        parcel.writeDouble(mHours);
        parcel.writeInt(mTransferredToAfas ? 1 : 0);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TimeCell) {
            return mApproved == ((TimeCell) o).isApproved() && mEntryDate.equals(((TimeCell) o)
                    .getEntryDate()) && mFromAfas == ((TimeCell) o).isFromAfas() &&
                    mTransferredToAfas == ((TimeCell) o).isTransferredToAfas() && mHours == (
                    (TimeCell) o).getHours();
        }
        return super.equals(o);
    }
}

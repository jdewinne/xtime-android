package com.xebia.xtime.shared.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    private double mHour;
    private boolean mTransferredToAfas;

    public TimeCell(boolean approved, Date entryDate, boolean fromAfas, double hour,
                    boolean transferredToAfas) {
        setApproved(approved);
        setEntryDate(entryDate);
        setFromAfas(fromAfas);
        setHour(hour);
        setTransferredToAfas(transferredToAfas);
    }

    protected TimeCell(Parcel parcel) {
        mApproved = parcel.readInt() > 0;
        mEntryDate = new Date(parcel.readLong());
        mFromAfas = parcel.readInt() > 0;
        mHour = parcel.readDouble();
        mTransferredToAfas = parcel.readInt() > 0;
    }

    public boolean isApproved() {
        return mApproved;
    }

    public void setApproved(boolean approved) {
        this.mApproved = approved;
    }

    public Date getEntryDate() {
        return mEntryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.mEntryDate = entryDate;
    }

    public boolean isFromAfas() {
        return mFromAfas;
    }

    public void setFromAfas(boolean fromAfas) {
        this.mFromAfas = fromAfas;
    }

    public double getHour() {
        return mHour;
    }

    public void setHour(double hour) {
        this.mHour = hour;
    }

    public boolean isTransferredToAfas() {
        return mTransferredToAfas;
    }

    public void setTransferredToAfas(boolean mTransferredToAfas) {
        this.mTransferredToAfas = mTransferredToAfas;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public JSONObject toJson() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("approved", isApproved());
        map.put("entryDate", getEntryDate().getTime());
        map.put("fromAfas", isFromAfas());
        map.put("hour", Double.toString(getHour()));
        map.put("transferredToAfas", isTransferredToAfas());
        return new JSONObject(map);
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
        parcel.writeDouble(mHour);
        parcel.writeInt(mTransferredToAfas ? 1 : 0);
    }
}

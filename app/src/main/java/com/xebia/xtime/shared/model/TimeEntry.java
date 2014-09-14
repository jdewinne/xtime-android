package com.xebia.xtime.shared.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Represents a single cell in the time registration form.
 * <p>
 * Each cell is related to a certain day that the work was performed on,
 * and also contains the number of hours worked.
 * </p>
 */
public class TimeEntry implements Parcelable {

    public static final Creator<TimeEntry> CREATOR = new Creator<TimeEntry>() {

        @Override
        public TimeEntry createFromParcel(Parcel parcel) {
            return new TimeEntry(parcel);
        }

        @Override
        public TimeEntry[] newArray(int size) {
            return new TimeEntry[size];
        }
    };

    private final Task mTask;
    private final boolean mFromAfas;
    private final Date mEntryDate;
    private final double mHours;

    /**
     * Constructor.
     *
     * @param task      The specific task that was worked on
     * @param entryDate Date of the day that the work was performed
     * @param hours     Amount of hours worked
     * @param fromAfas  <code>true</code> if the entry comes from Afas and cannot be changed anymore
     */
    public TimeEntry(final Task task, final Date entryDate, final double hours,
                     final boolean fromAfas) {
        mTask = task;
        mFromAfas = fromAfas;
        mEntryDate = entryDate;
        mHours = hours;
    }

    protected TimeEntry(final Parcel parcel) {
        mTask = parcel.readParcelable(Task.class.getClassLoader());
        mFromAfas = parcel.readInt() > 0;
        mEntryDate = new Date(parcel.readLong());
        mHours = parcel.readDouble();
    }

    public Task getTask() {
        return mTask;
    }

    /**
     * @return Date of the day that the work was performed
     */
    public Date getEntryDate() {
        return mEntryDate;
    }

    /**
     * @return Amount of hours worked
     */
    public double getHours() {
        return mHours;
    }

    /**
     * @return <code>true</code> if the time cell is approved and cannot be changed anymore
     */
    public boolean isFromAfas() {
        return mFromAfas;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(mTask, flags);
        parcel.writeInt(mFromAfas ? 1 : 0);
        parcel.writeLong(mEntryDate.getTime());
        parcel.writeDouble(mHours);
    }

    @Override public int hashCode() {
        return 11 * mTask.hashCode() * mEntryDate.hashCode() * Double.toHexString(mHours).hashCode()
                * (mFromAfas ? 3 : 7);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TimeEntry) {
            TimeEntry that = (TimeEntry) o;
            return this.getTask().equals(that.getTask())
                    && this.isFromAfas() == that.isFromAfas()
                    && this.getEntryDate().equals(that.getEntryDate())
                    && this.getHours() - that.getHours() < 0.1
                    && this.getHours() - that.getHours() > -0.1;
        }
        return super.equals(o);
    }
}

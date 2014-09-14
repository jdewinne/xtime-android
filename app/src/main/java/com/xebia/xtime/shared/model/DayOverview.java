package com.xebia.xtime.shared.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Represents an overview of the work that has been done for a certain day.
 * <p>
 * Similar to its big brother, {@link XTimeOverview}, each day contains a list of {@link TimeEntry}
 * that the user already registered time for, and a complete list of all {@link Project} that the
 * user can possibly log time on. Additionally, the model contains the total amount of time
 * registered for this day.
 * </p>
 */
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
    private final Date mDate;
    private final List<TimeEntry> mTimeEntries;

    /**
     * @param date Day of this overview
     */
    public DayOverview(final Date date) {
        mDate = date;
        mTimeEntries = new ArrayList<>();
    }

    protected DayOverview(Parcel parcel) {
        mDate = new Date(parcel.readLong());
        mTimeEntries = new ArrayList<>();
        parcel.readTypedList(mTimeEntries, TimeEntry.CREATOR);
    }

    /**
     * @return Day of this overview
     */
    public Date getDate() {
        return mDate;
    }

    /**
     * @return Total time of work for this day
     */
    public double getTotalHours() {
        double total = 0;
        for (TimeEntry timeEntry : mTimeEntries) {
            total += timeEntry.getHours();
        }
        return total;
    }

    /**
     * @return List of time cells that the user already registered
     */
    public List<TimeEntry> getTimeEntries() {
        return mTimeEntries;
    }

    /**
     * @return <code>true</code> if the data for this day is not transferred to Afas yet
     */
    public boolean isEditable() {
        boolean editable = true;
        for (TimeEntry timeEntry : mTimeEntries) {
            editable &= !timeEntry.isFromAfas();
        }
        return editable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(mDate.getTime());
        parcel.writeTypedList(mTimeEntries);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DayOverview) {
            DayOverview that = (DayOverview) o;
            return this.getDate().equals(that.getDate())
                    && Arrays.equals(this.getTimeEntries().toArray(), that.getTimeEntries().toArray())
                    && this.isEditable() == that.isEditable()
                    && this.getTotalHours() - that.getTotalHours() < 0.1
                    && this.getTotalHours() - that.getTotalHours() > -0.1;
        }
        return super.equals(o);
    }

}

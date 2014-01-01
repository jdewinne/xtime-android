package com.xebia.xtime.shared.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents an overview of the work that has been done for a certain day.
 * <p/>
 * Similar to its big brother, {@link WeekOverview}, each day contains a list of {@link
 * TimeSheetEntry} that the user already registered time for, and a complete list of all {@link
 * Project} that the user can possibly log time on. Additionally, the model contains the total
 * amount of time registered for this day.
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
    private Date mDate;
    private double mTotalHours;
    private List<TimeSheetEntry> mTimeSheetEntries;
    private List<Project> mProjects;

    /**
     * @param date     Day of this overview
     * @param projects List of available projects for this day
     */
    public DayOverview(Date date, List<Project> projects) {
        mDate = date;
        mProjects = projects;
        mTimeSheetEntries = new ArrayList<TimeSheetEntry>();
    }

    protected DayOverview(Parcel parcel) {
        mDate = new Date(parcel.readLong());
        mTotalHours = parcel.readDouble();
        mTimeSheetEntries = new ArrayList<TimeSheetEntry>();
        parcel.readTypedList(mTimeSheetEntries, TimeSheetEntry.CREATOR);
        mProjects = new ArrayList<Project>();
        parcel.readTypedList(mProjects, Project.CREATOR);
    }

    /**
     * @return Day of this overview
     */
    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    /**
     * @return List of available projects for this day
     */
    public List<Project> getProjects() {
        return mProjects;
    }

    /**
     * @return Total time of work for this day
     */
    public double getTotalHours() {
        return mTotalHours;
    }

    public void setTotalHours(double totalHours) {
        mTotalHours = totalHours;
    }

    /**
     * @return List of time sheet entries that the user already registered
     */
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
        parcel.writeTypedList(mProjects);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DayOverview) {
            return mDate.equals(((DayOverview) o).getDate()) &&
                    mTotalHours == ((DayOverview) o).getTotalHours() &&
                    mProjects.equals(((DayOverview) o).getProjects()) &&
                    mTimeSheetEntries.equals(((DayOverview) o).getTimeSheetEntries());
        }
        return super.equals(o);
    }
}

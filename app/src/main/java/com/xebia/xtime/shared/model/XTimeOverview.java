package com.xebia.xtime.shared.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents an overview of time registrations.
 * <p/>
 * For each overview there is a list of {@link TimeSheetRow} that the user already registered
 * time in, and a complete list of all {@link Project} that the user can possibly log time on.
 */
public class XTimeOverview implements Parcelable {

    public static final Creator<XTimeOverview> CREATOR = new Creator<XTimeOverview>() {

        @Override
        public XTimeOverview createFromParcel(Parcel parcel) {
            return new XTimeOverview(parcel);
        }

        @Override
        public XTimeOverview[] newArray(int size) {
            return new XTimeOverview[size];
        }
    };
    private List<TimeSheetRow> mTimeSheetRows;
    private List<Project> mProjects;
    private String mUsername;
    private boolean mMonthlyDataApproved;
    private Date mLastTransferred;

    /**
     * Constructor.
     *
     * @param timeSheetRows       List of active time sheet rows.
     * @param projects            List of available projects for this week.
     * @param username            Full user name.
     * @param monthlyDataApproved <code>true</code> if the data for this month is already approved.
     * @param lastTransferred     Date when the data was last sent to Afas (transferred data
     *                            cannot be edited)
     */
    public XTimeOverview(List<TimeSheetRow> timeSheetRows, List<Project> projects,
                         String username, boolean monthlyDataApproved, Date lastTransferred) {
        mProjects = projects;
        mTimeSheetRows = timeSheetRows;
        mUsername = username;
        mMonthlyDataApproved = monthlyDataApproved;
        mLastTransferred = lastTransferred;
    }

    protected XTimeOverview(Parcel parcel) {
        mProjects = new ArrayList<Project>();
        parcel.readTypedList(mProjects, Project.CREATOR);
        mTimeSheetRows = new ArrayList<TimeSheetRow>();
        parcel.readTypedList(mTimeSheetRows, TimeSheetRow.CREATOR);
        mUsername = parcel.readString();
        mMonthlyDataApproved = parcel.readInt() > 0;
        mLastTransferred = new Date(parcel.readLong());
    }

    /**
     * @return List of available projects for this week.
     */
    public List<Project> getProjects() {
        return mProjects;
    }

    public void setProjects(List<Project> projects) {
        mProjects = projects;
    }

    /**
     * @return List of active time sheet rows.
     */
    public List<TimeSheetRow> getTimeSheetRows() {
        return mTimeSheetRows;
    }

    public void setTimeSheetRows(List<TimeSheetRow> timeSheetRows) {
        mTimeSheetRows = timeSheetRows;
    }

    /**
     * @return Full user name.
     */
    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    /**
     * @return Date when the data was last sent to Afas (transferred data cannot be edited)
     */
    public Date getLastTransferred() {
        return mLastTransferred;
    }

    /**
     * @return <code>true</code> if the data for this month is already approved.
     */
    public boolean isMonthlyDataApproved() {
        return mMonthlyDataApproved;
    }

    public void setMonthlyDataApproved(boolean monthlyDataApproved) {
        mMonthlyDataApproved = monthlyDataApproved;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeTypedList(mProjects);
        parcel.writeTypedList(mTimeSheetRows);
        parcel.writeString(mUsername);
        parcel.writeInt(mMonthlyDataApproved ? 1 : 0);
        parcel.writeLong(mLastTransferred.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof XTimeOverview) {
            return mMonthlyDataApproved == ((XTimeOverview) o).isMonthlyDataApproved() &&
                    mLastTransferred.equals(((XTimeOverview) o).getLastTransferred()) &&
                    mProjects.equals(((XTimeOverview) o).getProjects()) &&
                    mTimeSheetRows.equals(((XTimeOverview) o).getTimeSheetRows()) &&
                    mUsername.equals(((XTimeOverview) o).getUsername());
        }
        return super.equals(o);
    }
}

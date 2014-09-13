package com.xebia.xtime.shared.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
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

    private final List<TimeSheetRow> mTimeSheetRows;
    private final List<Project> mProjects;
    private final String mUsername;
    private final boolean mMonthlyDataApproved;
    private final Date mLastTransferred;

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
    public XTimeOverview(List<TimeSheetRow> timeSheetRows, List<Project> projects, String username,
                         boolean monthlyDataApproved, Date lastTransferred) {
        mProjects = projects;
        mTimeSheetRows = timeSheetRows;
        mUsername = username;
        mMonthlyDataApproved = monthlyDataApproved;
        mLastTransferred = lastTransferred;
    }

    protected XTimeOverview(Parcel parcel) {
        mProjects = new ArrayList<>();
        parcel.readTypedList(mProjects, Project.CREATOR);
        mTimeSheetRows = new ArrayList<>();
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

    /**
     * @return List of active time sheet rows.
     */
    public List<TimeSheetRow> getTimeSheetRows() {
        return mTimeSheetRows;
    }

    /**
     * @return Full user name.
     */
    public String getUsername() {
        return mUsername;
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
            return mMonthlyDataApproved == ((XTimeOverview) o).isMonthlyDataApproved()
                    && mUsername.equals(((XTimeOverview) o).getUsername())
                    && mLastTransferred.equals(((XTimeOverview) o).getLastTransferred())
                    && Arrays.equals(mProjects.toArray(),
                    ((XTimeOverview) o).getProjects().toArray())
                    && Arrays.equals(mTimeSheetRows.toArray(),
                    ((XTimeOverview) o).getTimeSheetRows().toArray());
        }
        return super.equals(o);
    }

    public static class Builder {

        private final List<TimeSheetRow> mTimeSheetRows = new ArrayList<>();
        private final List<Project> mProjects = new ArrayList<>();
        private String mUsername;
        private boolean mMonthlyDataApproved;
        private Date mLastTransferred;

        public XTimeOverview build() {
            return new XTimeOverview(mTimeSheetRows, mProjects, mUsername, mMonthlyDataApproved,
                    mLastTransferred);
        }

        public Builder addProject(final Project project) {
            mProjects.add(project);
            return this;
        }

        public Builder addTimeSheetRow(final TimeSheetRow sheetRow) {
            mTimeSheetRows.add(sheetRow);
            return this;
        }

        public Builder setMonthlyDataApproved(final boolean approved) {
            mMonthlyDataApproved = approved;
            return this;
        }

        public Builder setLastTransferred(final Date lastTransferred) {
            mLastTransferred = lastTransferred;
            return this;
        }

        public Builder setUsername(final String username) {
            mUsername = username;
            return this;
        }
    }
}

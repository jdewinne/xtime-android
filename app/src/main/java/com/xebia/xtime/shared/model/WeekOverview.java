package com.xebia.xtime.shared.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an overview of time registrations for a given week.
 * <p/>
 * For each week there is a list of {@link TimeSheetRow} that the user already registered time in,
 * and a complete list of all {@link Project} that the user can possibly log time on.
 */
public class WeekOverview implements Parcelable {

    public static final Creator<WeekOverview> CREATOR = new Creator<WeekOverview>() {

        @Override
        public WeekOverview createFromParcel(Parcel parcel) {
            return new WeekOverview(parcel);
        }

        @Override
        public WeekOverview[] newArray(int size) {
            return new WeekOverview[size];
        }
    };
    private List<TimeSheetRow> mTimeSheetRows;
    private List<Project> mProjects;
    private String mUsername;
    private boolean mMonthlyDataApproved;

    /**
     * Constructor.
     *
     * @param timeSheetRows       List of active time sheet rows.
     * @param projects            List of available projects for this week.
     * @param username            Full user name.
     * @param monthlyDataApproved <code>true</code> if the data for this month is already approved.
     */
    public WeekOverview(List<TimeSheetRow> timeSheetRows, List<Project> projects,
                        String username,
                        boolean monthlyDataApproved) {
        mProjects = projects;
        mTimeSheetRows = timeSheetRows;
        mUsername = username;
        mMonthlyDataApproved = monthlyDataApproved;
    }

    protected WeekOverview(Parcel parcel) {
        mProjects = new ArrayList<Project>();
        parcel.readTypedList(mProjects, Project.CREATOR);
        mTimeSheetRows = new ArrayList<TimeSheetRow>();
        parcel.readTypedList(mTimeSheetRows, TimeSheetRow.CREATOR);
        mUsername = parcel.readString();
        mMonthlyDataApproved = parcel.readInt() > 0;
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
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof WeekOverview) {
            return mMonthlyDataApproved == ((WeekOverview) o).isMonthlyDataApproved() &&
                    mProjects.equals(((WeekOverview) o).getProjects()) &&
                    mTimeSheetRows.equals(((WeekOverview) o).getTimeSheetRows()) &&
                    mUsername.equals(((WeekOverview) o).getUsername());
        }
        return super.equals(o);
    }
}

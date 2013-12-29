package com.xebia.xtime.shared.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class TimeSheetRow implements Parcelable {

    public static final Creator<TimeSheetRow> CREATOR = new Creator<TimeSheetRow>() {
        @Override
        public TimeSheetRow createFromParcel(Parcel parcel) {
            return new TimeSheetRow(parcel);
        }

        @Override
        public TimeSheetRow[] newArray(int size) {
            return new TimeSheetRow[size];
        }
    };
    private String mClientName;
    private String mDescription;
    private Project mProject;
    private List<TimeCell> mTimeCells;
    private String mUserId;
    private WorkType mWorkType;

    public TimeSheetRow(String clientName, String description, Project project,
                        List<TimeCell> timeCells, String userId, WorkType workType) {
        setClientName(clientName);
        setDescription(description);
        setProject(project);
        setTimeCells(timeCells);
        setUserId(userId);
        setWorkType(workType);
    }

    protected TimeSheetRow(Parcel parcel) {
        mClientName = parcel.readString();
        mDescription = parcel.readString();
        mProject = parcel.readParcelable(getClass().getClassLoader());
        mTimeCells = new ArrayList<TimeCell>();
        parcel.readTypedList(mTimeCells, TimeCell.CREATOR);
        mUserId = parcel.readString();
        mWorkType = parcel.readParcelable(getClass().getClassLoader());
    }

    public String getClientName() {
        return mClientName;
    }

    public void setClientName(String clientName) {
        mClientName = clientName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Project getProject() {
        return mProject;
    }

    public void setProject(Project project) {
        mProject = project;
    }

    public List<TimeCell> getTimeCells() {
        return mTimeCells;
    }

    public void setTimeCells(List<TimeCell> timeCells) {
        mTimeCells = timeCells;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public WorkType getWorkType() {
        return mWorkType;
    }

    public void setWorkType(WorkType workType) {
        mWorkType = workType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(getClientName());
        parcel.writeString(getDescription());
        parcel.writeParcelable(getProject(), flags);
        parcel.writeTypedList(getTimeCells());
        parcel.writeString(getUserId());
        parcel.writeParcelable(getWorkType(), flags);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TimeSheetRow) {
            return mClientName.equals(((TimeSheetRow) o).getClientName()) && mDescription.equals(
                    ((TimeSheetRow) o).mDescription) && mProject.equals(((TimeSheetRow) o)
                    .getProject()) && mTimeCells.equals(((TimeSheetRow) o).getTimeCells()) &&
                    mWorkType.equals(((TimeSheetRow) o).getWorkType()) && mUserId.equals((
                    (TimeSheetRow) o).getUserId());

        }
        return super.equals(o);
    }
}

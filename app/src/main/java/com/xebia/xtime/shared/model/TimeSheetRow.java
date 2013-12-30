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
    private String mDescription;
    private Project mProject;
    private List<TimeCell> mTimeCells;
    private WorkType mWorkType;

    public TimeSheetRow(Project project, WorkType workType, String description,
                        List<TimeCell> timeCells) {
        setProject(project);
        setWorkType(workType);
        setDescription(description);
        setTimeCells(timeCells);
    }

    protected TimeSheetRow(Parcel parcel) {
        mProject = parcel.readParcelable(Project.class.getClassLoader());
        mWorkType = parcel.readParcelable(WorkType.class.getClassLoader());
        mDescription = parcel.readString();
        mTimeCells = new ArrayList<TimeCell>();
        parcel.readTypedList(mTimeCells, TimeCell.CREATOR);
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
        parcel.writeParcelable(getProject(), flags);
        parcel.writeParcelable(getWorkType(), flags);
        parcel.writeString(getDescription());
        parcel.writeTypedList(getTimeCells());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TimeSheetRow) {
            return mDescription.equals(((TimeSheetRow) o).mDescription) && mProject.equals((
                    (TimeSheetRow) o).getProject()) && mTimeCells.equals(((TimeSheetRow) o)
                    .getTimeCells()) && mWorkType.equals(((TimeSheetRow) o).getWorkType());

        }
        return super.equals(o);
    }
}

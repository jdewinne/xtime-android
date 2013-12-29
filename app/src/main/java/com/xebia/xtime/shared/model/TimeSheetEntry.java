package com.xebia.xtime.shared.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TimeSheetEntry implements Parcelable {

    public static final Creator<TimeSheetEntry> CREATOR = new Creator<TimeSheetEntry>() {
        @Override
        public TimeSheetEntry createFromParcel(Parcel parcel) {
            return new TimeSheetEntry(parcel);
        }

        @Override
        public TimeSheetEntry[] newArray(int size) {
            return new TimeSheetEntry[size];
        }
    };
    private Project mProject;
    private WorkType mWorkType;
    private TimeCell mTimeCell;

    protected TimeSheetEntry(Parcel parcel) {
        mProject = parcel.readParcelable(Project.class.getClassLoader());
        mWorkType = parcel.readParcelable(WorkType.class.getClassLoader());
        mTimeCell = parcel.readParcelable(TimeCell.class.getClassLoader());
    }

    public TimeSheetEntry(Project project, WorkType workType, TimeCell timeCell) {
        mProject = project;
        mWorkType = workType;
        mTimeCell = timeCell;
    }

    public WorkType getWorkType() {
        return mWorkType;
    }

    public TimeCell getTimeCell() {
        return mTimeCell;
    }

    public Project getProject() {
        return mProject;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(mProject, flags);
        parcel.writeParcelable(mWorkType, flags);
        parcel.writeParcelable(mTimeCell, flags);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TimeSheetEntry) {
            return mProject.equals(((TimeSheetEntry) o).getProject()) && mWorkType.equals((
                    (TimeSheetEntry) o).getWorkType()) && mTimeCell.equals(((TimeSheetEntry) o)
                    .getTimeCell());
        }
        return super.equals(o);
    }
}

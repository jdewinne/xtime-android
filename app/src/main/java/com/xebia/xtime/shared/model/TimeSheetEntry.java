package com.xebia.xtime.shared.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a single work time registration entry.
 * <p/>
 * This class is very similar to a {@link TimeSheetRow}, but only contains a single {@link
 * TimeCell}. Just like in a full time sheet row, the work time is related to a specific
 * combination of a certain {@link Project}, a certain {@link WorkType},
 * and sometimes a short description text.
 */
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
    private String mDescription;
    private Project mProject;
    private WorkType mWorkType;
    private TimeCell mTimeCell;

    /**
     * @param project     The project
     * @param workType    The work type
     * @param description Optional free form description of the work
     * @param timeCell    Time cell that contains the amount of time that is registered for this
     *                    project/work type
     */
    public TimeSheetEntry(Project project, WorkType workType, String description,
                          TimeCell timeCell) {
        mProject = project;
        mWorkType = workType;
        mDescription = description;
        mTimeCell = timeCell;
    }

    protected TimeSheetEntry(Parcel parcel) {
        mProject = parcel.readParcelable(Project.class.getClassLoader());
        mWorkType = parcel.readParcelable(WorkType.class.getClassLoader());
        mDescription = parcel.readString();
        mTimeCell = parcel.readParcelable(TimeCell.class.getClassLoader());
    }

    /**
     * @return Optional free form description of the work
     */
    public String getDescription() {
        return mDescription;
    }

    public Project getProject() {
        return mProject;
    }

    /**
     * @return Time cell that contains the amount of time that is registered for this
     * project/work type
     */
    public TimeCell getTimeCell() {
        return mTimeCell;
    }

    public WorkType getWorkType() {
        return mWorkType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(mProject, flags);
        parcel.writeParcelable(mWorkType, flags);
        parcel.writeString(mDescription);
        parcel.writeParcelable(mTimeCell, flags);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TimeSheetEntry) {
            return mProject.equals(((TimeSheetEntry) o).getProject()) &&
                    mWorkType.equals(((TimeSheetEntry) o).getWorkType()) &&
                    mTimeCell.equals(((TimeSheetEntry) o).getTimeCell()) &&
                    mDescription.equals(((TimeSheetEntry) o).getDescription());
        }
        return super.equals(o);
    }
}

package com.xebia.xtime.shared.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a row of days with registered work time in the {@link XTimeOverview} form.
 * <p/>
 * The days are represented by a list of {@link TimeCell}. Each row is also related to one specific
 * combination of a certain {@link Project}, a certain {@link WorkType},
 * and sometimes a short description text.
 */
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
    private final String mDescription;
    private final Project mProject;
    private final List<TimeCell> mTimeCells;
    private final WorkType mWorkType;

    /**
     * Constructor.
     *
     * @param project     The project to register time for
     * @param workType    The type of work that was performed
     * @param description Optional free form description of the work
     * @param timeCells   List of time cells that contain the amount of time that is registered
     *                    for this project/work type
     */
    public TimeSheetRow(Project project, WorkType workType, String description,
                        List<TimeCell> timeCells) {
        mProject = project;
        mWorkType = workType;
        mDescription = description;
        mTimeCells = timeCells;
    }

    protected TimeSheetRow(Parcel parcel) {
        mProject = parcel.readParcelable(Project.class.getClassLoader());
        mWorkType = parcel.readParcelable(WorkType.class.getClassLoader());
        mDescription = parcel.readString();
        mTimeCells = new ArrayList<>();
        parcel.readTypedList(mTimeCells, TimeCell.CREATOR);
    }

    /**
     * @return Optional free form description of the work
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * @return The project to register time for
     */
    public Project getProject() {
        return mProject;
    }

    /**
     * @return List of time cells that contain the amount of time that is registered for this
     * project/work type
     */
    public List<TimeCell> getTimeCells() {
        return mTimeCells;
    }

    /**
     * @return The type of work that was performed
     */
    public WorkType getWorkType() {
        return mWorkType;
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
            return mDescription.equals(((TimeSheetRow) o).mDescription) &&
                    mProject.equals(((TimeSheetRow) o).getProject()) &&
                    mTimeCells.equals(((TimeSheetRow) o).getTimeCells()) &&
                    mWorkType.equals(((TimeSheetRow) o).getWorkType());

        }
        return super.equals(o);
    }

    public static class Builder {

        private final List<TimeCell> mTimeCells = new ArrayList<>();
        private String mDescription;
        private Project mProject;
        private WorkType mWorkType;

        public TimeSheetRow build() {
            return new TimeSheetRow(mProject, mWorkType, mDescription, mTimeCells);
        }

        public Builder addTimeCell(final TimeCell timeCell) {
            mTimeCells.add(timeCell);
            return this;
        }

        public Builder setDescription(final String description) {
            mDescription = description;
            return this;
        }

        public Builder setProject(final Project project) {
            mProject = project;
            return this;
        }

        public Builder setWorkType(final WorkType workType) {
            mWorkType = workType;
            return this;
        }
    }
}

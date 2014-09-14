package com.xebia.xtime.shared.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a row of days with registered work time in the {@link XTimeOverview} form.
 * <p>
 * The days are represented by a list of {@link TimeEntry}. Each row is also related to one specific
 * combination of a certain {@link Project}, a certain {@link WorkType},
 * and sometimes a short description text.
 * </p>
 */
public class Task implements Parcelable {

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel parcel) {
            return new Task(parcel);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
    private final String mDescription;
    private final Project mProject;
    private final WorkType mWorkType;

    /**
     * Constructor.
     *
     * @param project     The project to register time for
     * @param workType    The type of work that was performed
     * @param description Optional free form description of the work
     */
    public Task(Project project, WorkType workType, String description) {
        mProject = project;
        mWorkType = workType;
        mDescription = description;
    }

    protected Task(Parcel parcel) {
        mProject = parcel.readParcelable(Project.class.getClassLoader());
        mWorkType = parcel.readParcelable(WorkType.class.getClassLoader());
        mDescription = parcel.readString();
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
    }

    @Override
    public int hashCode() {
        return 23 * getDescription().hashCode() * mProject.hashCode() * mWorkType.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Task) {
            Task that = (Task) o;
            return this.getDescription().equals(that.getDescription())
                    && this.getProject().equals(that.getProject())
                    && this.getWorkType().equals(that.getWorkType());
        }
        return super.equals(o);
    }

    public static class Builder {

        private String mDescription;
        private Project mProject;
        private WorkType mWorkType;

        public Task build() {
            return new Task(mProject, mWorkType, mDescription);
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

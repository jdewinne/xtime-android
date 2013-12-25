package com.xebia.xtime.shared.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TimeRegistration implements Parcelable {

    public static final Creator<TimeRegistration> CREATOR = new Creator<TimeRegistration>() {
        @Override
        public TimeRegistration createFromParcel(Parcel parcel) {
            return new TimeRegistration(parcel);
        }

        @Override
        public TimeRegistration[] newArray(int size) {
            return new TimeRegistration[size];
        }
    };

    private double mHours;
    private Project mProject;

    protected TimeRegistration(Parcel parcel) {
        mProject = parcel.readParcelable(getClass().getClassLoader());
        mHours = parcel.readDouble();
    }

    public TimeRegistration(Project project, double hours) {
        mProject = project;
        mHours = hours;
    }

    public double getHours() {
        return mHours;
    }

    public void setHours(double hours) {
        this.mHours = hours;
    }

    public Project getProject() {
        return mProject;
    }

    public void setProject(Project project) {
        this.mProject = project;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(mProject, flags);
        parcel.writeDouble(mHours);
    }
}

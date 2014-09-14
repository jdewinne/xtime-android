package com.xebia.xtime.shared.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a project. Each project is identified by its ID and its name.
 * <p/>
 * Note: projects with identical IDs can sometimes have slightly different names,
 * e.g. 'Internal Project' versus 'Internal projects'. This issue comes from the in the XTime
 * backend.
 */
public class Project implements Parcelable {

    public static final Creator<Project> CREATOR = new Creator<Project>() {

        @Override
        public Project createFromParcel(Parcel parcel) {
            return new Project(parcel);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };
    private final String mId;
    private final String mName;

    public Project(final String id, final String name) {
        mId = id;
        mName = name;
    }

    protected Project(Parcel parcel) {
        mId = parcel.readString();
        mName = parcel.readString();
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mId);
        parcel.writeString(mName);
    }

    @Override
    public int hashCode() {
        return 17 * mId.hashCode() * mName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Project) {
            Project that = (Project) o;
            return this.getId().equals(that.getId()) && this.getName().equals(that.getName());
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        // just returning the project name makes it possible to create a list of projects without
        // having to build a special adapter
        return mName;
    }
}

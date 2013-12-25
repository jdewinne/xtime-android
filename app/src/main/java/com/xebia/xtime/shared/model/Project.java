package com.xebia.xtime.shared.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
    private String mId;
    private String mDescription;

    public Project(String id, String description) {
        setId(id);
        setDescription(description);
    }

    protected Project(Parcel parcel) {
        mId = parcel.readString();
        mDescription = parcel.readString();
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public JSONObject toJson() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", getId());
        map.put("description", getDescription());
        return new JSONObject(map);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mId);
        parcel.writeString(mDescription);
    }
}

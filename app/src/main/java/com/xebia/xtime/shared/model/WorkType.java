package com.xebia.xtime.shared.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WorkType implements Parcelable {

    public static final Creator<WorkType> CREATOR = new Creator<WorkType>() {
        @Override
        public WorkType createFromParcel(Parcel parcel) {
            return new WorkType(parcel);
        }

        @Override
        public WorkType[] newArray(int size) {
            return new WorkType[size];
        }
    };
    private String mId;
    private String mDescription;

    protected WorkType(Parcel parcel) {
        mId = parcel.readString();
        mDescription = parcel.readString();
    }

    public WorkType(String id, String description) {
        mId = id;
        mDescription = description;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
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

    public JSONObject toJson() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", mId);
        map.put("description", mDescription);
        return new JSONObject(map);
    }
}

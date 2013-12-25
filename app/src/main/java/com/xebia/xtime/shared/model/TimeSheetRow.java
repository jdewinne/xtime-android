package com.xebia.xtime.shared.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String mProjectId;
    private String mProjectName;
    private List<TimeCell> mTimeCells;
    private String mUserId;
    private String mWorkTypeDescription;
    private String mWorkTypeId;

    public TimeSheetRow(String clientName, String description, String projectId,
                        String projectName, List<TimeCell> timeCells, String userId,
                        String workTypeDescription, String workTypeId) {
        setClientName(clientName);
        setDescription(description);
        setProjectId(projectId);
        setProjectName(projectName);
        setTimeCells(timeCells);
        setUserId(userId);
        setWorkTypeDescription(workTypeDescription);
        setWorkTypeId(workTypeId);
    }

    protected TimeSheetRow(Parcel parcel) {
        mClientName = parcel.readString();
        mDescription = parcel.readString();
        mProjectId = parcel.readString();
        mProjectName = parcel.readString();
        mTimeCells = new ArrayList<TimeCell>();
        parcel.readTypedList(mTimeCells, TimeCell.CREATOR);
        mUserId = parcel.readString();
        mWorkTypeDescription = parcel.readString();
        mWorkTypeId = parcel.readString();
    }

    public String getClientName() {
        return mClientName;
    }

    public void setClientName(String clientName) {
        this.mClientName = clientName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getProjectId() {
        return mProjectId;
    }

    public void setProjectId(String projectId) {
        this.mProjectId = projectId;
    }

    public String getProjectName() {
        return mProjectName;
    }

    public void setProjectName(String projectName) {
        this.mProjectName = projectName;
    }

    public List<TimeCell> getTimeCells() {
        return mTimeCells;
    }

    public void setTimeCells(List<TimeCell> timeCells) {
        this.mTimeCells = timeCells;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getWorkTypeDescription() {
        return mWorkTypeDescription;
    }

    public void setWorkTypeDescription(String workTypeDescription) {
        this.mWorkTypeDescription = workTypeDescription;
    }

    public String getWorkTypeId() {
        return mWorkTypeId;
    }

    public void setWorkTypeId(String workTypeId) {
        this.mWorkTypeId = workTypeId;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public JSONObject toJson() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("clientName", getClientName());
        map.put("description", getDescription());
        map.put("projectId", getProjectId());
        map.put("projectName", getProjectName());
        map.put("userId", getUserId());
        map.put("workTypeDescription", getWorkTypeDescription());
        map.put("workTypeId", getWorkTypeId());

        JSONArray timeCells = new JSONArray();
        for (TimeCell t : getTimeCells()) {
            timeCells.put(t.toJson());
        }
        map.put("timeCells", timeCells);

        return new JSONObject(map);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(getClientName());
        parcel.writeString(getDescription());
        parcel.writeString(getProjectId());
        parcel.writeString(getProjectName());
        parcel.writeTypedList(getTimeCells());
        parcel.writeString(getUserId());
        parcel.writeString(getWorkTypeDescription());
        parcel.writeString(getWorkTypeId());
    }
}

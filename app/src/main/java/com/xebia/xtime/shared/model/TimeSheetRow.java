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
    private Project mProject;
    private List<TimeCell> mTimeCells;
    private String mUserId;
    private WorkType mWorkType;

    public TimeSheetRow(String clientName, String description, Project project,
                        List<TimeCell> timeCells, String userId, WorkType workType) {
        setClientName(clientName);
        setDescription(description);
        setProject(project);
        setTimeCells(timeCells);
        setUserId(userId);
        setWorkType(workType);
    }

    protected TimeSheetRow(Parcel parcel) {
        mClientName = parcel.readString();
        mDescription = parcel.readString();
        mProject = parcel.readParcelable(getClass().getClassLoader());
        mTimeCells = new ArrayList<TimeCell>();
        parcel.readTypedList(mTimeCells, TimeCell.CREATOR);
        mUserId = parcel.readString();
        mWorkType = parcel.readParcelable(getClass().getClassLoader());
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

    public Project getProject() {
        return mProject;
    }

    public void setProject(Project project) {
        this.mProject = project;
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

    public WorkType getWorkType() {
        return mWorkType;
    }

    public void setWorkType(WorkType workType) {
        mWorkType = workType;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public JSONObject toJson() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("clientName", getClientName());
        map.put("description", getDescription());
        map.put("project", getProject().toJson());
        map.put("userId", getUserId());
        map.put("workType", getWorkType().toJson());

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
        parcel.writeParcelable(getProject(), flags);
        parcel.writeTypedList(getTimeCells());
        parcel.writeString(getUserId());
        parcel.writeParcelable(getWorkType(), flags);
    }
}

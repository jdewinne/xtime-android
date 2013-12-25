package com.xebia.xtime.weekoverview.model;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Project {

    private String mId;
    private String mDescription;

    public Project(String id, String description) {
        setId(id);
        setDescription(description);
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
}

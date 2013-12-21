package com.xebia.xtime.weekoverview.model;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimeCell {

    private boolean mApproved;
    private Date mEntryDate;
    private boolean mFromAfas;
    private double mHour;
    private boolean mTransferredToAfas;

    public TimeCell(boolean approved, Date entryDate, boolean fromAfas, double hour,
                    boolean transferredToAfas) {
        setApproved(approved);
        setEntryDate(entryDate);
        setFromAfas(fromAfas);
        setHour(hour);
        setTransferredToAfas(transferredToAfas);
    }

    public boolean isApproved() {
        return mApproved;
    }

    public void setApproved(boolean approved) {
        this.mApproved = approved;
    }

    public Date getEntryDate() {
        return mEntryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.mEntryDate = entryDate;
    }

    public boolean isFromAfas() {
        return mFromAfas;
    }

    public void setFromAfas(boolean fromAfas) {
        this.mFromAfas = fromAfas;
    }

    public double getHour() {
        return mHour;
    }

    public void setHour(double hour) {
        this.mHour = hour;
    }

    public boolean isTransferredToAfas() {
        return mTransferredToAfas;
    }

    public void setTransferredToAfas(boolean mTransferredToAfas) {
        this.mTransferredToAfas = mTransferredToAfas;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public JSONObject toJson() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("approved", isApproved());
        map.put("entryDate", getEntryDate().getTime());
        map.put("fromAfas", isFromAfas());
        map.put("hour", Double.toString(getHour()));
        map.put("transferredToAfas", isTransferredToAfas());
        return new JSONObject(map);
    }
}

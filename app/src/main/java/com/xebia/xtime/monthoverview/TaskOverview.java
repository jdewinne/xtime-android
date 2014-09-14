package com.xebia.xtime.monthoverview;

import com.xebia.xtime.shared.model.Task;
import com.xebia.xtime.shared.model.TimeEntry;

import java.util.ArrayList;
import java.util.List;

public class TaskOverview {

    private final Task mTask;
    private final List<TimeEntry> mTimeEntries = new ArrayList<>();

    public TaskOverview(final Task task) {
        mTask = task;
    }

    public Task getTask() {
        return mTask;
    }

    public List<TimeEntry> getTimeEntries() {
        return mTimeEntries;
    }

    public double getTotalHours() {
        double total = 0;
        for (TimeEntry timeEntry : mTimeEntries) {
            total += timeEntry.getHours();
        }
        return total;
    }
}

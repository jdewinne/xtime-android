package com.xebia.xtime.test.shared.model;

import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.Task;
import com.xebia.xtime.shared.model.TimeEntry;
import com.xebia.xtime.shared.model.WorkType;
import com.xebia.xtime.shared.model.XTimeOverview;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public interface TestValues {
    String DESCRIPTION = "description";

    String PROJECT_ID = "project id";
    String PROJECT_NAME = "project name";
    Project PROJECT = new Project(PROJECT_ID, PROJECT_NAME);
    List<Project> PROJECTS = Arrays.asList(PROJECT, new Project("other id", "other name"));

    String WORK_TYPE_ID = "work type id";
    String WORK_TYPE_DESCRIPTION = "work type description";
    WorkType WORK_TYPE = new WorkType(WORK_TYPE_ID, WORK_TYPE_DESCRIPTION);

    Task TASK = new Task(PROJECT, WORK_TYPE, DESCRIPTION);
    Date ENTRY_DATE = new Date(1234);
    double HOURS = 3.14;
    boolean APPROVED = true;
    TimeEntry TIME_CELL = new TimeEntry(TASK, ENTRY_DATE, HOURS, APPROVED);
    List<TimeEntry> TIME_ENTRIES = Arrays.asList(TIME_CELL, new TimeEntry(TASK, new Date(665), 7,
            true));

    String USERNAME = "username";
    Date LAST_TRANSFERRED = new Date(4242);
    XTimeOverview OVERVIEW = new XTimeOverview(TIME_ENTRIES, PROJECTS, USERNAME,
            TestValues.APPROVED, LAST_TRANSFERRED);
}

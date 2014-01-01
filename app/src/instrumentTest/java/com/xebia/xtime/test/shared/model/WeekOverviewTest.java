package com.xebia.xtime.test.shared.model;

import android.os.Parcel;

import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.TimeCell;
import com.xebia.xtime.shared.model.TimeSheetRow;
import com.xebia.xtime.shared.model.WeekOverview;
import com.xebia.xtime.shared.model.WorkType;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeekOverviewTest extends TestCase {

    private WeekOverview mOverview;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Project project = new Project("project id", "project name");
        TimeSheetRow row = new TimeSheetRow(project, new WorkType("id", "descr"), "descr",
                new ArrayList<TimeCell>());
        List<TimeSheetRow> timeSheetRows = Arrays.asList(row);
        List<Project> projects = Arrays.asList(project);
        String username = "username";
        final boolean approved = true;
        mOverview = new WeekOverview(timeSheetRows, projects, username, approved);
    }

    public void testEquals() {
        Project project = new Project("project id", "project name");
        TimeSheetRow row = new TimeSheetRow(project, new WorkType("id", "descr"), "descr",
                new ArrayList<TimeCell>());
        List<TimeSheetRow> timeSheetRows = Arrays.asList(row);
        List<Project> projects = Arrays.asList(project);
        String username = "username";
        final boolean approved = true;

        assertTrue(mOverview.equals(new WeekOverview(timeSheetRows, projects, username, approved)));
        assertFalse(mOverview.equals(new WeekOverview(new ArrayList<TimeSheetRow>(), projects,
                username, approved)));
        assertFalse(mOverview.equals(new WeekOverview(timeSheetRows, new ArrayList<Project>(),
                username, approved)));
        assertFalse(mOverview.equals(new WeekOverview(timeSheetRows, projects, "wrong", approved)));
        assertFalse(mOverview.equals(new WeekOverview(timeSheetRows, projects, username, false)));
    }

    public void testParcelable() {
        Parcel in = Parcel.obtain();
        Parcel out = Parcel.obtain();
        WeekOverview result = null;
        try {
            in.writeParcelable(mOverview, 0);
            byte[] bytes = in.marshall();
            out.unmarshall(bytes, 0, bytes.length);
            out.setDataPosition(0);
            result = out.readParcelable(WeekOverview.class.getClassLoader());
        } finally {
            in.recycle();
            out.recycle();
        }

        assertNotNull(result);
        assertEquals(mOverview, result);
    }
}

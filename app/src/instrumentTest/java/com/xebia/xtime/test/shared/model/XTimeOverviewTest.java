package com.xebia.xtime.test.shared.model;

import android.os.Parcel;

import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.TimeCell;
import com.xebia.xtime.shared.model.TimeSheetRow;
import com.xebia.xtime.shared.model.WorkType;
import com.xebia.xtime.shared.model.XTimeOverview;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class XTimeOverviewTest extends TestCase {

    private XTimeOverview mOverview;

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
        Date transferred = new Date(1234);
        mOverview = new XTimeOverview(timeSheetRows, projects, username, approved, transferred);
    }

    public void testEquals() {
        Project project = new Project("project id", "project name");
        TimeSheetRow row = new TimeSheetRow(project, new WorkType("id", "descr"), "descr",
                new ArrayList<TimeCell>());
        List<TimeSheetRow> timeSheetRows = Arrays.asList(row);
        List<Project> projects = Arrays.asList(project);
        String username = "username";
        final boolean approved = true;
        Date transferred = new Date(1234);

        assertTrue(mOverview.equals(new XTimeOverview(timeSheetRows, projects, username, approved,
                transferred)));
        assertFalse(mOverview.equals(new XTimeOverview(new ArrayList<TimeSheetRow>(), projects,
                username, approved, transferred)));
        assertFalse(mOverview.equals(new XTimeOverview(timeSheetRows, new ArrayList<Project>(),
                username, approved, transferred)));
        assertFalse(mOverview.equals(new XTimeOverview(timeSheetRows, projects, "wrong", approved,
                transferred)));
        assertFalse(mOverview.equals(new XTimeOverview(timeSheetRows, projects, username, false,
                transferred)));
        assertFalse(mOverview.equals(new XTimeOverview(timeSheetRows, projects, username,
                approved, new Date(666))));
    }

    public void testParcelable() {
        Parcel in = Parcel.obtain();
        Parcel out = Parcel.obtain();
        XTimeOverview result = null;
        try {
            in.writeParcelable(mOverview, 0);
            byte[] bytes = in.marshall();
            out.unmarshall(bytes, 0, bytes.length);
            out.setDataPosition(0);
            result = out.readParcelable(XTimeOverview.class.getClassLoader());
        } finally {
            in.recycle();
            out.recycle();
        }

        assertNotNull(result);
        assertEquals(mOverview, result);
    }
}

package com.xebia.xtime.test.shared.model;

import android.os.Parcel;

import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.TimeCell;
import com.xebia.xtime.shared.model.TimeSheetRow;
import com.xebia.xtime.shared.model.WorkType;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class TimeSheetRowTest extends TestCase {

    private TimeSheetRow mTimeSheet;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        String clientName = "client name";
        String description = "description";
        Project project = new Project("project id", "project name");
        List<TimeCell> timeCells = new ArrayList<TimeCell>();
        String userId = "user id";
        WorkType workType = new WorkType("work type id", "work type description");
        mTimeSheet = new TimeSheetRow(clientName, description, project, timeCells, userId,
                workType);
    }

    public void testEquals() {
        String clientName = "client name";
        String description = "description";
        Project project = new Project("project id", "project name");
        List<TimeCell> timeCells = new ArrayList<TimeCell>();
        String userId = "user id";
        WorkType workType = new WorkType("work type id", "work type description");

        assertTrue(mTimeSheet.equals(new TimeSheetRow(clientName, description, project,
                timeCells, userId, workType)));
        assertFalse(mTimeSheet.equals(new TimeSheetRow("not client", description, project,
                timeCells, userId, workType)));
        assertFalse(mTimeSheet.equals(new TimeSheetRow(clientName, "not descr", project,
                timeCells, userId, workType)));
        assertFalse(mTimeSheet.equals(new TimeSheetRow(clientName, description, project,
                timeCells, "not user", workType)));
        assertFalse(mTimeSheet.equals(new TimeSheetRow(clientName, description,
                new Project("foo", "bar"), timeCells, userId, workType)));
        assertFalse(mTimeSheet.equals(new TimeSheetRow(clientName, description, project,
                timeCells, userId, new WorkType("foo", "bar"))));
    }

    public void testParcelable() {
        Parcel in = Parcel.obtain();
        Parcel out = Parcel.obtain();
        TimeSheetRow result = null;
        try {
            in.writeParcelable(mTimeSheet, 0);
            byte[] bytes = in.marshall();
            out.unmarshall(bytes, 0, bytes.length);
            out.setDataPosition(0);
            result = out.readParcelable(TimeSheetRow.class.getClassLoader());
        } finally {
            in.recycle();
            out.recycle();
        }

        assertNotNull(result);
        assertEquals(mTimeSheet, result);
    }
}

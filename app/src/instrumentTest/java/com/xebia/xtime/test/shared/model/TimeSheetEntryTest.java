package com.xebia.xtime.test.shared.model;

import android.os.Parcel;

import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.TimeCell;
import com.xebia.xtime.shared.model.TimeSheetEntry;
import com.xebia.xtime.shared.model.WorkType;

import junit.framework.TestCase;

import java.util.Date;

public class TimeSheetEntryTest extends TestCase {

    private TimeSheetEntry mTimeSheet;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Project project = new Project("project id", "project name");
        WorkType workType = new WorkType("work type id", "work type description");
        String description = "description";
        TimeCell timeCell = new TimeCell(true, new Date(1234), true, 1, true);
        mTimeSheet = new TimeSheetEntry(project, workType, description, timeCell);
    }

    public void testEquals() {
        Project project = new Project("project id", "project name");
        WorkType workType = new WorkType("work type id", "work type description");
        String description = "description";
        TimeCell timeCell = new TimeCell(true, new Date(1234), true, 1, true);

        assertTrue(mTimeSheet.equals(new TimeSheetEntry(project, workType, description, timeCell)));
        assertFalse(mTimeSheet.equals(new TimeSheetEntry(new Project("not", "not"), workType,
                description,
                timeCell)));
        assertFalse(mTimeSheet.equals(new TimeSheetEntry(project, new WorkType("not", "not"),
                description, timeCell)));
        assertFalse(mTimeSheet.equals(new TimeSheetEntry(project, workType, "wrong", timeCell)));
        timeCell.setApproved(false);
        assertFalse(mTimeSheet.equals(new TimeSheetEntry(project, workType, description,
                timeCell)));

    }

    public void testParcelable() {
        Parcel in = Parcel.obtain();
        Parcel out = Parcel.obtain();
        TimeSheetEntry result = null;
        try {
            in.writeParcelable(mTimeSheet, 0);
            byte[] bytes = in.marshall();
            out.unmarshall(bytes, 0, bytes.length);
            out.setDataPosition(0);
            result = out.readParcelable(TimeSheetEntry.class.getClassLoader());
        } finally {
            in.recycle();
            out.recycle();
        }

        assertNotNull(result);
        assertEquals(mTimeSheet, result);
    }
}

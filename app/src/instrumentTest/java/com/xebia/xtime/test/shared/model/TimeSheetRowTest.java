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

        String description = "description";
        Project project = new Project("project id", "project name");
        List<TimeCell> timeCells = new ArrayList<TimeCell>();
        WorkType workType = new WorkType("work type id", "work type description");
        mTimeSheet = new TimeSheetRow(project, workType, description, timeCells);
    }

    public void testEquals() {
        String description = "description";
        Project project = new Project("project id", "project name");
        List<TimeCell> timeCells = new ArrayList<TimeCell>();
        WorkType workType = new WorkType("work type id", "work type description");

        assertTrue(mTimeSheet.equals(new TimeSheetRow(project, workType, description, timeCells)));
        assertFalse(mTimeSheet.equals(new TimeSheetRow(new Project("wrong", "wrong"), workType,
                description, timeCells)));
        assertFalse(mTimeSheet.equals(new TimeSheetRow(project, new WorkType("wrong", "wrong"),
                description, timeCells)));
        assertFalse(mTimeSheet.equals(new TimeSheetRow(project, workType, "wrong", timeCells)));
        timeCells.add(new TimeCell(null, 0, false, false, false));
        assertFalse(mTimeSheet.equals(new TimeSheetRow(project, workType, description, timeCells)));
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

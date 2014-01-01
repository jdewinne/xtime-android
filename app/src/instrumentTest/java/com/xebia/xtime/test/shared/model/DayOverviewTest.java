package com.xebia.xtime.test.shared.model;

import android.os.Parcel;

import com.xebia.xtime.shared.model.DayOverview;
import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.TimeSheetEntry;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DayOverviewTest extends TestCase {

    private DayOverview mOverview;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        List<Project> projects = Arrays.asList(new Project("project id", "project name"));
        mOverview = new DayOverview(new Date(1234), projects);
        mOverview.setTotalHours(42);
        mOverview.setTimeSheetEntries(new ArrayList<TimeSheetEntry>());
    }

    public void testEquals() {
        List<Project> projects = Arrays.asList(new Project("project id", "project name"));
        DayOverview shouldEqual = new DayOverview(new Date(1234), projects);
        shouldEqual.setTotalHours(42);
        shouldEqual.setTimeSheetEntries(new ArrayList<TimeSheetEntry>());

        assertTrue(mOverview.equals(shouldEqual));
        shouldEqual.setTotalHours(2);

        assertFalse(mOverview.equals(shouldEqual));
        shouldEqual.setTotalHours(42);
        assertTrue(mOverview.equals(shouldEqual));

        shouldEqual.getTimeSheetEntries().add(new TimeSheetEntry(null, null, null, null));
        assertFalse(mOverview.equals(shouldEqual));
        shouldEqual.getTimeSheetEntries().clear();
        assertTrue(mOverview.equals(shouldEqual));

        DayOverview shouldNotEqual = new DayOverview(new Date(1234), new ArrayList<Project>());
        shouldNotEqual.setTotalHours(42);
        shouldNotEqual.setTimeSheetEntries(new ArrayList<TimeSheetEntry>());
        assertFalse(mOverview.equals(shouldNotEqual));
    }

    public void testParcelable() {
        Parcel in = Parcel.obtain();
        Parcel out = Parcel.obtain();
        DayOverview result = null;
        try {
            in.writeParcelable(mOverview, 0);
            byte[] bytes = in.marshall();
            out.unmarshall(bytes, 0, bytes.length);
            out.setDataPosition(0);
            result = out.readParcelable(DayOverview.class.getClassLoader());
        } finally {
            in.recycle();
            out.recycle();
        }

        assertNotNull(result);
        assertEquals(mOverview, result);
    }
}

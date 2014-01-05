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
        mOverview = new DayOverview(new Date(1234), projects, true);
        mOverview.setTotalHours(42);
        mOverview.setTimeSheetEntries(new ArrayList<TimeSheetEntry>());
    }

    public void testEquals() {
        List<Project> projects = Arrays.asList(new Project("project id", "project name"));
        DayOverview compare = new DayOverview(new Date(1234), projects, true);
        compare.setTotalHours(42);
        compare.setTimeSheetEntries(new ArrayList<TimeSheetEntry>());
        assertTrue(mOverview.equals(compare));

        compare.setTotalHours(13);
        assertFalse(mOverview.equals(compare));
        compare.setTotalHours(42);
        assertTrue(mOverview.equals(compare));

        compare.getTimeSheetEntries().add(new TimeSheetEntry(null, null, null, null));
        assertFalse(mOverview.equals(compare));
        compare.getTimeSheetEntries().clear();
        assertTrue(mOverview.equals(compare));

        compare.setEditable(false);
        assertFalse(mOverview.equals(compare));
        compare.setEditable(true);
        assertTrue(mOverview.equals(compare));

        DayOverview shouldNotEqual = new DayOverview(new Date(1234), new ArrayList<Project>(),
                true);
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

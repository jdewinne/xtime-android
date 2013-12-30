package com.xebia.xtime.test.shared.model;

import android.os.Parcel;

import com.xebia.xtime.shared.model.DayOverview;
import com.xebia.xtime.shared.model.TimeSheetEntry;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;

public class DayOverviewTest extends TestCase {

    private DayOverview mOverview;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mOverview = new DayOverview(new Date(1234));
        mOverview.setTotalHours(42);
        mOverview.setTimeSheetEntries(new ArrayList<TimeSheetEntry>());
    }

    public void testEquals() {
        DayOverview shouldEqual = new DayOverview(new Date(1234));
        shouldEqual.setTotalHours(42);
        shouldEqual.setTimeSheetEntries(new ArrayList<TimeSheetEntry>());

        assertTrue(mOverview.equals(shouldEqual));
        shouldEqual.setTotalHours(2);
        assertFalse(mOverview.equals(shouldEqual));
        shouldEqual.setTotalHours(42);
        shouldEqual.getTimeSheetEntries().add(new TimeSheetEntry(null, null, null, null));
        assertFalse(mOverview.equals(shouldEqual));
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

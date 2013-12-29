package com.xebia.xtime.test.shared.model;

import android.os.Parcel;

import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.TimeSheetRow;
import com.xebia.xtime.shared.model.WeekOverview;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeekOverviewTest extends TestCase {

    private WeekOverview mOverview;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Date lastTransferred = new Date(1234);
        int monthDaysCount = 21;
        final boolean monthlyDataApproved = true;
        final boolean monthlyDataTransferred = false;
        List<Project> projects = new ArrayList<Project>();
        List<TimeSheetRow> timeSheetRows = new ArrayList<TimeSheetRow>();
        String username = "username";
        mOverview = new WeekOverview(lastTransferred, monthDaysCount, monthlyDataApproved,
                monthlyDataTransferred, projects, timeSheetRows, username);
    }

    public void testEquals() {
        Date lastTransferred = new Date(1234);
        int monthDaysCount = 21;
        final boolean monthlyDataApproved = true;
        final boolean monthlyDataTransferred = false;
        List<Project> projects = new ArrayList<Project>();
        List<TimeSheetRow> timeSheetRows = new ArrayList<TimeSheetRow>();
        String username = "username";
        WeekOverview shouldEqual = new WeekOverview(lastTransferred, monthDaysCount,
                monthlyDataApproved,
                monthlyDataTransferred, projects, timeSheetRows, username);

        assertTrue(mOverview.equals(shouldEqual));
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

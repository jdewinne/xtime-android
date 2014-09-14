package com.xebia.xtime.test.shared.model;

import android.os.Parcel;

import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.TimeEntry;
import com.xebia.xtime.shared.model.XTimeOverview;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;

import static com.xebia.xtime.test.shared.model.TestValues.APPROVED;
import static com.xebia.xtime.test.shared.model.TestValues.LAST_TRANSFERRED;
import static com.xebia.xtime.test.shared.model.TestValues.OVERVIEW;
import static com.xebia.xtime.test.shared.model.TestValues.PROJECTS;
import static com.xebia.xtime.test.shared.model.TestValues.TIME_ENTRIES;
import static com.xebia.xtime.test.shared.model.TestValues.USERNAME;

public class XTimeOverviewTest extends TestCase {

    public void testEquals() {
        assertEquals(OVERVIEW, new XTimeOverview(TIME_ENTRIES, PROJECTS, USERNAME, APPROVED,
                LAST_TRANSFERRED));
        assertFalse(OVERVIEW.equals(new XTimeOverview(new ArrayList<TimeEntry>(), PROJECTS,
                USERNAME, APPROVED, LAST_TRANSFERRED)));
        assertFalse(OVERVIEW.equals(new XTimeOverview(TIME_ENTRIES, new ArrayList<Project>(),
                USERNAME, APPROVED, LAST_TRANSFERRED)));
        assertFalse(OVERVIEW.equals(new XTimeOverview(TIME_ENTRIES, PROJECTS, "wrong", APPROVED,
                LAST_TRANSFERRED)));
        assertFalse(OVERVIEW.equals(new XTimeOverview(TIME_ENTRIES, PROJECTS, USERNAME, false,
                LAST_TRANSFERRED)));
        assertFalse(OVERVIEW.equals(new XTimeOverview(TIME_ENTRIES, PROJECTS, USERNAME,
                APPROVED, new Date(666))));
    }

    public void testParcelable() {
        Parcel in = Parcel.obtain();
        Parcel out = Parcel.obtain();
        XTimeOverview result = null;
        try {
            in.writeParcelable(OVERVIEW, 0);
            byte[] bytes = in.marshall();
            out.unmarshall(bytes, 0, bytes.length);
            out.setDataPosition(0);
            result = out.readParcelable(XTimeOverview.class.getClassLoader());
        } finally {
            in.recycle();
            out.recycle();
        }

        assertNotNull(result);
        assertEquals(OVERVIEW, result);
    }
}

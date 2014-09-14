package com.xebia.xtime.test.shared.model;

import android.os.Parcel;

import com.xebia.xtime.shared.model.DayOverview;

import junit.framework.TestCase;

import java.util.Date;

import static com.xebia.xtime.test.shared.model.TestValues.ENTRY_DATE;
import static com.xebia.xtime.test.shared.model.TestValues.TIME_ENTRY;

public class DayOverviewTest extends TestCase {

    private static final DayOverview DAY_OVERVIEW = new DayOverview.Builder().setDate(ENTRY_DATE)
            .addTimeEntry(TIME_ENTRY).build();

    public void testEquals() {
        DayOverview wrongDate = new DayOverview.Builder().setDate(new Date(-1)).build();
        assertFalse(DAY_OVERVIEW.equals(wrongDate));

        DayOverview noTimeCells = new DayOverview.Builder().setDate(ENTRY_DATE).build();
        assertFalse(DAY_OVERVIEW.equals(noTimeCells));

        DayOverview correct = new DayOverview.Builder().setDate(ENTRY_DATE)
                .addTimeEntry(TIME_ENTRY).build();
        assertEquals(DAY_OVERVIEW, correct);
    }

    public void testParcelable() {
        Parcel in = Parcel.obtain();
        Parcel out = Parcel.obtain();
        DayOverview result = null;
        try {
            in.writeParcelable(DAY_OVERVIEW, 0);
            byte[] bytes = in.marshall();
            out.unmarshall(bytes, 0, bytes.length);
            out.setDataPosition(0);
            result = out.readParcelable(DayOverview.class.getClassLoader());
        } finally {
            in.recycle();
            out.recycle();
        }

        assertNotNull(result);
        assertEquals(DAY_OVERVIEW, result);
    }
}

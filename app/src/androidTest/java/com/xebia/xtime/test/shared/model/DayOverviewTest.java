package com.xebia.xtime.test.shared.model;

import android.os.Parcel;

import com.xebia.xtime.shared.model.DayOverview;

import junit.framework.TestCase;

import java.util.Date;

import static com.xebia.xtime.test.shared.model.TestValues.ENTRY_DATE;
import static com.xebia.xtime.test.shared.model.TestValues.TIME_CELL;

public class DayOverviewTest extends TestCase {

    private DayOverview mOverview;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mOverview = new DayOverview(ENTRY_DATE);
        mOverview.getTimeEntries().add(TIME_CELL);
    }

    public void testEquals() {
        DayOverview wrongDate = new DayOverview(new Date(-1));
        assertFalse(mOverview.equals(wrongDate));

        DayOverview noTimeCells = new DayOverview(ENTRY_DATE);
        assertFalse(mOverview.equals(noTimeCells));

        DayOverview correct = new DayOverview(ENTRY_DATE);
        correct.getTimeEntries().add(TIME_CELL);
        assertEquals(mOverview, correct);
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

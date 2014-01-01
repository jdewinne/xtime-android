package com.xebia.xtime.test.shared.model;

import android.os.Parcel;

import com.xebia.xtime.shared.model.TimeCell;

import junit.framework.TestCase;

import java.util.Date;

public class TimeCellTest extends TestCase {

    private TimeCell mTimeCell;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        final boolean approved = true;
        Date entryDate = new Date(1234);
        double hours = 3.14;
        mTimeCell = new TimeCell(entryDate, hours, approved);
    }

    public void testEquals() {
        final boolean approved = true;
        Date entryDate = new Date(1234);
        double hours = 3.14;

        assertTrue(mTimeCell.equals(new TimeCell(entryDate, hours, approved)));
        assertFalse(mTimeCell.equals(new TimeCell(new Date(), hours, approved)));
        assertFalse(mTimeCell.equals(new TimeCell(entryDate, hours + 1, approved)));
        assertFalse(mTimeCell.equals(new TimeCell(entryDate, hours, false)));
    }

    public void testParcelable() {
        Parcel in = Parcel.obtain();
        Parcel out = Parcel.obtain();
        TimeCell result = null;
        try {
            in.writeParcelable(mTimeCell, 0);
            byte[] bytes = in.marshall();
            out.unmarshall(bytes, 0, bytes.length);
            out.setDataPosition(0);
            result = out.readParcelable(TimeCell.class.getClassLoader());
        } finally {
            in.recycle();
            out.recycle();
        }

        assertNotNull(result);
        assertEquals(mTimeCell, result);
    }
}

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
        final boolean fromAfas = true;
        double hours = 3.14;
        final boolean transferredToAfas = true;
        mTimeCell = new TimeCell(entryDate, hours, approved, fromAfas, transferredToAfas);
    }

    public void testEquals() {
        final boolean approved = true;
        Date entryDate = new Date(1234);
        final boolean fromAfas = true;
        double hours = 3.14;
        final boolean transferredToAfas = true;

        assertTrue(mTimeCell.equals(new TimeCell(entryDate, hours, approved, fromAfas,
                transferredToAfas)));
        assertFalse(mTimeCell.equals(new TimeCell(new Date(), hours, approved, fromAfas,
                transferredToAfas)));
        assertFalse(mTimeCell.equals(new TimeCell(entryDate, hours + 1, approved, fromAfas,
                transferredToAfas)));
        assertFalse(mTimeCell.equals(new TimeCell(entryDate, hours, false, fromAfas,
                transferredToAfas)));
        assertFalse(mTimeCell.equals(new TimeCell(entryDate, hours, approved, false,
                transferredToAfas)));
        assertFalse(mTimeCell.equals(new TimeCell(entryDate, hours, approved, fromAfas, false)));
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

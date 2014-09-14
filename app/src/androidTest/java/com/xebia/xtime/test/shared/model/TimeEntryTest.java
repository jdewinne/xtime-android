package com.xebia.xtime.test.shared.model;

import android.os.Parcel;

import com.xebia.xtime.shared.model.Task;
import com.xebia.xtime.shared.model.TimeEntry;

import junit.framework.TestCase;

import java.util.Date;

import static com.xebia.xtime.test.shared.model.TestValues.APPROVED;
import static com.xebia.xtime.test.shared.model.TestValues.ENTRY_DATE;
import static com.xebia.xtime.test.shared.model.TestValues.HOURS;
import static com.xebia.xtime.test.shared.model.TestValues.PROJECT;
import static com.xebia.xtime.test.shared.model.TestValues.TASK;
import static com.xebia.xtime.test.shared.model.TestValues.TIME_CELL;
import static com.xebia.xtime.test.shared.model.TestValues.WORK_TYPE;

public class TimeEntryTest extends TestCase {

    public void testEquals() {
        assertEquals(TIME_CELL, new TimeEntry(TASK, ENTRY_DATE, HOURS, APPROVED));
        Task wrongTask = new Task(PROJECT, WORK_TYPE, "wrong");
        assertFalse(TIME_CELL.equals(new TimeEntry(wrongTask, new Date(), HOURS, APPROVED)));
        assertFalse(TIME_CELL.equals(new TimeEntry(TASK, new Date(), HOURS, APPROVED)));
        assertFalse(TIME_CELL.equals(new TimeEntry(TASK, ENTRY_DATE, HOURS + 1, APPROVED)));
        assertFalse(TIME_CELL.equals(new TimeEntry(TASK, ENTRY_DATE, HOURS, false)));
    }

    public void testParcelable() {
        Parcel in = Parcel.obtain();
        Parcel out = Parcel.obtain();
        TimeEntry result = null;
        try {
            in.writeParcelable(TIME_CELL, 0);
            byte[] bytes = in.marshall();
            out.unmarshall(bytes, 0, bytes.length);
            out.setDataPosition(0);
            result = out.readParcelable(TimeEntry.class.getClassLoader());
        } finally {
            in.recycle();
            out.recycle();
        }

        assertNotNull(result);
        assertEquals(TIME_CELL, result);
    }
}

package com.xebia.xtime.test.shared.model;

import android.os.Parcel;

import com.xebia.xtime.shared.model.WorkType;

import junit.framework.TestCase;

import static com.xebia.xtime.test.shared.model.TestValues.WORK_TYPE;
import static com.xebia.xtime.test.shared.model.TestValues.WORK_TYPE_DESCRIPTION;
import static com.xebia.xtime.test.shared.model.TestValues.WORK_TYPE_ID;

public class WorkTypeTest extends TestCase {

    public void testEquals() {
        assertEquals(WORK_TYPE, new WorkType(WORK_TYPE_ID, WORK_TYPE_DESCRIPTION));
        assertFalse(WORK_TYPE.equals(new WorkType("wrong", WORK_TYPE_DESCRIPTION)));
        assertFalse(WORK_TYPE.equals(new WorkType(WORK_TYPE_ID, "wrong")));
    }

    public void testParcelable() {
        Parcel in = Parcel.obtain();
        Parcel out = Parcel.obtain();
        WorkType result = null;
        try {
            in.writeParcelable(WORK_TYPE, 0);
            byte[] bytes = in.marshall();
            out.unmarshall(bytes, 0, bytes.length);
            out.setDataPosition(0);
            result = out.readParcelable(WorkType.class.getClassLoader());
        } finally {
            in.recycle();
            out.recycle();
        }

        assertNotNull(result);
        assertEquals(WORK_TYPE, result);
    }
}

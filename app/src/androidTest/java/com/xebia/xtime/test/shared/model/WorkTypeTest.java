package com.xebia.xtime.test.shared.model;

import android.os.Parcel;

import com.xebia.xtime.shared.model.WorkType;

import junit.framework.TestCase;

public class WorkTypeTest extends TestCase {

    private WorkType mWorkType;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mWorkType = new WorkType("id", "name");
    }

    public void testEquals() {
        assertTrue(mWorkType.equals(new WorkType("id", "name")));
        assertFalse(mWorkType.equals(new WorkType("not id", "name")));
        assertFalse(mWorkType.equals(new WorkType("id", "not name")));
    }

    public void testParcelable() {
        Parcel in = Parcel.obtain();
        Parcel out = Parcel.obtain();
        WorkType result = null;
        try {
            in.writeParcelable(mWorkType, 0);
            byte[] bytes = in.marshall();
            out.unmarshall(bytes, 0, bytes.length);
            out.setDataPosition(0);
            result = out.readParcelable(WorkType.class.getClassLoader());
        } finally {
            in.recycle();
            out.recycle();
        }

        assertNotNull(result);
        assertEquals(mWorkType, result);
    }
}

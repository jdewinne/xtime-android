package com.xebia.xtime.test.shared.model;

import android.os.Parcel;

import com.xebia.xtime.shared.model.Project;

import junit.framework.TestCase;

import static com.xebia.xtime.test.shared.model.TestValues.*;


public class ProjectTest extends TestCase {

    public void testEquals() {
        assertEquals(PROJECT, new Project(PROJECT_ID, PROJECT_NAME));
        assertFalse(PROJECT.equals(new Project("wrong", PROJECT_NAME)));
        assertFalse(PROJECT.equals(new Project(PROJECT_ID, "wrong")));
    }

    public void testParcelable() {
        Parcel in = Parcel.obtain();
        Parcel out = Parcel.obtain();
        Project result = null;
        try {
            in.writeParcelable(PROJECT, 0);
            byte[] bytes = in.marshall();
            out.unmarshall(bytes, 0, bytes.length);
            out.setDataPosition(0);
            result = out.readParcelable(Project.class.getClassLoader());
        } finally {
            in.recycle();
            out.recycle();
        }

        assertNotNull(result);
        assertEquals(PROJECT, result);
    }
}

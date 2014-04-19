package com.xebia.xtime.test.shared.model;

import android.os.Parcel;

import com.xebia.xtime.shared.model.Project;

import junit.framework.TestCase;

public class ProjectTest extends TestCase {

    private Project mProject;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mProject = new Project("id", "name");
    }

    public void testEquals() {
        assertTrue(mProject.equals(new Project("id", "name")));
        assertFalse(mProject.equals(new Project("not id", "name")));
        assertFalse(mProject.equals(new Project("id", "not name")));
    }

    public void testParcelable() {
        Parcel in = Parcel.obtain();
        Parcel out = Parcel.obtain();
        Project result = null;
        try {
            in.writeParcelable(mProject, 0);
            byte[] bytes = in.marshall();
            out.unmarshall(bytes, 0, bytes.length);
            out.setDataPosition(0);
            result = out.readParcelable(Project.class.getClassLoader());
        } finally {
            in.recycle();
            out.recycle();
        }

        assertNotNull(result);
        assertEquals(mProject, result);
    }
}

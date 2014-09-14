package com.xebia.xtime.test.shared.model;

import android.os.Parcel;

import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.Task;
import com.xebia.xtime.shared.model.WorkType;

import junit.framework.TestCase;

import static com.xebia.xtime.test.shared.model.TestValues.DESCRIPTION;
import static com.xebia.xtime.test.shared.model.TestValues.PROJECT;
import static com.xebia.xtime.test.shared.model.TestValues.TASK;
import static com.xebia.xtime.test.shared.model.TestValues.WORK_TYPE;

public class TaskTest extends TestCase {

    public void testEquals() {
        assertEquals(TASK, new Task(PROJECT, WORK_TYPE, DESCRIPTION));
        assertFalse(TASK.equals(new Task(new Project("wrong", "wrong"), WORK_TYPE, DESCRIPTION)));
        assertFalse(TASK.equals(new Task(PROJECT, new WorkType("wrong", "wrong"), DESCRIPTION)));
        assertFalse(TASK.equals(new Task(PROJECT, WORK_TYPE, "wrong")));
    }

    public void testParcelable() {
        Parcel in = Parcel.obtain();
        Parcel out = Parcel.obtain();
        Task result = null;
        try {
            in.writeParcelable(TASK, 0);
            byte[] bytes = in.marshall();
            out.unmarshall(bytes, 0, bytes.length);
            out.setDataPosition(0);
            result = out.readParcelable(Task.class.getClassLoader());
        } finally {
            in.recycle();
            out.recycle();
        }

        assertNotNull(result);
        assertEquals(TASK, result);
    }
}

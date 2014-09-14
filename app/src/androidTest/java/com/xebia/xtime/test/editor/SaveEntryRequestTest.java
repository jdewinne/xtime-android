package com.xebia.xtime.test.editor;

import com.xebia.xtime.editor.save.SaveEntryRequest;
import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.Task;
import com.xebia.xtime.shared.model.TimeEntry;
import com.xebia.xtime.shared.model.WorkType;

import junit.framework.TestCase;

import java.util.Date;

public class SaveEntryRequestTest extends TestCase {

    public static final String EXPECTED = "startDate=9+Mar+2015" +
            "&endDate=15+Mar+2015" +
            "&weekDates=2015-03-09" +
            "&weekDates=2015-03-10" +
            "&weekDates=2015-03-11" +
            "&weekDates=2015-03-12" +
            "&weekDates=2015-03-13" +
            "&weekDates=2015-03-14" +
            "&weekDates=2015-03-15" +
            "&projectId=42" +
            "&workType=100" +
            "&description=foo+bar" +
            "&monday=&tuesday=&wednesday=&thursday=&friday=&saturday=3.14&sunday=" +
            "&weekTotal1=3.14" +
            "&projectId=" +
            "&workType=" +
            "&description=" +
            "&monday=&tuesday=&wednesday=&thursday=&friday=&saturday=&sunday=" +
            "&weekTotal1=" +
            "&projectId=" +
            "&workType=" +
            "&description=" +
            "&monday=&tuesday=&wednesday=&thursday=&friday=&saturday=&sunday=" +
            "&weekTotal1=" +
            "&projectId=" +
            "&workType=" +
            "&description=" +
            "&monday=&tuesday=&wednesday=&thursday=&friday=&saturday=&sunday=" +
            "&weekTotal1=" +
            "&projectId=" +
            "&workType=" +
            "&description=" +
            "&monday=&tuesday=&wednesday=&thursday=&friday=&saturday=&sunday=" +
            "&weekTotal1=" +
            "&dayTotal1=" +
            "&dayTotal2=" +
            "&dayTotal3=" +
            "&dayTotal4=" +
            "&dayTotal5=" +
            "&dayTotal6=3.14" +
            "&dayTotal7=" +
            "&grandTotal=3.14" +
            "&buttonClicked=save";

    public void testRequestData() {
        // request to write 3.14 hours on some project on pi day
        Project project = new Project("42", "some project name");
        WorkType workType = new WorkType("100", "some work description");
        String description = "foo bar";
        Date date = new Date(1426287600000l); // Sat, 14 Mar 2015, 0:00:00 CET
        TimeEntry timeEntry = new TimeEntry(new Task(project, workType, description), date, 3.14,
                false);
        SaveEntryRequest request = new SaveEntryRequest(timeEntry);

        String data = request.getRequestData();

        assertEquals(EXPECTED, data);
    }
}

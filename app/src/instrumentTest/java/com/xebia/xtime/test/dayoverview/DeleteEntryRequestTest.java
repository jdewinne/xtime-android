package com.xebia.xtime.test.dayoverview;

import com.xebia.xtime.editor.delete.DeleteEntryRequest;
import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.TimeCell;
import com.xebia.xtime.shared.model.TimeSheetEntry;
import com.xebia.xtime.shared.model.WorkType;

import junit.framework.TestCase;

import java.util.Date;

public class DeleteEntryRequestTest extends TestCase {

    public static final String EXPECTED = "callCount=1\n" +
            "page=/xtime/entryform.html\n" +
            "httpSessionId=\n" +
            "scriptSessionId=\n" +
            "c0-scriptName=TimeEntryServiceBean\n" +
            "c0-methodName=deleteTimeSheetEntries\n" +
            "c0-id=0\n" +
            "c0-param0=string:1\n" +
            "c0-param1=string:940\n" +
            "c0-param2=string:description\n" +
            "c0-param3=string:2015-03-14\n" +
            "batchId=0";

    public void testRequestData() {
        // request to get the week overview for pi day
        Date date = new Date(1426287600000l); // Sat, 14 Mar 2015, 0:00:00 CET
        TimeSheetEntry timeSheetEntry = new TimeSheetEntry(new Project("1", "foo"),
                new WorkType("940", "bar"), "description", new TimeCell(date, 8, false));
        DeleteEntryRequest request = new DeleteEntryRequest(timeSheetEntry);

        String data = request.getRequestData();

        assertEquals(EXPECTED, data);
    }
}

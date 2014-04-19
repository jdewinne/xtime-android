package com.xebia.xtime.test.weekoverview.loader;

import com.xebia.xtime.weekoverview.loader.WeekOverviewRequest;

import junit.framework.TestCase;

import java.util.Date;

public class WeekOverviewRequestTest extends TestCase {

    public static final String EXPECTED = "callCount=1\n" +
            "page=/xtime/entryform.html\n" +
            "httpSessionId=\n" +
            "scriptSessionId=\n" +
            "c0-scriptName=TimeEntryServiceBean\n" +
            "c0-methodName=getWeekOverview\n" +
            "c0-id=0\n" +
            "c0-param0=string:2015-03-14\n" +
            "c0-param1=boolean:true\n" +
            "batchId=0\n";

    public void testRequestData() {
        // request to get the week overview for pi day
        Date date = new Date(1426287600000l); // Sat, 14 Mar 2015, 0:00:00 CET
        WeekOverviewRequest request = new WeekOverviewRequest(date);

        String data = request.getRequestData();

        assertEquals(EXPECTED, data);
    }
}

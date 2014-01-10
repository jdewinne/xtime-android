package com.xebia.xtime.test.monthoverview;

import com.xebia.xtime.monthoverview.loader.MonthOverviewRequest;

import junit.framework.TestCase;

import java.util.Date;

public class MonthOverviewRequestTest extends TestCase {

    public static final String EXPECTED = "callCount=1\n" +
            "page=/xtime/monthlyApprove.html\n" +
            "httpSessionId=\n" +
            "scriptSessionId=\n" +
            "c0-scriptName=TimeEntryServiceBean\n" +
            "c0-methodName=getMonthOverview\n" +
            "c0-id=0\n" +
            "c0-param0=string:2015-01-01\n" +
            "batchId=0";

    public void testRequestData() {
        Date month = new Date(1420066800000l); // 1 Jan 2015, 0:00:00 CET
        MonthOverviewRequest request = new MonthOverviewRequest(month);
        String data = request.getRequestData();
        assertEquals(EXPECTED, data);
    }
}

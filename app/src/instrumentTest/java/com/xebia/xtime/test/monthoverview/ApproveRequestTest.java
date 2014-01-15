package com.xebia.xtime.test.monthoverview;

import com.xebia.xtime.monthoverview.approve.ApproveRequest;
import com.xebia.xtime.shared.model.TimeCell;
import com.xebia.xtime.shared.model.TimeSheetRow;
import com.xebia.xtime.shared.model.WeekOverview;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ApproveRequestTest extends TestCase {

    public static final String EXPECTED =
            "approvalMonthYear=2015-01-01&approve=Approve&inp_grandTotal=24";

    public void testRequestData() {
        List<TimeCell> timeCells = Arrays.asList(new TimeCell(new Date(1), 8, false),
                new TimeCell(new Date(2), 8, false), new TimeCell(new Date(3), 8, false));
        List<TimeSheetRow> timeSheetRows = Arrays.asList(new TimeSheetRow(null, null, null,
                timeCells));
        WeekOverview weekOverview = new WeekOverview(timeSheetRows, null, null, false, null);
        Date month = new Date(1420066800000l); // 1 Jan 2015, 0:00:00 CET
        ApproveRequest request = new ApproveRequest(weekOverview, month);
        String data = request.getRequestData();
        assertEquals(EXPECTED, data);
    }
}

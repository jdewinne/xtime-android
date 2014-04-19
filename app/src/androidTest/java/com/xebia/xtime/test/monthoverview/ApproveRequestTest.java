package com.xebia.xtime.test.monthoverview;

import com.xebia.xtime.monthoverview.approve.ApproveRequest;

import junit.framework.TestCase;

import java.util.Date;

public class ApproveRequestTest extends TestCase {

    public static final String EXPECTED =
            "approvalMonthYear=2015-01-01&approve=Approve&inp_grandTotal=24.5";

    public void testRequestData() {
        double grandTotal = 24.5;
        Date month = new Date(1420066800000l); // 1 Jan 2015, 0:00:00 CET
        ApproveRequest request = new ApproveRequest(grandTotal, month);
        String data = request.getRequestData();
        assertEquals(EXPECTED, data);
    }
}

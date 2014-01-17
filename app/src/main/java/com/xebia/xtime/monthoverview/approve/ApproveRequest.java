package com.xebia.xtime.monthoverview.approve;

import com.xebia.xtime.shared.XTimeRequest;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ApproveRequest extends XTimeRequest {

    private static final String XTIME_URL = "https://xtime.xebia.com/xtime/monthlyApprove.html";
    private final Date mMonth;
    private double mGrandTotal;

    public ApproveRequest(double grandTotal, Date month) {
        mGrandTotal = grandTotal;
        mMonth = month;
    }

    @Override
    public String getContentType() {
        return CONTENT_TYPE_FORM;
    }

    @Override
    public String getRequestData() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("CET"));
        return "approvalMonthYear=" + dateFormat.format(mMonth) +
                "&approve=Approve" +
                "&inp_grandTotal=" + NumberFormat.getInstance(Locale.US).format(mGrandTotal);
    }

    @Override
    public String getUrl() {
        return XTIME_URL;
    }
}

package com.xebia.xtime.monthoverview.approve;

import com.xebia.xtime.shared.TimeSheetUtils;
import com.xebia.xtime.shared.XTimeRequest;
import com.xebia.xtime.shared.model.WeekOverview;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ApproveRequest extends XTimeRequest {

    private static final String XTIME_URL = "https://xtime.xebia.com/xtime/monthlyApprove.html";
    private final WeekOverview mOverview;
    private final Date mMonth;

    public ApproveRequest(WeekOverview overview, Date month) {
        mOverview = overview;
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
        double grandTotal = TimeSheetUtils.getGrandTotalHours(mOverview);
        return "approvalMonthYear=" + dateFormat.format(mMonth) +
                "&approve=Approve" +
                "&inp_grandTotal=" + NumberFormat.getInstance(Locale.US).format(grandTotal);
    }

    @Override
    public String getUrl() {
        return XTIME_URL;
    }
}

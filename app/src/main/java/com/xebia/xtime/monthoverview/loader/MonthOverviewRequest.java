package com.xebia.xtime.monthoverview.loader;

import com.xebia.xtime.shared.XTimeRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MonthOverviewRequest extends XTimeRequest {

    private static final String XTIME_URL = "https://xtime.xebia.com" +
            "/xtime/dwr/call/plaincall/TimeEntryServiceBean.getMonthOverview.dwr";
    private final Date mMonth;

    public MonthOverviewRequest(Date month) {
        mMonth = month;
    }

    @Override
    public String getRequestData() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("CET"));
        return "callCount=1\n" +
                "page=/xtime/monthlyApprove.html\n" +
                "httpSessionId=\n" +
                "scriptSessionId=\n" +
                "c0-scriptName=TimeEntryServiceBean\n" +
                "c0-methodName=getMonthOverview\n" +
                "c0-id=0\n" +
                "c0-param0=string:" + dateFormat.format(mMonth) + "\n" +
                "batchId=0";
    }

    @Override
    public String getUrl() {
        return XTIME_URL;
    }
}

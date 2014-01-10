package com.xebia.xtime.weekoverview.loader;

import com.xebia.xtime.shared.XTimeRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class WeekOverviewRequest extends XTimeRequest {

    private static final String XTIME_URL = "https://xtime.xebia.com" +
            "/xtime/dwr/call/plaincall/TimeEntryServiceBean.getWeekOverview.dwr";
    private final Date mDate;

    /**
     * Constructor.
     *
     * @param date Date of the first day of the week that will be requested.
     */
    public WeekOverviewRequest(Date date) {
        mDate = date;
    }

    @Override
    public String getRequestData() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("CET"));
        return "callCount=1\n" +
                "page=/xtime/entryform.html\n" +
                "httpSessionId=\n" +
                "scriptSessionId=\n" +
                "c0-scriptName=TimeEntryServiceBean\n" +
                "c0-methodName=getWeekOverview\n" +
                "c0-id=0\n" + // only used for JSONP callback
                "c0-param0=string:" + dateFormat.format(mDate) + "\n" +
                "c0-param1=boolean:true\n" + // not used?
                "batchId=0\n"; // only used for JSONP callback
    }

    @Override
    public String getUrl() {
        return XTIME_URL;
    }
}

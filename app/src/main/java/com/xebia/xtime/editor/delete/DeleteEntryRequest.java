package com.xebia.xtime.editor.delete;

import com.xebia.xtime.shared.XTimeRequest;
import com.xebia.xtime.shared.model.TimeEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DeleteEntryRequest extends XTimeRequest {

    private static final String XTIME_URL = "https://xtime.xebia.com" +
            "/xtime/dwr/call/plaincall/TimeEntryServiceBean.deleteTimeSheetEntries.dwr";
    private final TimeEntry mTimeEntry;

    public DeleteEntryRequest(TimeEntry timeEntry) {
        mTimeEntry = timeEntry;
    }

    @Override
    public String getContentType() {
        return CONTENT_TYPE_PLAIN;
    }

    @Override
    public String getRequestData() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("CET"));
        return "callCount=1\n"
                + "page=/xtime/entryform.html\n"
                + "httpSessionId=\n"
                + "scriptSessionId=\n"
                + "c0-scriptName=TimeEntryServiceBean\n"
                + "c0-methodName=deleteTimeSheetEntries\n"
                + "c0-id=0\n"
                + "c0-param0=string:" + mTimeEntry.getTask().getProject().getId() + "\n"
                + "c0-param1=string:" + mTimeEntry.getTask().getWorkType().getId() + "\n"
                + "c0-param2=string:" + mTimeEntry.getTask().getDescription() + "\n"
                + "c0-param3=string:" + dateFormat.format(mTimeEntry.getEntryDate()) + "\n"
                + "batchId=0";
    }

    @Override
    public String getUrl() {
        return XTIME_URL;
    }
}

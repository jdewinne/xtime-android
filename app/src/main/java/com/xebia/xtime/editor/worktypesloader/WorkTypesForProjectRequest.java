package com.xebia.xtime.editor.worktypesloader;

import com.xebia.xtime.shared.XTimeRequest;
import com.xebia.xtime.shared.model.Project;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class WorkTypesForProjectRequest extends XTimeRequest {

    private static final String XTIME_URL = "https://xtime.xebia.com" +
            "/xtime/dwr/call/plaincall/TimeEntryServiceBean.getWorkTypesListForProjectInRange.dwr";
    private final Project mProject;
    private final Date mDate;

    public WorkTypesForProjectRequest(Project project, Date date) {
        mProject = project;
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
                "c0-methodName=getWorkTypesListForProjectInRange\n" +
                "c0-id=0\n" +
                "c0-param0=string:" + mProject.getId() + "\n" +
                "c0-param1=string:" + dateFormat.format(mDate) + "\n" +
                "c0-param2=boolean:true\n" +
                "batchId=0";
    }

    @Override
    public String getUrl() {
        return XTIME_URL;
    }
}

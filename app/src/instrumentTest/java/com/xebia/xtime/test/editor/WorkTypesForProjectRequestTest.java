package com.xebia.xtime.test.editor;

import com.xebia.xtime.editor.worktypesloader.WorkTypesForProjectRequest;
import com.xebia.xtime.shared.model.Project;

import junit.framework.TestCase;

import java.util.Date;

public class WorkTypesForProjectRequestTest extends TestCase {

    public static final String EXPECTED = "callCount=1\n" +
            "page=/xtime/entryform.html\n" +
            "httpSessionId=\n" +
            "scriptSessionId=\n" +
            "c0-scriptName=TimeEntryServiceBean\n" +
            "c0-methodName=getWorkTypesListForProjectInRange\n" +
            "c0-id=0\n" +
            "c0-param0=string:31415\n" +
            "c0-param1=string:2015-03-14\n" +
            "c0-param2=boolean:true\n" +
            "batchId=0";

    public void testRequestData() {
        // request to get work types for some project on pi day
        Project project = new Project("31415", "some project name");
        Date date = new Date(1426287600000l); // Sat, 14 Mar 2015, 0:00:00 CET
        WorkTypesForProjectRequest request = new WorkTypesForProjectRequest(project, date);

        String data = request.getRequestData();

        assertEquals(EXPECTED, data);
    }
}

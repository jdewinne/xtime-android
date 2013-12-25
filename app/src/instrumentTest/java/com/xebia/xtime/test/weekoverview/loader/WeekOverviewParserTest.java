package com.xebia.xtime.test.weekoverview.loader;

import com.xebia.xtime.weekoverview.loader.WeekOverviewParser;
import com.xebia.xtime.shared.model.TimeSheetRow;
import com.xebia.xtime.shared.model.WeekOverview;

import junit.framework.TestCase;

import java.util.Date;

public class WeekOverviewParserTest extends TestCase {

    private final String INPUT_UNPARSEABLE = "this is intentionally not parseable";
    private static final String INPUT_REGULAR = "throw 'allowScriptTagRemoting is false.';\n" +
            "//#DWR-INSERT\n" +
            "//#DWR-REPLY\n" +
            "var s0=[];var s3={};var s4={};var s5={};var s6={};var s7={};var s8={};var s9={};var " +
            "s10={};var s11={};var s12={};var s1=[];var s13={};var s15=[];var s16={};var s14={};" +
            "var s17=[];var s18={};var s2=[];s0[0]=s3;s0[1]=s4;s0[2]=s5;s0[3]=s6;s0[4]=s7;" +
            "s0[5]=s8;s0[6]=s9;s0[7]=s10;s0[8]=s11;s0[9]=s12;\n" +
            "s3.description=\"PROJECT 1\";s3.id=\"0001\";\n" +
            "s7.description=\"PROJECT 2\";s7.id=\"0002\";\n" +
            "s12.description=\"XKE\";s12.id=\"0062\";\n" +
            "s1[0]=s13;s1[1]=s14;\n" +
            "s13.clientName=\"CLIENT 1\";s13.description=\"description\";s13.projectId=\"0001\";" +
            "s13.projectName=\"PROJECT 1\";s13.timeCells=s15;s13.userId=\"1234567\";s13" +
            ".workTypeDescription=\"Work work\";s13.workTypeId=\"100\";\n" +
            "s15[0]=s16;\n" +
            "s16.approved=true;s16.entryDate=new Date(11111);s16.fromAfas=false;s16.hour=\"7.0\";" +
            "s16.transferredToAfas=false;\n" +
            "s14.clientName=\"CLIENT 2\";s14.description=\"\";s14.projectId=\"0002\";s14" +
            ".projectName=\"PROJECT 2\";s14.timeCells=s17;s14.userId=\"1234567\";s14" +
            ".workTypeDescription=\"Work work\";s14.workTypeId=\"100\";\n" +
            "s17[0]=s18;\n" +
            "s18.approved=true;s18.entryDate=new Date(22222);s18.fromAfas=false;s18.hour=\"1.0\";" +
            "s18.transferredToAfas=false;\n" +
            "s2[0]=21;s2[1]=22;\n" +
            "dwr.engine._remoteHandleCallback('0','0',{lastTransferredDate:new Date" +
            "(1385766000000),monthDaysCount:7,monthlyDataApproved:true," +
            "monthlyDataTransferred:false,projects:s0,timesheetRows:s1,userName:\"Foo Bar\"," +
            "weekEndDates:s2,weekStart:null});";

    public void testRegularInput() {
        WeekOverview result = WeekOverviewParser.parse(INPUT_REGULAR);

        // list of projects
        assertEquals(3, result.getProjects().size());
        assertEquals("PROJECT 1", result.getProjects().get(0).getDescription());
        assertEquals("0001", result.getProjects().get(0).getId());
        assertEquals("PROJECT 2", result.getProjects().get(1).getDescription());
        assertEquals("0002", result.getProjects().get(1).getId());
        assertEquals("XKE", result.getProjects().get(2).getDescription());
        assertEquals("0062", result.getProjects().get(2).getId());

        // list of time sheet rows
        assertEquals(2, result.getTimeSheetRows().size());
        TimeSheetRow row = result.getTimeSheetRows().get(0);
        assertEquals("CLIENT 1", row.getClientName());
        assertEquals("description", row.getDescription());
        assertEquals("PROJECT 1", row.getProjectName());
        assertEquals("0001", row.getProjectId());
        assertEquals("1234567", row.getUserId());
        assertEquals("Work work", row.getWorkTypeDescription());
        assertEquals("100", row.getWorkTypeId());
        assertEquals(1, row.getTimeCells().size());
        assertEquals(new Date(11111), row.getTimeCells().get(0).getEntryDate());

        row = result.getTimeSheetRows().get(1);
        assertEquals("CLIENT 2", row.getClientName());
        assertEquals("", row.getDescription());
        assertEquals("PROJECT 2", row.getProjectName());
        assertEquals("0002", row.getProjectId());
        assertEquals("1234567", row.getUserId());
        assertEquals("Work work", row.getWorkTypeDescription());
        assertEquals("100", row.getWorkTypeId());
        assertEquals(1, row.getTimeCells().size());
        assertEquals(new Date(22222), row.getTimeCells().get(0).getEntryDate());

        assertEquals("Foo Bar", result.getUsername());
    }

    public void testUnparseableInput() {
        WeekOverview result = WeekOverviewParser.parse(INPUT_UNPARSEABLE);
        assertNull(result);
    }

    public void testEmptyInput() {
        WeekOverview result = WeekOverviewParser.parse("");
        assertNull(result);
    }

    public void testNullInput() {
        WeekOverview result = WeekOverviewParser.parse(null);
        assertNull(result);
    }
}

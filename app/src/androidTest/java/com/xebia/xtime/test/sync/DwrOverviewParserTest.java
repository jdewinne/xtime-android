package com.xebia.xtime.test.sync;

import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.Task;
import com.xebia.xtime.shared.model.TimeEntry;
import com.xebia.xtime.shared.model.WorkType;
import com.xebia.xtime.shared.model.XTimeOverview;
import com.xebia.xtime.sync.DwrOverviewParser;

import junit.framework.TestCase;

import java.util.Date;

public class DwrOverviewParserTest extends TestCase {

    private static final String INPUT_REGULAR = "throw 'allowScriptTagRemoting is false.';\n" +
            "//#DWR-INSERT\n" +
            "//#DWR-REPLY\n" +
            "var s0=[];var s3={};var s4={};var s5={};var s6={};var s7={};var s8={};var s9={};" +
            "var s10={};var s11={};var s12={};var s13={};var s1=[];var s14={};var s15={};" +
            "var s16=[];var s17={};var s18={};var s19={};var s20=[];var s21={};var s2=[];" +
            "s0[0]=s3;s0[1]=s4;s0[2]=s5;s0[3]=s6;s0[4]=s7;s0[5]=s8;s0[6]=s9;s0[7]=s10;s0[8]=s11;" +
            "s0[9]=s12;s0[10]=s13;\n" +
            "s3.description=\"PROJECT 1\";s3.id=\"0001\";\n" +
            "s4.description=\"PROJECT 2\";s4.id=\"0002\";\n" +
            "s5.description=\"PROJECT 3\";s5.id=\"0003\";\n" +
            "s6.description=\"PROJECT 4\";s6.id=\"0004\";\n" +
            "s7.description=\"PROJECT 5\";s7.id=\"0005\";\n" +
            "s8.description=\"PROJECT 6\";s8.id=\"0006\";\n" +
            "s9.description=\"PROJECT 7\";s9.id=\"0007\";\n" +
            "s10.description=\"Internal projects\";s10.id=\"1\";\n" +
            "s11.description=\"Pre-sales werkzaamheden\";s11.id=\"0073\";\n" +
            "s12.description=\"TECHRALLY\";s12.id=\"0072\";\n" +
            "s13.description=\"XKE\";s13.id=\"0062\";\n" +
            "s1[0]=s14;s1[1]=s15;\n" +
            "s14.clientName=\"Xebia Internal\";s14.description=\"\";s14.projectId=\"1\";" +
            "s14.projectName=\"Internal Project\";s14.timeCells=s16;s14.userId=\"1015900\";" +
            "s14.workTypeDescription=\"Holiday\";s14.workTypeId=\"950\";\n" +
            "s16[0]=s17;s16[1]=s18;s16[2]=s19;\n" +
            "s17.approved=true;s17.entryDate=new Date(1388358000000);s17.fromAfas=true;" +
            "s17.hour=\"8.0\";s17.transferredToAfas=false;\n" +
            "s18.approved=true;s18.entryDate=new Date(1388444400000);s18.fromAfas=true;" +
            "s18.hour=\"8.0\";s18.transferredToAfas=false;\n" +
            "s19.approved=false;s19.entryDate=new Date(1388530800000);s19.fromAfas=false;" +
            "s19.hour=\"8.0\";s19.transferredToAfas=false;\n" +
            "s15.clientName=\"CLIENT X\";s15.description=\"\";s15.projectId=\"0001\";" +
            "s15.projectName=\"PROJECT 1\";s15.timeCells=s20;s15.userId=\"1015900\";" +
            "s15.workTypeDescription=\"Work work work\";s15.workTypeId=\"400\";\n" +
            "s20[0]=s21;\n" +
            "s21.approved=false;s21.entryDate=new Date(1388617200000);s21.fromAfas=false;" +
            "s21.hour=\"7.0\";s21.transferredToAfas=false;\n" +
            "s2[0]=4;s2[1]=5;\n" +
            "dwr.engine._remoteHandleCallback('0','0',{lastTransferredDate:" +
            "new Date(1388444400000),monthDaysCount:7,monthlyDataApproved:true," +
            "monthlyDataTransferred:true,projects:s0,timesheetRows:s1,userName:\"Foo Bar\"," +
            "weekEndDates:s2,weekStart:null});\n";

    public void testRegularInput() {

        // prepare the right answer
        Task holiday = new Task(new Project("1", "Internal Project"), new WorkType("950",
                "Holiday"), "");
        Task workWorkWork = new Task(new Project("0001", "PROJECT 1"), new WorkType("400",
                "Work work work"), "");
        XTimeOverview expected = new XTimeOverview.Builder()
                .setUsername("Foo Bar")
                .setLastTransferred(new Date(1388444400000l))
                .setMonthlyDataApproved(true)
                .addProject(new Project("0001", "PROJECT 1"))
                .addProject(new Project("0002", "PROJECT 2"))
                .addProject(new Project("0003", "PROJECT 3"))
                .addProject(new Project("0004", "PROJECT 4"))
                .addProject(new Project("0005", "PROJECT 5"))
                .addProject(new Project("0006", "PROJECT 6"))
                .addProject(new Project("0007", "PROJECT 7"))
                .addProject(new Project("1", "Internal projects"))
                .addProject(new Project("0073", "Pre-sales werkzaamheden"))
                .addProject(new Project("0072", "TECHRALLY"))
                .addProject(new Project("0062", "XKE"))
                .addTimeEntry(new TimeEntry(holiday, new Date(1388358000000l), 8.0, true))
                .addTimeEntry(new TimeEntry(holiday, new Date(1388444400000l), 8.0, true))
                .addTimeEntry(new TimeEntry(holiday, new Date(1388530800000l), 8.0, false))
                .addTimeEntry(new TimeEntry(workWorkWork, new Date(1388617200000l), 7.0, false))
                .build();

        XTimeOverview result = DwrOverviewParser.parse(INPUT_REGULAR);
        assertEquals(expected, result);
    }

    public void testUnparseableInput() {
        XTimeOverview result = DwrOverviewParser.parse("this is intentionally not parseable");
        assertNull(result);
    }

    public void testEmptyInput() {
        XTimeOverview result = DwrOverviewParser.parse("");
        assertNull(result);
    }

    public void testNullInput() {
        XTimeOverview result = DwrOverviewParser.parse(null);
        assertNull(result);
    }
}

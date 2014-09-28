package com.xebia.xtime.test.sync;

import android.test.InstrumentationTestCase;

import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.Task;
import com.xebia.xtime.shared.model.TimeEntry;
import com.xebia.xtime.shared.model.WorkType;
import com.xebia.xtime.shared.model.XTimeOverview;
import com.xebia.xtime.sync.DwrOverviewParser;
import com.xebia.xtime.test.Mocks;

import java.io.IOException;
import java.util.Date;

public class DwrOverviewParserTest extends InstrumentationTestCase {

    public void testRegularInput() throws IOException {

        // prepare the right answer
        Task xtime = new Task(new Project("1", "Internal Project"), new WorkType("940",
                "Internal work"), "XTime for Android");
        XTimeOverview expected = new XTimeOverview.Builder()
                .setUsername("Foo Bar")
                .setLastTransferred(new Date(1385766000000l))
                .setMonthlyDataApproved(false)
                .addProject(new Project("1757", "PROJECT 1757"))
                .addProject(new Project("1831", "PROJECT 1831"))
                .addProject(new Project("0940", "India Mentorship Programme"))
                .addProject(new Project("1828", "PROJECT 1828"))
                .addProject(new Project("1860", "PROJECT 1860"))
                .addProject(new Project("1", "Internal projects"))
                .addProject(new Project("1850", "PROJECT 1850"))
                .addProject(new Project("0073", "Pre-sales werkzaamheden"))
                .addProject(new Project("0072", "TECHRALLY"))
                .addProject(new Project("0062", "XKE"))
                .addTimeEntry(new TimeEntry(xtime, new Date(1388358000000l), 3.25, false))
                .addTimeEntry(new TimeEntry(xtime, new Date(1388444400000l), 3.5, false))
                .addTimeEntry(new TimeEntry(xtime, new Date(1388530800000l), 3.75, false))
                .build();

        String input = Mocks.getWeekOverviewResponse(getInstrumentation().getContext());
        XTimeOverview result = DwrOverviewParser.parse(input);
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

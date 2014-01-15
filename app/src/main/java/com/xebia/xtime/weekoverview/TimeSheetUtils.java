package com.xebia.xtime.weekoverview;

import android.text.format.DateUtils;
import android.util.SparseArray;

import com.xebia.xtime.shared.model.DayOverview;
import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.TimeCell;
import com.xebia.xtime.shared.model.TimeSheetEntry;
import com.xebia.xtime.shared.model.TimeSheetRow;
import com.xebia.xtime.shared.model.WeekOverview;
import com.xebia.xtime.shared.model.WorkType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TimeSheetUtils {

    private static final int[] DAILY_INDEXES = new int[]{Calendar.MONDAY, Calendar.TUESDAY,
            Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY,
            Calendar.SUNDAY};

    /**
     * Converts a week overview into a list of 7 day overviews.
     * <p/>
     * Each day of the week gets a separate DayOverview model. The list of TimeSheetRows is split
     * up into separate TimeSheetEntries, which are stored in the correct DayOverview
     *
     * @param overview  WeekOverview to split up
     * @param startDate The date of the first day of the week
     * @return A list of 7 days
     */
    public static List<DayOverview> weekToDays(WeekOverview overview, Date startDate) {

        Date lastTransferred = overview.getLastTransferred();

        // initialize array of day overview entries for the week, indexed by Calendar.DAY_OF_WEEK
        SparseArray<DayOverview> dailyHoursArray = new SparseArray<DayOverview>();
        for (int i = 0; i < DAILY_INDEXES.length; i++) {
            Date date = new Date(startDate.getTime() + i * DateUtils.DAY_IN_MILLIS);
            boolean editable = lastTransferred.before(date);
            DayOverview dayOverview = new DayOverview(date, overview.getProjects(), editable);
            dailyHoursArray.put(DAILY_INDEXES[i], dayOverview);
        }

        // fill the array with data from the overview
        for (TimeSheetRow row : overview.getTimeSheetRows()) {

            // find the project that is related to this row
            Project project = row.getProject();
            WorkType workType = row.getWorkType();
            String description = row.getDescription();

            // group the time cells by day of the week
            for (TimeCell timeCell : row.getTimeCells()) {
                // get day overview for this day from array
                Calendar entryCal = Calendar.getInstance();
                entryCal.setTime(timeCell.getEntryDate());
                entryCal.setTimeZone(TimeZone.getTimeZone("CET"));
                DayOverview dayOverview = dailyHoursArray.get(entryCal.get(Calendar.DAY_OF_WEEK));

                // add time registration entry
                TimeSheetEntry timeReg = new TimeSheetEntry(project, workType, description,
                        timeCell);
                dayOverview.getTimeSheetEntries().add(timeReg);

                // increment total hours
                dayOverview.setTotalHours(dayOverview.getTotalHours() + timeCell.getHours());

                dailyHoursArray.put(entryCal.get(Calendar.DAY_OF_WEEK), dayOverview);
            }
        }

        // return list of totalHours, ordered by date
        List<DayOverview> result = new ArrayList<DayOverview>();
        for (int day : DAILY_INDEXES) {
            result.add(dailyHoursArray.get(day));
        }

        return result;
    }

    public static double getTotalHours(TimeSheetRow row) {
        double total = 0;
        for (TimeCell timeCell : row.getTimeCells()) {
            total += timeCell.getHours();
        }
        return total;
    }

    public static double getGrandTotalHours(WeekOverview overview) {
        double total = 0;
        for (TimeSheetRow row : overview.getTimeSheetRows()) {
            total += getTotalHours(row);
        }
        return total;
    }
}

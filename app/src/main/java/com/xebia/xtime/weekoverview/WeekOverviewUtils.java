package com.xebia.xtime.weekoverview;

import android.database.Cursor;
import android.text.format.DateUtils;
import android.util.SparseArray;

import com.xebia.xtime.content.XTimeContract.TimeEntries;
import com.xebia.xtime.content.XTimeContract.TimeSheetRows;
import com.xebia.xtime.shared.model.DayOverview;
import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.TimeCell;
import com.xebia.xtime.shared.model.TimeSheetEntry;
import com.xebia.xtime.shared.model.TimeSheetRow;
import com.xebia.xtime.shared.model.WorkType;
import com.xebia.xtime.shared.model.XTimeOverview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public final class WeekOverviewUtils {

    private static final int[] DAILY_INDEXES = new int[]{Calendar.MONDAY, Calendar.TUESDAY,
            Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY,
            Calendar.SUNDAY};

    private WeekOverviewUtils() {
        // do not instantiate
    }

    public static XTimeOverview cursorToOverview(final Cursor cursor) {
        cursor.moveToFirst();
        long lastSheetRowId = -1;
        XTimeOverview.Builder overviewBuilder = new XTimeOverview.Builder();
        TimeSheetRow.Builder rowBuilder = null;
        while (!cursor.isAfterLast()) {
            // time sheet row details
            long sheetRowId = cursor.getLong(cursor.getColumnIndex(TimeEntries.SHEET_ROW_ID));
            if (sheetRowId != lastSheetRowId) {
                if (null != rowBuilder) {
                    overviewBuilder.addTimeSheetRow(rowBuilder.build());
                }
                rowBuilder = new TimeSheetRow.Builder()
                        .setDescription(
                                cursor.getString(cursor.getColumnIndex(TimeSheetRows.DESCRIPTION)))
                        .setProject(new Project(
                                cursor.getString(cursor.getColumnIndex(TimeSheetRows.PROJECT_ID)),
                                cursor.getString(cursor.getColumnIndex(
                                        TimeSheetRows.PROJECT_NAME))))
                        .setWorkType(new WorkType(
                                cursor.getString(cursor.getColumnIndex(TimeSheetRows.WORKTYPE_ID)),
                                cursor.getString(cursor.getColumnIndex(
                                        TimeSheetRows.WORKTYPE_NAME))));
                lastSheetRowId = sheetRowId;
            }

            // time entry details
            double hours = cursor.getDouble(cursor.getColumnIndex(TimeEntries.HOURS));
            boolean approved = cursor.getLong(cursor.getColumnIndex(TimeEntries.APPROVED)) == 1;
            long entryDate = cursor.getLong(cursor.getColumnIndex(TimeEntries.ENTRY_DATE));
            if (null != rowBuilder) {
                rowBuilder.addTimeCell(new TimeCell(new Date(entryDate), hours, approved));
            }

            cursor.moveToNext();
        }
        // make sure the last row is also added to the sheet
        if (null != rowBuilder) {
            overviewBuilder.addTimeSheetRow(rowBuilder.build());
        }
        return overviewBuilder.build();
    }

    /**
     * Converts a week overview into a list of 7 day overviews.
     * <p/>
     * Each day of the week gets a separate DayOverview model. The list of TimeSheetRows is split
     * up into separate TimeSheetEntries, which are stored in the correct DayOverview
     *
     * @param overview  XTimeOverview to split up
     * @param startDate The date of the first day of the week
     * @return A list of 7 days
     */
    public static List<DayOverview> weekToDays(XTimeOverview overview, Date startDate) {

        // initialize array of day overview entries for the week, indexed by Calendar.DAY_OF_WEEK
        SparseArray<DayOverview> dailyHoursArray = new SparseArray<>();
        for (int i = 0; i < DAILY_INDEXES.length; i++) {
            Date date = new Date(startDate.getTime() + i * DateUtils.DAY_IN_MILLIS);
            DayOverview dayOverview = new DayOverview(date, overview.getProjects(), true);
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
                dayOverview.setEditable(!timeCell.isApproved());

                // increment total hours
                dayOverview.setTotalHours(dayOverview.getTotalHours() + timeCell.getHours());

                dailyHoursArray.put(entryCal.get(Calendar.DAY_OF_WEEK), dayOverview);
            }
        }

        // return list of totalHours, ordered by date
        List<DayOverview> result = new ArrayList<>();
        for (int day : DAILY_INDEXES) {
            result.add(dailyHoursArray.get(day));
        }

        return result;
    }
}

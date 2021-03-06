package com.xebia.xtime.weekoverview;

import android.database.Cursor;
import android.text.format.DateUtils;
import android.util.SparseArray;

import com.xebia.xtime.content.XTimeContract.Tasks;
import com.xebia.xtime.content.XTimeContract.TimeEntries;
import com.xebia.xtime.shared.model.DayOverview;
import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.Task;
import com.xebia.xtime.shared.model.TimeEntry;
import com.xebia.xtime.shared.model.WorkType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public final class WeekOverviewUtils {

    public static final int[] DAILY_INDEXES = new int[]{Calendar.MONDAY, Calendar.TUESDAY,
            Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY,
            Calendar.SUNDAY};

    private WeekOverviewUtils() {
        // do not instantiate
    }

    /**
     * Aggregates a list of TimeCells into separate DayOverview objects,
     * one for each day of the week.
     *
     * @param timeEntries TimeEntry list to aggregateTimeCells
     * @param startDate   The date of the first day of the week
     * @return A list of 7 day overviews
     */
    public static List<DayOverview> aggregate(List<TimeEntry> timeEntries, Date startDate) {

        // initialize array of day overview entries for the week, indexed by Calendar.DAY_OF_WEEK
        SparseArray<DayOverview.Builder> dailyHoursArray = new SparseArray<>();
        for (int i = 0; i < DAILY_INDEXES.length; i++) {
            Date date = new Date(startDate.getTime() + i * DateUtils.DAY_IN_MILLIS);
            dailyHoursArray.put(DAILY_INDEXES[i], new DayOverview.Builder().setDate(date));
        }

        // group the time entries by day of the week
        Calendar entryCal = Calendar.getInstance();
        entryCal.setTimeZone(TimeZone.getTimeZone("CET"));
        boolean editable = true;
        for (TimeEntry timeEntry : timeEntries) {
            entryCal.setTime(timeEntry.getEntryDate());
            dailyHoursArray.get(entryCal.get(Calendar.DAY_OF_WEEK)).addTimeEntry(timeEntry);
            editable &= !timeEntry.isFromAfas();
        }

        // return list of day overviews, ordered by date
        List<DayOverview> result = new ArrayList<>();
        for (int day : DAILY_INDEXES) {
            DayOverview.Builder builder = dailyHoursArray.get(day);
            if (builder.getTimeEntries().size() == 0) {
                // special case to determine if the day locked without time entries
                Calendar startCal = Calendar.getInstance();
                startCal.setTime(startDate);
                entryCal.setTime(builder.getDate());
                if (entryCal.get(Calendar.MONTH) == startCal.get(Calendar.MONTH)) {
                    builder.setEditable(editable);
                }
            }
            result.add(builder.build());
        }

        return result;
    }

    public static List<TimeEntry> cursorToTimeCells(final Cursor cursor) {
        List<TimeEntry> timeEntries = new ArrayList<>();
        cursor.moveToFirst();
        long lastTaskId = -1;
        Task task = null;
        while (!cursor.isAfterLast()) {
            // task details
            long taskId = cursor.getLong(cursor.getColumnIndex(TimeEntries.TASK_ID));
            if (taskId != lastTaskId) {
                task = new Task.Builder()
                        .setDescription(cursor.getString(cursor.getColumnIndex(Tasks.DESCRIPTION)))
                        .setProject(new Project(
                                cursor.getString(cursor.getColumnIndex(Tasks.PROJECT_ID)),
                                cursor.getString(cursor.getColumnIndex(Tasks.PROJECT_NAME))))
                        .setWorkType(new WorkType(
                                cursor.getString(cursor.getColumnIndex(Tasks.WORKTYPE_ID)),
                                cursor.getString(cursor.getColumnIndex(
                                        Tasks.WORKTYPE_DESCRIPTION))))
                        .build();
                lastTaskId = taskId;
            }

            // time entry details
            double hours = cursor.getDouble(cursor.getColumnIndex(TimeEntries.HOURS));
            boolean approved = cursor.getLong(cursor.getColumnIndex(TimeEntries.APPROVED)) == 1;
            long entryDate = cursor.getLong(cursor.getColumnIndex(TimeEntries.ENTRY_DATE));
            timeEntries.add(new TimeEntry(task, new Date(entryDate), hours, approved));

            cursor.moveToNext();
        }
        return timeEntries;
    }
}

package com.xebia.xtime.weekoverview;

import android.text.format.DateUtils;
import android.util.SparseArray;

import com.xebia.xtime.weekoverview.model.DailyHours;
import com.xebia.xtime.weekoverview.model.TimeCell;
import com.xebia.xtime.weekoverview.model.TimeSheetRow;
import com.xebia.xtime.weekoverview.model.WeekOverview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeSheetUtils {

    private static final int[] DAILY_INDEXES = new int[]{Calendar.MONDAY, Calendar.TUESDAY,
            Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY,
            Calendar.SUNDAY};

    public static List<DailyHours> dailyHours(WeekOverview overview, Date startDate) {

        // initialize array of entries for the week, indexed by Calendar.DAY_OF_WEEK
        SparseArray<DailyHours> dailyHoursArray = new SparseArray<DailyHours>();
        for (int i = 0; i < DAILY_INDEXES.length; i++) {
            Date date = new Date(startDate.getTime() + i * DateUtils.DAY_IN_MILLIS);
            dailyHoursArray.put(DAILY_INDEXES[i], new DailyHours(date, 0));
        }

        // fill the array with data from the overview
        for (TimeSheetRow row : overview.getTimeSheetRows()) {
            for (TimeCell timeCell : row.getTimeCells()) {
                // get daily hours from array
                Calendar entryCal = Calendar.getInstance();
                entryCal.setTime(timeCell.getEntryDate());
                DailyHours dayHours = dailyHoursArray.get(entryCal.get(Calendar.DAY_OF_WEEK));

                // increment daily hours
                dayHours.hours += timeCell.getHour();
                dailyHoursArray.put(entryCal.get(Calendar.DAY_OF_WEEK), dayHours);
            }
        }

        // return list of hours, ordered by date
        List<DailyHours> result = new ArrayList<DailyHours>();
        for (int day : DAILY_INDEXES) {
            result.add(dailyHoursArray.get(day));
        }

        return result;
    }

}

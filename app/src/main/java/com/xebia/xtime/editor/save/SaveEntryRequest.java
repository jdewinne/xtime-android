package com.xebia.xtime.editor.save;

import com.xebia.xtime.shared.XTimeRequest;
import com.xebia.xtime.shared.model.TimeEntry;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class SaveEntryRequest extends XTimeRequest {

    private static final String XTIME_URL = "https://xtime.xebia.com/xtime/entryform.html";
    private final TimeEntry mTimeEntry;

    public SaveEntryRequest(TimeEntry timeEntry) {
        mTimeEntry = timeEntry;
    }

    @Override
    public String getContentType() {
        return CONTENT_TYPE_FORM;
    }

    @Override
    public String getRequestData() {

        String data = "";

        // start date and end date of the week
        Calendar cal = Calendar.getInstance();
        cal.setTime(mTimeEntry.getEntryDate());
        cal.setTimeZone(TimeZone.getTimeZone("CET")); // use central european time
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DAY_OF_WEEK, -1);
        }
        DateFormat dateFormat = new SimpleDateFormat("d+MMM+yyyy", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("CET"));
        data += "startDate=" + dateFormat.format(cal.getTime());
        cal.add(Calendar.DAY_OF_YEAR, 6);
        data += "&endDate=" + dateFormat.format(cal.getTime());

        // week dates
        cal.add(Calendar.DAY_OF_YEAR, -6);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("CET"));
        for (int i = 0; i < 7; i++) {
            data += "&weekDates=" + dateFormat.format(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        // time sheet entry info
        Calendar entryCal = Calendar.getInstance();
        entryCal.setTime(mTimeEntry.getEntryDate());
        entryCal.setTimeZone(TimeZone.getTimeZone("CET")); // use central european time
        int entryWeekDay = entryCal.get(Calendar.DAY_OF_WEEK);
        String description = "";
        try {
            description = URLEncoder.encode(mTimeEntry.getTask().getDescription(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // should never happen
        }
        String hours = NumberFormat.getNumberInstance().format(mTimeEntry.getHours());
        data += "&projectId=" + mTimeEntry.getTask().getProject().getId() +
                "&workType=" + mTimeEntry.getTask().getWorkType().getId() +
                "&description=" + description +
                "&monday=" + (entryWeekDay == Calendar.MONDAY ? hours : "") +
                "&tuesday=" + (entryWeekDay == Calendar.TUESDAY ? hours : "") +
                "&wednesday=" + (entryWeekDay == Calendar.WEDNESDAY ? hours : "") +
                "&thursday=" + (entryWeekDay == Calendar.THURSDAY ? hours : "") +
                "&friday=" + (entryWeekDay == Calendar.FRIDAY ? hours : "") +
                "&saturday=" + (entryWeekDay == Calendar.SATURDAY ? hours : "") +
                "&sunday=" + (entryWeekDay == Calendar.SUNDAY ? hours : "") +
                "&weekTotal1=" + hours;

        // complete the time sheet with four empty rows
        for (int i = 0; i < 4; i++) {
            data += "&projectId=&workType=&description=" +
                    "&monday=&tuesday=&wednesday=&thursday=&friday=&saturday=&sunday=" +
                    "&weekTotal1=";
        }

        // day totals
        data += "&dayTotal1=" + (entryWeekDay == Calendar.MONDAY ? hours : "") +
                "&dayTotal2=" + (entryWeekDay == Calendar.TUESDAY ? hours : "") +
                "&dayTotal3=" + (entryWeekDay == Calendar.WEDNESDAY ? hours : "") +
                "&dayTotal4=" + (entryWeekDay == Calendar.THURSDAY ? hours : "") +
                "&dayTotal5=" + (entryWeekDay == Calendar.FRIDAY ? hours : "") +
                "&dayTotal6=" + (entryWeekDay == Calendar.SATURDAY ? hours : "") +
                "&dayTotal7=" + (entryWeekDay == Calendar.SUNDAY ? hours : "") +
                "&grandTotal=" + hours +
                "&buttonClicked=save";

        return data;
    }

    @Override
    public String getUrl() {
        return XTIME_URL;
    }
}

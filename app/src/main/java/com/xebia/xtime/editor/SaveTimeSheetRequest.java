package com.xebia.xtime.editor;

import android.util.Log;

import com.xebia.xtime.shared.XTimeRequest;
import com.xebia.xtime.shared.model.TimeSheetEntry;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class SaveTimeSheetRequest extends XTimeRequest {

    private static final String XTIME_URL = "https://xtime.xebia.com/xtime/entryform.html";
    private static final String TAG = "SaveTimeSheetRequest";
    private final TimeSheetEntry mTimeSheetEntry;

    public SaveTimeSheetRequest(TimeSheetEntry timeSheetEntry) {
        mTimeSheetEntry = timeSheetEntry;
    }

    /**
     * Submits the request.
     *
     * @return <code>true</code> if save was successful, <code>false</code> if the save was
     * denied, or <code>null</code> if the request failed for another reason.
     */
    public Boolean submit() {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(XTIME_URL);

            // blindly trust all certificates
            trustAllCertificates();

            urlConnection = (HttpURLConnection) url.openConnection();

            // do not follow redirects, we use the Location header to see if the request was OK
            urlConnection.setInstanceFollowRedirects(false);

            // write request data
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            writeCredentials(out);

            // request was successful if the Location header does not redirect to an error page
            String location = urlConnection.getHeaderField("Location");
            return location != null && !location.contains("error=true");

        } catch (IOException e) {
            Log.w(TAG, "Save request failed", e);
            return null;
        } catch (GeneralSecurityException e) {
            Log.e(TAG, "Security problem performing request", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    public String getRequestData() throws UnsupportedEncodingException {

        String data = "";

        // start date and end date of the week
        Calendar cal = Calendar.getInstance();
        cal.setTime(mTimeSheetEntry.getTimeCell().getEntryDate());
        cal.setTimeZone(TimeZone.getTimeZone("CET")); // use central european time
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DAY_OF_WEEK, -1);
        }
        DateFormat dateFormat = new SimpleDateFormat("d+MMM+yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("CET"));
        data += "startDate=" + dateFormat.format(cal.getTime());
        cal.add(Calendar.DAY_OF_YEAR, 6);
        data += "&endDate=" + dateFormat.format(cal.getTime());

        // week dates
        cal.add(Calendar.DAY_OF_YEAR, -6);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("CET"));
        for (int i = 0; i < 7; i++) {
            data += "&weekDates=" + dateFormat.format(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        // time sheet entry info
        Calendar entryCal = Calendar.getInstance();
        entryCal.setTime(mTimeSheetEntry.getTimeCell().getEntryDate());
        entryCal.setTimeZone(TimeZone.getTimeZone("CET")); // use central european time
        int entryWeekDay = entryCal.get(Calendar.DAY_OF_WEEK);
        String hours = NumberFormat.getNumberInstance().format(mTimeSheetEntry.getTimeCell
                ().getHours());
        data += "&projectId=" + mTimeSheetEntry.getProject().getId() +
                "&workType=" + mTimeSheetEntry.getWorkType().getId() +
                "&description=" + URLEncoder.encode(mTimeSheetEntry.getDescription(), "UTF-8") +
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

    private void writeCredentials(OutputStream out) throws IOException {
        String data = getRequestData();
        out.write(data.getBytes());
        out.flush();
        out.close();
    }
}

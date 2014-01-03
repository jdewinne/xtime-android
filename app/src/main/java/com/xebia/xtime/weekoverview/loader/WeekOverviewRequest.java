package com.xebia.xtime.weekoverview.loader;

import android.util.Log;

import com.xebia.xtime.shared.XTimeRequest;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class WeekOverviewRequest extends XTimeRequest {

    private static final String URL = "https://xtime.xebia.com" +
            "/xtime/dwr/call/plaincall/TimeEntryServiceBean.getWeekOverview.dwr";
    private static final String TAG = "WeekOverviewRequest";
    private final Date mDate;

    /**
     * Constructor.
     *
     * @param date Date of the first day of the week that will be requested.
     */
    public WeekOverviewRequest(Date date) {
        mDate = date;
    }

    /**
     * Submits the request.
     *
     * @return Response content, or null if the request failed.
     */
    public String submit() {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(URL);

            // yeah...
            trustAllCertificates();

            urlConnection = (HttpURLConnection) url.openConnection();

            // do not follow redirects, we use the Location header to see if the request was OK
            urlConnection.setInstanceFollowRedirects(false);

            // write request data
            urlConnection.setDoOutput(true);
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            writeStream(out);

            // check if the request was OK
            String location = urlConnection.getHeaderField("Location");
            if (null != location && location.contains("error=true")) {
                Log.w(TAG, "Request denied");
                return null;
            }

            // read response data
            InputStream in = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in), 8196);
            String line;
            StringBuilder responseContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }

            return responseContent.toString();

        } catch (IOException e) {
            Log.w(TAG, "Request failed", e);
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

    public String getRequestData() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("CET"));
        return "callCount=1\n" +
                "page=/xtime/entryform.html\n" +
                "httpSessionId=\n" +
                "scriptSessionId=\n" +
                "c0-scriptName=TimeEntryServiceBean\n" +
                "c0-methodName=getWeekOverview\n" +
                "c0-id=0\n" + // only used for JSONP callback
                "c0-param0=string:" + dateFormat.format(mDate) + "\n" +
                "c0-param1=boolean:true\n" + // not used?
                "batchId=0\n"; // only used for JSONP callback
    }

    private void writeStream(OutputStream out) throws IOException {
        out.write(getRequestData().getBytes());
        out.flush();
        out.close();
    }
}

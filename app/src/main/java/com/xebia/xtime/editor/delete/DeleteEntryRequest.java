package com.xebia.xtime.editor.delete;

import android.util.Log;

import com.xebia.xtime.shared.XTimeRequest;
import com.xebia.xtime.shared.model.TimeSheetEntry;

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
import java.util.Locale;
import java.util.TimeZone;

public class DeleteEntryRequest extends XTimeRequest {

    private static final String XTIME_URL = "https://xtime.xebia.com" +
            "/xtime/dwr/call/plaincall/TimeEntryServiceBean.deleteTimeSheetEntries.dwr";
    private static final String TAG = "DeleteEntryRequest";
    private final TimeSheetEntry mEntry;

    public DeleteEntryRequest(TimeSheetEntry entry) {
        mEntry = entry;
    }

    /**
     * Submits the request.
     *
     * @return <code>true</code> if save was successful, <code>false</code> if the save was
     * denied, or <code>null</code> if the request failed for another reason.
     */
    public String submit() {
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
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            writeCredentials(out);

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

    public String getRequestData() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("CET"));
        return "callCount=1\n" +
                "page=/xtime/entryform.html\n" +
                "httpSessionId=\n" +
                "scriptSessionId=\n" +
                "c0-scriptName=TimeEntryServiceBean\n" +
                "c0-methodName=deleteTimeSheetEntries\n" +
                "c0-id=0\n" +
                "c0-param0=string:" + mEntry.getProject().getId() + "\n" +
                "c0-param1=string:" + mEntry.getWorkType().getId() + "\n" +
                "c0-param2=string:" + mEntry.getDescription() + "\n" +
                "c0-param3=string:" + dateFormat.format(mEntry.getTimeCell().getEntryDate()) +
                "\n" +
                "batchId=0";
    }

    private void writeCredentials(OutputStream out) throws IOException {
        String data = getRequestData();
        out.write(data.getBytes());
        out.flush();
        out.close();
    }
}

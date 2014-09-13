package com.xebia.xtime.sync;

import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.os.RemoteException;
import android.util.Log;

import com.xebia.xtime.content.XTimeContract.TimeEntries;
import com.xebia.xtime.content.XTimeContract.TimeSheetRows;
import com.xebia.xtime.shared.model.TimeCell;
import com.xebia.xtime.shared.model.TimeSheetRow;
import com.xebia.xtime.shared.model.XTimeOverview;
import com.xebia.xtime.webservice.XTimeWebService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class SyncHelper {

    /**
     * The first time sheet to sync data for is 2 month old.
     */
    public static final int OLDEST_WEEK = -2;
    /**
     * The last time sheet to sync data for is 1 month in the future.
     */
    public static final int NEWEST_MONTH = 1;
    private static final String TAG = "SyncHelper";

    public void performSync(final String cookie, final ContentProviderClient provider,
                            final SyncResult syncResult) throws CookieExpiredException {
        try {
            List<XTimeOverview> overviews = new ArrayList<>();
            for (int offset = OLDEST_WEEK; offset <= NEWEST_MONTH; offset++) {
                XTimeOverview overview = requestMonthOverview(cookie, offset);
                if (null != overview) {
                    Log.d(TAG, "Parsed overview");
                    overviews.add(overview);
                } else {
                    Log.w(TAG, "Parse failed");
                    syncResult.stats.numParseExceptions++;
                }
            }
            storeTimeSheets(overviews, provider);
        } catch (IOException e) {
            Log.w(TAG, "Failed to sync! Connection error: '" + e.getMessage() + "'");
            syncResult.stats.numIoExceptions++;
        } catch (RemoteException | OperationApplicationException e) {
            Log.w(TAG, "Failed to store data! RemoteException: '" + e.getMessage() + "'");
            syncResult.stats.numParseExceptions++;
        }
    }

    private int storeTimeSheets(final List<XTimeOverview> overviews,
                                final ContentProviderClient provider) throws
            RemoteException, OperationApplicationException {
        Log.d(TAG, "Store time sheets");

        ArrayList<ContentProviderOperation> batch = new ArrayList<>();

        batch.add(ContentProviderOperation.newDelete(TimeSheetRows.CONTENT_URI).build());
        batch.add(ContentProviderOperation.newDelete(TimeEntries.CONTENT_URI).build());

        for (XTimeOverview overview : overviews) {

            // insert the time sheet rows with back reference to the time sheet ID
            for (TimeSheetRow sheetRow : overview.getTimeSheetRows()) {
                batch.add(ContentProviderOperation.newInsert(TimeSheetRows.CONTENT_URI)
                        .withValue(TimeSheetRows.DESCRIPTION, sheetRow.getDescription())
                        .withValue(TimeSheetRows.PROJECT_ID, sheetRow.getProject().getId())
                        .withValue(TimeSheetRows.PROJECT_NAME, sheetRow.getProject().getName())
                        .withValue(TimeSheetRows.WORKTYPE_ID, sheetRow.getWorkType().getId())
                        .withValue(TimeSheetRows.WORKTYPE_NAME,
                                sheetRow.getWorkType().getDescription())
                        .build());

                // insert the time entries with back reference to the time sheet row ID
                int indexOfTimeSheetRow = batch.size() - 1;
                for (TimeCell timeEntry : sheetRow.getTimeCells()) {
                    batch.add(ContentProviderOperation.newInsert(TimeEntries.CONTENT_URI)
                            .withValue(TimeEntries.APPROVED, timeEntry.isApproved())
                            .withValue(TimeEntries.ENTRY_DATE, timeEntry.getEntryDate().getTime())
                            .withValue(TimeEntries.HOURS, timeEntry.getHours())
                            .withValueBackReference(TimeEntries.SHEET_ROW_ID, indexOfTimeSheetRow)
                            .build());
                }
            }
        }
        provider.applyBatch(batch);
        return batch.size();
    }

    private XTimeOverview requestMonthOverview(final String cookie, int monthOffset)
            throws CookieExpiredException, IOException {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthOffset);
        calendar.setTimeZone(TimeZone.getTimeZone("CET"));

        String response = XTimeWebService.getInstance().getMonthOverview(calendar.getTime(),
                cookie);
        if (response.contains("UsernameNotFoundException")) {
            throw new CookieExpiredException("UsernameNotFoundException");
        }
        return XTimeOverviewParser.parse(response, year, week);
    }

    static class CookieExpiredException extends Exception {
        public CookieExpiredException(String message) {
            super(message);
        }
    }
}

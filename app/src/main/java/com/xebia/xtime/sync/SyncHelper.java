package com.xebia.xtime.sync;

import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.os.RemoteException;
import android.util.Log;

import com.xebia.xtime.content.XTimeContract.Tasks;
import com.xebia.xtime.content.XTimeContract.TimeEntries;
import com.xebia.xtime.shared.model.Task;
import com.xebia.xtime.shared.model.TimeEntry;
import com.xebia.xtime.shared.model.XTimeOverview;
import com.xebia.xtime.webservice.XTimeWebService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class SyncHelper {

    /**
     * The first time sheet to sync data for is 2 month old.
     */
    public static final int OLDEST_MONTH = -1;
    /**
     * The last time sheet to sync data for is 1 month in the future.
     */
    public static final int NEWEST_MONTH = 0;
    private static final String TAG = "SyncHelper";

    public void performSync(final String cookie, final ContentProviderClient provider,
                            final SyncResult syncResult) throws CookieExpiredException {
        try {
            List<XTimeOverview> overviews = new ArrayList<>();
            for (int offset = OLDEST_MONTH; offset <= NEWEST_MONTH; offset++) {
                XTimeOverview overview = requestMonthOverview(cookie, offset);
                if (null != overview) {
                    Log.d(TAG, "Parsed overview");
                    overviews.add(overview);
                } else {
                    Log.w(TAG, "Parse failed");
                    syncResult.stats.numParseExceptions++;
                }
            }
            // concat all the time cells
            List<TimeEntry> allTheTimeInTheWorld = new ArrayList<>();
            for (XTimeOverview overview : overviews) {
                allTheTimeInTheWorld.addAll(overview.getTimeEntries());
            }
            storeTimeCells(allTheTimeInTheWorld, provider);
        } catch (IOException e) {
            Log.w(TAG, "Failed to sync! Connection error: '" + e.getMessage() + "'");
            syncResult.stats.numIoExceptions++;
        } catch (RemoteException | OperationApplicationException e) {
            Log.w(TAG, "Failed to store data! RemoteException: '" + e.getMessage() + "'");
            syncResult.stats.numParseExceptions++;
        }
    }

    private int storeTimeCells(final List<TimeEntry> timeEntries,
                                final ContentProviderClient provider) throws
            RemoteException, OperationApplicationException {
        Log.d(TAG, "Store time sheets");

        // group all the time cells by task (project, work type)
        Map<Task, List<TimeEntry>> groupedByTask = new HashMap<>();
        List<TimeEntry> timeEntryGroup;
        for (TimeEntry timeEntry : timeEntries) {
            timeEntryGroup = groupedByTask.get(timeEntry.getTask());
            if (null == timeEntryGroup) {
                timeEntryGroup = new ArrayList<>();
            }
            timeEntryGroup.add(timeEntry);
            groupedByTask.put(timeEntry.getTask(), timeEntryGroup);
        }

        // collect the operation in a giant batch
        ArrayList<ContentProviderOperation> batch = new ArrayList<>();

        // remove all local data
        batch.add(ContentProviderOperation.newDelete(TimeEntries.CONTENT_URI).build());
        batch.add(ContentProviderOperation.newDelete(Tasks.CONTENT_URI).build());

        for (Map.Entry<Task, List<TimeEntry>> entry : groupedByTask.entrySet()) {
            final Task task = entry.getKey();
            batch.add(ContentProviderOperation.newInsert(Tasks.CONTENT_URI)
                    .withValue(Tasks.DESCRIPTION, task.getDescription())
                    .withValue(Tasks.PROJECT_ID, task.getProject().getId())
                    .withValue(Tasks.PROJECT_NAME, task.getProject().getName())
                    .withValue(Tasks.WORKTYPE_ID, task.getWorkType().getId())
                    .withValue(Tasks.WORKTYPE_NAME, task.getWorkType().getDescription())
                    .build());
            int taskIndex = batch.size() - 1;

            for (final TimeEntry timeEntry : entry.getValue()) {
                batch.add(ContentProviderOperation.newInsert(TimeEntries.CONTENT_URI)
                        .withValue(TimeEntries.HOURS, timeEntry.getHours())
                        .withValue(TimeEntries.ENTRY_DATE, timeEntry.getEntryDate().getTime())
                        .withValue(TimeEntries.APPROVED, timeEntry.isFromAfas())
                        .withValueBackReference(TimeEntries.TASK_ID, taskIndex)
                        .build());
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
        return DwrOverviewParser.parse(response);
    }

    static class CookieExpiredException extends Exception {
        public CookieExpiredException(String message) {
            super(message);
        }
    }
}

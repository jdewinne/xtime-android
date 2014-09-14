package com.xebia.xtime.sync;

import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.os.RemoteException;
import android.text.format.DateUtils;
import android.util.Log;

import com.xebia.xtime.content.XTimeContract.Projects;
import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.XTimeOverview;
import com.xebia.xtime.webservice.XTimeWebService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectSyncHelper {

    private static final int OLDEST_WEEK = -4;
    private static final int NEWEST_WEEK = 1;
    private static final String TAG = "ProjectSyncHelper";

    public void performSync(final String cookie, final ContentProviderClient provider,
                            final SyncResult syncResult) throws CookieExpiredException {
        try {
            List<XTimeOverview> overviews = new ArrayList<>();
            for (int offset = OLDEST_WEEK; offset <= NEWEST_WEEK; offset++) {
                XTimeOverview overview = requestWeekOverview(cookie, offset);
                if (null != overview) {
                    Log.d(TAG, "Parsed overview");
                    overviews.add(overview);
                } else {
                    Log.w(TAG, "Parse failed");
                    syncResult.stats.numParseExceptions++;
                }
            }
            // concat all the projects
            List<Project> allProjects = new ArrayList<>();
            for (XTimeOverview overview : overviews) {
                for (Project project : overview.getProjects()) {
                    if (!allProjects.contains(project)) {
                        allProjects.add(project);
                    }
                }
            }
            storeProjects(allProjects, provider);
        } catch (IOException e) {
            Log.w(TAG, "Failed to sync! Connection error: '" + e.getMessage() + "'");
            syncResult.stats.numIoExceptions++;
        } catch (RemoteException | OperationApplicationException e) {
            Log.w(TAG, "Failed to store data! RemoteException: '" + e.getMessage() + "'");
            syncResult.stats.numParseExceptions++;
        }
    }

    private int storeProjects(final List<Project> projects, final ContentProviderClient provider)
            throws RemoteException, OperationApplicationException {
        Log.d(TAG, "Store projects");
        ArrayList<ContentProviderOperation> batch = new ArrayList<>();
        batch.add(ContentProviderOperation.newDelete(Projects.CONTENT_URI).build());
        for (Project project : projects) {
            batch.add(ContentProviderOperation.newInsert(Projects.CONTENT_URI)
                    .withValue(Projects.ID, project.getId())
                    .withValue(Projects.NAME, project.getName())
                    .build());
        }
        provider.applyBatch(batch);
        return batch.size();
    }

    private XTimeOverview requestWeekOverview(final String cookie, int weekOffset)
            throws CookieExpiredException, IOException {
        Date week = new Date(System.currentTimeMillis() + weekOffset * DateUtils.WEEK_IN_MILLIS);
        String response = XTimeWebService.getInstance().getWeekOverview(week, cookie);
        if (response.contains("UsernameNotFoundException")) {
            throw new CookieExpiredException("UsernameNotFoundException");
        }
        return DwrOverviewParser.parse(response);
    }
}

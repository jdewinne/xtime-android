package com.xebia.xtime.monthoverview;

import android.database.Cursor;

import com.xebia.xtime.content.XTimeContract.Tasks;
import com.xebia.xtime.content.XTimeContract.TimeEntries;
import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.Task;
import com.xebia.xtime.shared.model.TimeEntry;
import com.xebia.xtime.shared.model.WorkType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MonthOverviewUtils {

    private MonthOverviewUtils() {
        // do not instantiate
    }

    public static List<TaskOverview> aggregateTimeCells(final Cursor cursor) {
        cursor.moveToFirst();
        Map<Long, TaskOverview> overviewMap = new HashMap<>();
        Task task = null;
        while (!cursor.isAfterLast()) {
            // task details
            long taskId = cursor.getLong(cursor.getColumnIndex(TimeEntries.TASK_ID));
            TaskOverview overview = overviewMap.get(taskId);
            if (null == overview) {
                task = new Task.Builder()
                        .setDescription(
                                cursor.getString(cursor.getColumnIndex(Tasks.DESCRIPTION)))
                        .setProject(new Project(
                                cursor.getString(cursor.getColumnIndex(Tasks.PROJECT_ID)),
                                cursor.getString(cursor.getColumnIndex(Tasks.PROJECT_NAME))))
                        .setWorkType(new WorkType(
                                cursor.getString(cursor.getColumnIndex(Tasks.WORKTYPE_ID)),
                                cursor.getString(cursor.getColumnIndex(Tasks.WORKTYPE_NAME))))
                        .build();
                overview = new TaskOverview(task);
            }

            // time entry details
            double hours = cursor.getDouble(cursor.getColumnIndex(TimeEntries.HOURS));
            boolean approved = cursor.getLong(cursor.getColumnIndex(TimeEntries.APPROVED)) == 1;
            long entryDate = cursor.getLong(cursor.getColumnIndex(TimeEntries.ENTRY_DATE));
            overview.getTimeEntries().add(new TimeEntry(task, new Date(entryDate), hours, approved));

            overviewMap.put(taskId, overview);

            cursor.moveToNext();
        }
        return new ArrayList<>(overviewMap.values());
    }

    public static double getGrandTotalHours(final List<TaskOverview> taskOverviews) {
        double total = 0;
        for (TaskOverview taskOverview : taskOverviews) {
            total += taskOverview.getTotalHours();
        }
        return total;
    }
}

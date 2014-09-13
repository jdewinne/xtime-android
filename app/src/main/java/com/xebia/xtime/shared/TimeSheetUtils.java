package com.xebia.xtime.shared;

import android.database.Cursor;

import com.xebia.xtime.content.XTimeContract;
import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.TimeCell;
import com.xebia.xtime.shared.model.TimeSheetRow;
import com.xebia.xtime.shared.model.WorkType;
import com.xebia.xtime.shared.model.XTimeOverview;

import java.util.Date;

public final class TimeSheetUtils {

    private TimeSheetUtils() {
        // do not instantiate
    }

    public static XTimeOverview cursorToOverview(final Cursor cursor) {
        cursor.moveToFirst();
        long lastSheetRowId = -1;
        XTimeOverview.Builder overviewBuilder = new XTimeOverview.Builder();
        TimeSheetRow.Builder rowBuilder = null;
        while (!cursor.isAfterLast()) {
            // time sheet row details
            long sheetRowId = cursor.getLong(cursor.getColumnIndex(XTimeContract.TimeEntries
                    .SHEET_ROW_ID));
            if (sheetRowId != lastSheetRowId) {
                if (null != rowBuilder) {
                    overviewBuilder.addTimeSheetRow(rowBuilder.build());
                }
                rowBuilder = new TimeSheetRow.Builder()
                        .setDescription(
                                cursor.getString(cursor.getColumnIndex(XTimeContract
                                        .TimeSheetRows.DESCRIPTION)))
                        .setProject(new Project(
                                cursor.getString(cursor.getColumnIndex(XTimeContract
                                        .TimeSheetRows.PROJECT_ID)),
                                cursor.getString(cursor.getColumnIndex(
                                        XTimeContract.TimeSheetRows.PROJECT_NAME))))
                        .setWorkType(new WorkType(
                                cursor.getString(cursor.getColumnIndex(XTimeContract
                                        .TimeSheetRows.WORKTYPE_ID)),
                                cursor.getString(cursor.getColumnIndex(
                                        XTimeContract.TimeSheetRows.WORKTYPE_NAME))));
                lastSheetRowId = sheetRowId;
            }

            // time entry details
            double hours = cursor.getDouble(cursor.getColumnIndex(XTimeContract.TimeEntries.HOURS));
            boolean approved = cursor.getLong(cursor.getColumnIndex(XTimeContract.TimeEntries
                    .APPROVED)) == 1;
            long entryDate = cursor.getLong(cursor.getColumnIndex(XTimeContract.TimeEntries
                    .ENTRY_DATE));
            if (null != rowBuilder) {
                rowBuilder.addTimeCell(new TimeCell(new Date(entryDate), hours, approved));
            }

            cursor.moveToNext();
        }
        // make sure the last row is also added to the sheet
        if (null != rowBuilder) {
            overviewBuilder.addTimeSheetRow(rowBuilder.build());
        }
        return overviewBuilder.build();
    }
}

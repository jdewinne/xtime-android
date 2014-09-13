package com.xebia.xtime.content;

import android.net.Uri;
import android.provider.BaseColumns;

import com.xebia.xtime.content.XTimeDatabase.Tables;

public final class XTimeContract {

    public static final String QUERY_PARAMETER_DISTINCT = "distinct";
    public static final String CONTENT_AUTHORITY = "com.xebia.xtime.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private XTimeContract() {
        // do not instantiate
    }

    interface TimeSheetRowColumns extends BaseColumns {
        public static final String DESCRIPTION = "description";
        public static final String PROJECT_ID = "project_id";
        public static final String PROJECT_NAME = "project_name";
        public static final String WORKTYPE_ID = "worktype_id";
        public static final String WORKTYPE_NAME = "worktype_name";
    }

    interface TimeEntryColumns extends BaseColumns {
        public static final String HOURS = "hours";
        public static final String APPROVED = "approved";
        public static final String ENTRY_DATE = "entry_date";
        public static final String SHEET_ROW_ID = "sheet_row_id";
    }

    public static class TimeSheetRows implements TimeSheetRowColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(Tables.TIME_SHEET_ROWS).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.xtime.time_sheet_row";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.xtime.time_sheet_row";

        /**
         * Build {@link Uri} for requested time sheet row ID.
         */
        public static Uri buildUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
        }
    }

    public static class TimeEntries implements TimeEntryColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(Tables.TIME_ENTRIES).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.xtime.time_entry";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.xtime.time_entry";

        /**
         * Build {@link Uri} for requested time cell ID.
         */
        public static Uri buildUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
        }
    }
}

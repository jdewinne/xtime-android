package com.xebia.xtime.content;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.xebia.xtime.content.XTimeDatabase.Tables;

public final class XTimeContract {

    public static final String QUERY_PARAMETER_DISTINCT = "distinct";
    public static final String CONTENT_AUTHORITY = "com.xebia.xtime.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private XTimeContract() {
        // do not instantiate
    }

    public static Uri addCallerIsSyncAdapterParameter(Uri uri) {
        return uri.buildUpon().appendQueryParameter(
                ContactsContract.CALLER_IS_SYNCADAPTER, "true").build();
    }

    public static boolean hasCallerIsSyncAdapterParameter(Uri uri) {
        return TextUtils.equals("true",
                uri.getQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER));
    }

    interface ProjectColumns extends BaseColumns {
        public static final String ID = "project_id";
        public static final String NAME = "name";
    }

    interface TaskColumns extends BaseColumns {
        public static final String DESCRIPTION = "description";
        public static final String PROJECT_ID = "project_id";
        public static final String PROJECT_NAME = "project_name";
        public static final String WORKTYPE_ID = "worktype_id";
        public static final String WORKTYPE_DESCRIPTION = "worktype_description";
    }

    interface TimeEntryColumns extends BaseColumns {
        public static final String HOURS = "hours";
        public static final String APPROVED = "approved";
        public static final String ENTRY_DATE = "entry_date";
        public static final String TASK_ID = "task_id";
    }

    public static class Projects implements ProjectColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(Tables.PROJECTS)
                .build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.xtime.project";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.xtime.project";

        /**
         * Build {@link Uri} for requested project ID.
         */
        public static Uri buildUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        /**
         * Read {@link #ID} from {@link Projects} {@link Uri}.
         */
        public static String getProjectId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Tasks implements TaskColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(Tables.TASKS)
                .build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.xtime.task";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.xtime.task";

        /**
         * Build {@link Uri} for requested task ID.
         */
        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static class TimeEntries implements TimeEntryColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(Tables.TIME_ENTRIES)
                .build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.xtime.time_entry";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.xtime.time_entry";

        /**
         * Build {@link Uri} for requested time entry ID.
         */
        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}

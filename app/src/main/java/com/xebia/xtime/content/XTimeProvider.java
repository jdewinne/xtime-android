package com.xebia.xtime.content;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.xebia.xtime.content.XTimeContract.Projects;
import com.xebia.xtime.content.XTimeContract.Tasks;
import com.xebia.xtime.content.XTimeContract.TimeEntries;
import com.xebia.xtime.content.XTimeDatabase.Tables;

import java.util.Arrays;

public class XTimeProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String TAG = "XTimeProvider";
    private XTimeDatabase mOpenHelper;

    /**
     * Build and return a {@link UriMatcher} that catches all {@link Uri}  variations supported by
     * this {@link ContentProvider}.
     */
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = XTimeContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, Tables.PROJECTS, UriCodes.PROJECTS);
        matcher.addURI(authority, Tables.PROJECTS + "/*", UriCodes.PROJECT);

        matcher.addURI(authority, Tables.TASKS, UriCodes.TASKS);
        matcher.addURI(authority, Tables.TASKS + "/*", UriCodes.TASK);

        matcher.addURI(authority, Tables.TIME_ENTRIES, UriCodes.TIME_ENTRIES);
        matcher.addURI(authority, Tables.TIME_ENTRIES + "/*", UriCodes.TIME_ENTRY);

        return matcher;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v(TAG, "delete(uri=" + uri
                + " selection=" + selection + " args=" + Arrays.toString(selectionArgs) + ")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        int count = builder.where(selection, selectionArgs).delete(db);
        notifyChange(uri);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case UriCodes.PROJECTS:
                return Projects.CONTENT_TYPE;
            case UriCodes.PROJECT:
                return Projects.CONTENT_ITEM_TYPE;
            case UriCodes.TASKS:
                return Tasks.CONTENT_TYPE;
            case UriCodes.TASK:
                return Tasks.CONTENT_ITEM_TYPE;
            case UriCodes.TIME_ENTRIES:
                return TimeEntries.CONTENT_TYPE;
            case UriCodes.TIME_ENTRY:
                return TimeEntries.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Log.v(TAG, "insert(uri=" + uri + ", values=" + values + ")");
        switch (match) {
            case UriCodes.PROJECTS: {
                db.insertOrThrow(Tables.PROJECTS, null, values);
                notifyChange(uri);
                return Projects.buildUri(values.getAsString(Projects.ID));
            }
            case UriCodes.TASKS: {
                long id = db.insertOrThrow(Tables.TASKS, null, values);
                notifyChange(uri);
                return Tasks.buildUri(id);
            }
            case UriCodes.TIME_ENTRIES: {
                long id = db.insertOrThrow(Tables.TIME_ENTRIES, null, values);
                notifyChange(uri);
                return TimeEntries.buildUri(id);
            }
            default:
                throw new UnsupportedOperationException("Unknown insert uri: " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new XTimeDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        Log.v(TAG, "uri=" + uri + " match=" + match + " proj=" + Arrays.toString(projection)
                + " selection=" + selection + " args=" + Arrays.toString(selectionArgs) + ")");
        switch (match) {
            default: {
                // Most cases are handled with simple SelectionBuilder
                final SelectionBuilder builder = buildExpandedSelection(uri, match);

                boolean distinct = !TextUtils.isEmpty(
                        uri.getQueryParameter(XTimeContract.QUERY_PARAMETER_DISTINCT));

                Cursor cursor = builder
                        .where(selection, selectionArgs)
                        .query(db, distinct, projection, sortOrder, null);
                Context context = getContext();
                if (null != context) {
                    cursor.setNotificationUri(context.getContentResolver(), uri);
                }
                return cursor;
            }
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.v(TAG, "update(uri=" + uri + ", values=" + values.toString() + ")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        int count = builder.where(selection, selectionArgs).update(db, values);
        notifyChange(uri);
        return count;
    }

    private void notifyChange(final Uri uri) {
        // We only notify changes if the caller is not the sync adapter.
        // The sync adapter has the responsibility of notifying changes (it can do so
        // more intelligently than we can -- for example, doing it only once at the end
        // of the sync instead of issuing thousands of notifications for each record).
        if (!XTimeContract.hasCallerIsSyncAdapterParameter(uri)) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
    }

    /**
     * Build a simple {@link SelectionBuilder} to match the requested {@link Uri}.
     * This is usually enough to support {@link #insert}, {@link #update},
     * and {@link #delete} operations.
     */
    private SelectionBuilder buildSimpleSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case UriCodes.PROJECTS: {
                return builder.table(Tables.PROJECTS);
            }
            case UriCodes.PROJECT: {
                final String id = Projects.getProjectId(uri);
                return builder.table(Tables.PROJECTS).where(Projects.ID + "=?", id);
            }
            case UriCodes.TASKS: {
                return builder.table(Tables.TASKS);
            }
            case UriCodes.TASK: {
                final String id = Long.toString(ContentUris.parseId(uri));
                return builder.table(Tables.TASKS).where(Tasks._ID + "=?", id);
            }
            case UriCodes.TIME_ENTRIES: {
                return builder.table(Tables.TIME_ENTRIES);
            }
            case UriCodes.TIME_ENTRY: {
                final String id = Long.toString(ContentUris.parseId(uri));
                return builder.table(Tables.TIME_ENTRIES).where(TimeEntries._ID + "=?", id);
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri for " + match + ": " + uri);
            }
        }
    }

    /**
     * Build an advanced {@link SelectionBuilder} to match the requested {@link Uri}.
     * This is usually only used by {@link #query}, since it performs table joins useful for
     * {@link Cursor} data.
     */
    private SelectionBuilder buildExpandedSelection(Uri uri, int match) {
        final SelectionBuilder builder = new SelectionBuilder();
        switch (match) {
            case UriCodes.PROJECTS: {
                return builder.table(Tables.PROJECTS);
            }
            case UriCodes.PROJECT: {
                final String id = Projects.getProjectId(uri);
                return builder.table(Tables.PROJECTS).where(Projects.ID + "=?", id);
            }
            case UriCodes.TASKS: {
                return builder.table(Tables.TASKS);
            }
            case UriCodes.TASK: {
                final String id = Long.toString(ContentUris.parseId(uri));
                return builder.table(Tables.TASKS).where(Tasks._ID + "=?", id);
            }
            case UriCodes.TIME_ENTRIES: {
                return builder.table(Tables.TIME_ENTRIES_JOIN_TASKS);
            }
            case UriCodes.TIME_ENTRY: {
                final String id = Long.toString(ContentUris.parseId(uri));
                return builder.table(Tables.TIME_ENTRIES).where(TimeEntries._ID + "=?", id);
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    private interface UriCodes {
        int PROJECTS = 100;
        int PROJECT = 101;
        int TASKS = 200;
        int TASK = 201;
        int TIME_ENTRIES = 300;
        int TIME_ENTRY = 301;
    }
}

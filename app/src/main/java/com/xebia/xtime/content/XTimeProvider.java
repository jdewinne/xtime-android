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

import com.xebia.xtime.content.XTimeContract.TimeEntries;
import com.xebia.xtime.content.XTimeContract.TimeSheetRows;
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

        matcher.addURI(authority, Tables.TIME_SHEET_ROWS, UriCodes.TIME_SHEET_ROWS);
        matcher.addURI(authority, Tables.TIME_SHEET_ROWS + "/*", UriCodes.TIME_SHEET_ROW);

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
            case UriCodes.TIME_SHEET_ROWS:
                return TimeSheetRows.CONTENT_TYPE;
            case UriCodes.TIME_SHEET_ROW:
                return TimeSheetRows.CONTENT_ITEM_TYPE;
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
            case UriCodes.TIME_SHEET_ROWS: {
                long id = db.insertOrThrow(Tables.TIME_SHEET_ROWS, null, values);
                notifyChange(uri);
                return TimeSheetRows.buildUri(id);
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
        getContext().getContentResolver().notifyChange(uri, null);
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
            case UriCodes.TIME_SHEET_ROWS: {
                return builder.table(Tables.TIME_SHEET_ROWS);
            }
            case UriCodes.TIME_SHEET_ROW: {
                final String id = Long.toString(ContentUris.parseId(uri));
                return builder.table(Tables.TIME_SHEET_ROWS)
                        .where(TimeSheetRows._ID + "=?", id);
            }
            case UriCodes.TIME_ENTRIES: {
                return builder.table(Tables.TIME_ENTRIES);
            }
            case UriCodes.TIME_ENTRY: {
                final String id = Long.toString(ContentUris.parseId(uri));
                return builder.table(Tables.TIME_ENTRIES)
                        .where(TimeEntries._ID + "=?", id);
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
            case UriCodes.TIME_SHEET_ROWS: {
                return builder.table(Tables.TIME_SHEET_ROWS);
            }
            case UriCodes.TIME_SHEET_ROW: {
                final String id = Long.toString(ContentUris.parseId(uri));
                return builder.table(Tables.TIME_SHEET_ROWS).where(TimeSheetRows._ID + "=?", id);
            }
            case UriCodes.TIME_ENTRIES: {
                return builder.table(Tables.TIME_ENTRIES_JOIN_ROWS_AND_SHEETS);
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
        int TIME_SHEET_ROWS = 200;
        int TIME_SHEET_ROW = 201;
        int TIME_ENTRIES = 300;
        int TIME_ENTRY = 301;
    }
}

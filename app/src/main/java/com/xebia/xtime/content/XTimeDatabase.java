package com.xebia.xtime.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.xebia.xtime.content.XTimeContract.TaskColumns;
import com.xebia.xtime.content.XTimeContract.TimeEntryColumns;

class XTimeDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "xtime.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String FLOAT_TYPE = " REAL";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA = ", ";
    private static final String NOT_NULL = " NOT NULL";
    private static final String TAG = "XTimeDatabase";

    public XTimeDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Create database");
        db.execSQL("CREATE TABLE " + Tables.TASKS + " ("
                + TaskColumns._ID + " INTEGER PRIMARY KEY" + COMMA
                + TaskColumns.DESCRIPTION + TEXT_TYPE + COMMA
                + TaskColumns.PROJECT_ID + TEXT_TYPE + COMMA
                + TaskColumns.PROJECT_NAME + TEXT_TYPE + COMMA
                + TaskColumns.WORKTYPE_ID + TEXT_TYPE + COMMA
                + TaskColumns.WORKTYPE_NAME + TEXT_TYPE
                + ")");
        db.execSQL("CREATE TABLE " + Tables.TIME_ENTRIES + " ("
                + TimeEntryColumns._ID + " INTEGER PRIMARY KEY" + COMMA
                + TimeEntryColumns.TASK_ID + INT_TYPE + COMMA
                + TimeEntryColumns.HOURS + FLOAT_TYPE + NOT_NULL + COMMA
                + TimeEntryColumns.ENTRY_DATE + INT_TYPE + NOT_NULL + COMMA
                + TimeEntryColumns.APPROVED + INT_TYPE
                + ")");
        // delete time entries when the task is deleted
        db.execSQL("CREATE TRIGGER " + Triggers.TASK_ENTRIES_DELETE
                + " AFTER DELETE ON " + Tables.TASKS
                + " BEGIN DELETE FROM " + Tables.TIME_ENTRIES
                + " WHERE " + Tables.TIME_ENTRIES + "." + TimeEntryColumns.TASK_ID
                + "=old." + TaskColumns._ID + ";" + " END;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Update database: " + oldVersion + " -> " + newVersion);
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + Tables.TASKS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + Tables.TIME_ENTRIES + ";");
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Downgrade database: " + oldVersion + " -> " + newVersion);
        onUpgrade(db, oldVersion, newVersion);
    }

    interface Tables {
        String TASKS = "tasks";
        String TIME_ENTRIES = "time_entries";
        String TIME_ENTRIES_JOIN_TASKS = TIME_ENTRIES
                + " LEFT OUTER JOIN " + TASKS + " ON "
                + TIME_ENTRIES + "." + TimeEntryColumns.TASK_ID + "="
                + TASKS + "." + TaskColumns._ID;
    }

    interface Triggers {
        String TASK_ENTRIES_DELETE = "task_entries_delete";
    }
}

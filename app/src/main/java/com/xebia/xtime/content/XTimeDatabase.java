package com.xebia.xtime.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.xebia.xtime.content.XTimeContract.TimeEntryColumns;
import com.xebia.xtime.content.XTimeContract.TimeSheetRowColumns;

class XTimeDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "xtime.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String FLOAT_TYPE = " REAL";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA = ", ";
    private static final String SQL_CREATE_TIME_SHEET_ROWS =
            "CREATE TABLE " + Tables.TIME_SHEET_ROWS + " ("
                    + TimeSheetRowColumns._ID + " INTEGER PRIMARY KEY" + COMMA
                    + TimeSheetRowColumns.DESCRIPTION + TEXT_TYPE + COMMA
                    + TimeSheetRowColumns.PROJECT_ID + TEXT_TYPE + COMMA
                    + TimeSheetRowColumns.PROJECT_NAME + TEXT_TYPE + COMMA
                    + TimeSheetRowColumns.WORKTYPE_ID + TEXT_TYPE + COMMA
                    + TimeSheetRowColumns.WORKTYPE_NAME + TEXT_TYPE + COMMA
                    + " UNIQUE (" + TimeSheetRowColumns.DESCRIPTION + COMMA
                    + TimeSheetRowColumns.PROJECT_ID + COMMA + TimeSheetRowColumns.WORKTYPE_ID
                    + ") ON CONFLICT REPLACE)";
    private static final String NOT_NULL = " NOT NULL";
    private static final String SQL_CREATE_TIME_ENTRIES =
            "CREATE TABLE " + Tables.TIME_ENTRIES + " ("
                    + TimeEntryColumns._ID + " INTEGER PRIMARY KEY" + COMMA
                    + TimeEntryColumns.SHEET_ROW_ID + INT_TYPE + COMMA
                    + TimeEntryColumns.HOURS + FLOAT_TYPE + NOT_NULL + COMMA
                    + TimeEntryColumns.ENTRY_DATE + INT_TYPE + NOT_NULL + COMMA
                    + TimeEntryColumns.APPROVED + INT_TYPE
                    + ")";
    private static final String TAG = "XTimeDatabase";

    public XTimeDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Create database");
        db.execSQL(SQL_CREATE_TIME_SHEET_ROWS);
        db.execSQL(SQL_CREATE_TIME_ENTRIES);

        db.execSQL("CREATE TRIGGER " + Triggers.TIME_SHEET_ROW_ENTRIES_DELETE
                + " AFTER DELETE ON " + Tables.TIME_SHEET_ROWS
                + " BEGIN DELETE FROM " + Tables.TIME_ENTRIES
                + " WHERE " + Tables.TIME_ENTRIES + "." + TimeEntryColumns.SHEET_ROW_ID
                + "=old." + TimeSheetRowColumns._ID + ";" + " END;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Update database: " + oldVersion + " -> " + newVersion);
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + Tables.TIME_SHEET_ROWS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + Tables.TIME_ENTRIES + ";");
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Downgrade database: " + oldVersion + " -> " + newVersion);
        onUpgrade(db, oldVersion, newVersion);
    }

    interface Tables {
        String TIME_SHEET_ROWS = "time_sheet_rows";
        String TIME_ENTRIES = "time_entries";
        String TIME_ENTRIES_JOIN_ROWS_AND_SHEETS = TIME_ENTRIES
                + " LEFT OUTER JOIN " + TIME_SHEET_ROWS + " ON "
                + TIME_ENTRIES + "." + TimeEntryColumns.SHEET_ROW_ID + "="
                + TIME_SHEET_ROWS + "." + TimeSheetRowColumns._ID;
    }

    interface Triggers {
        String TIME_SHEET_ROW_ENTRIES_DELETE = "time_sheet_row_entries_delete";
    }
}

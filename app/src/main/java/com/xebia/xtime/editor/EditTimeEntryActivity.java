package com.xebia.xtime.editor;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import com.xebia.xtime.R;
import com.xebia.xtime.shared.model.TimeEntry;

import java.util.Date;

/**
 * Activity that wraps a {@link EditTimeEntryFragment}.
 * <p>
 * When the activity is created, it checks the {@link #EXTRA_TIME_CELL} Intent extra for a time
 * sheet entry to edit. If the time sheet is not present, a new one will be created.
 * </p>
 */
public class EditTimeEntryActivity extends Activity implements EditTimeEntryFragment.Listener {

    /**
     * Key for intent extra that contains time stamp of the day
     */
    public static final String EXTRA_DATE = "date";
    /**
     * Key for (optional) intent extra that contains the TimeEntry to edit,
     * leave null to create a new entry
     */
    public static final String EXTRA_TIME_CELL = "time_cell";
    /**
     * Result code for when the time cell was deleted
     */
    public static final int RESULT_DELETE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_container);

        Date date = new Date(getIntent().getLongExtra(EXTRA_DATE, -1));
        if (date.getTime() < 0) {
            throw new NullPointerException("Missing EXTRA_DATE");
        }

        TimeEntry timeEntry = getIntent().getParcelableExtra(EXTRA_TIME_CELL);

        if (savedInstanceState == null) {
            Fragment fragment = EditTimeEntryFragment.getInstance(date, timeEntry);
            getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
        }
    }

    @Override
    public void onEntryUpdate(TimeEntry timeEntry) {
        // put the time sheet in the result data
        Intent data = new Intent();
        data.putExtra(EXTRA_TIME_CELL, timeEntry);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onEntryDelete(TimeEntry timeEntry) {
        // put the time sheet in the result data
        Intent data = new Intent();
        data.putExtra(EXTRA_TIME_CELL, timeEntry);
        setResult(RESULT_DELETE, data);
        finish();
    }
}

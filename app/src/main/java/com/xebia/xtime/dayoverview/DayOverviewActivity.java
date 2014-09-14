package com.xebia.xtime.dayoverview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.xebia.xtime.R;
import com.xebia.xtime.editor.EditTimeEntryActivity;
import com.xebia.xtime.shared.model.DayOverview;
import com.xebia.xtime.shared.model.TimeEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Activity that displays a list of {@link com.xebia.xtime.shared.model.TimeEntry} in a {@link DailyTimeEntryFragment}.
 * <p>
 * Clicking on a time sheet entry opens up the {@link com.xebia.xtime.editor.EditTimeEntryActivity},
 * and the action bar also contains an option to create a new time sheet entry.
 * </p>
 */
public class DayOverviewActivity extends Activity implements DailyTimeEntryFragment.Listener {

    /**
     * Key for intent extra that contains the day overview to display
     */
    public static final String EXTRA_DAY_OVERVIEW = "day_overview";
    private static final int REQ_CODE_EDIT = 1;
    private static final int REQ_CODE_CREATE = 2;
    private DayOverview mOverview;
    private TimeEntry mSelectedEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_container);

        // get the day overview
        mOverview = getIntent().getParcelableExtra(EXTRA_DAY_OVERVIEW);
        if (null == mOverview) {
            throw new NullPointerException("Missing EXTRA_DAY_OVERVIEW");
        }

        // set up the UI
        if (savedInstanceState == null) {
            ArrayList<TimeEntry> timeSheets = (ArrayList<TimeEntry>) mOverview.getTimeEntries();
            Fragment fragment = DailyTimeEntryFragment.getInstance(timeSheets);
            FragmentTransaction tx = getFragmentManager().beginTransaction();
            tx.replace(R.id.content, fragment, "tag");
            tx.commit();
        }

        // set up the title
        setTitle(getDayTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mOverview.isEditable()) {
            getMenuInflater().inflate(R.menu.menu_day_overview, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.newItem) {
            startEditor(null, REQ_CODE_CREATE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimeCellSelected(TimeEntry selected) {
        if (mOverview.isEditable()) {
            mSelectedEntry = selected;
            startEditor(selected, REQ_CODE_EDIT);
        }
    }

    private void startEditor(TimeEntry entry, int requestCode) {
        Intent editor = new Intent(this, EditTimeEntryActivity.class);
        editor.putExtra(EditTimeEntryActivity.EXTRA_DATE, mOverview.getDate().getTime());
        editor.putExtra(EditTimeEntryActivity.EXTRA_TIME_CELL, entry);
        startActivityForResult(editor, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_EDIT:
                if (RESULT_OK == resultCode) {
                    TimeEntry edited = data.getParcelableExtra(EditTimeEntryActivity
                            .EXTRA_TIME_CELL);
                    onEntryEdited(edited);
                } else if (EditTimeEntryActivity.RESULT_DELETE == resultCode) {
                    onEntryDeleted();
                }
                mSelectedEntry = null;
                break;
            case REQ_CODE_CREATE:
                if (RESULT_OK == resultCode) {
                    TimeEntry created = data.getParcelableExtra(EditTimeEntryActivity
                            .EXTRA_TIME_CELL);
                    onEntryCreated(created);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onEntryDeleted() {
        mOverview.getTimeEntries().remove(mSelectedEntry);
        onDataSetChanged();
    }

    private void onEntryEdited(TimeEntry edited) {
        mOverview.getTimeEntries().remove(mSelectedEntry);
        mOverview.getTimeEntries().add(edited);
        onDataSetChanged();
    }

    private void onEntryCreated(TimeEntry created) {
        mOverview.getTimeEntries().add(created);
        onDataSetChanged();
    }

    @TargetApi(18)
    private String getDayTitle() {
        Locale locale = Locale.getDefault();
        String pattern = "EEEE d MMMM";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            pattern = android.text.format.DateFormat.getBestDateTimePattern(locale, pattern);
        }
        DateFormat dateFormat = new SimpleDateFormat(pattern, locale);
        dateFormat.setTimeZone(TimeZone.getTimeZone("CET"));
        return dateFormat.format(mOverview.getDate());
    }

    /**
     * Notifies the list fragment that the data set changed and the list might have to be updated.
     */
    private void onDataSetChanged() {
        DailyTimeEntryFragment fragment = (DailyTimeEntryFragment) getFragmentManager()
                .findFragmentByTag("tag");
        fragment.onDataSetChanged();
    }
}

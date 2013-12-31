package com.xebia.xtime.dayoverview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.xebia.xtime.R;
import com.xebia.xtime.editor.EditTimeSheetActivity;
import com.xebia.xtime.shared.model.DayOverview;
import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.TimeSheetEntry;
import com.xebia.xtime.shared.model.WeekOverview;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DayOverviewActivity extends ActionBarActivity implements DailyTimeSheetFragment
        .Listener {

    public static final String EXTRA_DAY_OVERVIEW = "day_overview";
    public static final String EXTRA_WEEK_OVERVIEW = "week_overview";
    private static final int REQ_CODE_EDIT = 1;
    private static final int REQ_CODE_CREATE = 2;
    private WeekOverview mWeekOverview;
    private DayOverview mDayOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_overview);

        // get the day overview
        mDayOverview = getIntent().getParcelableExtra(EXTRA_DAY_OVERVIEW);
        if (null == mDayOverview) {
            throw new NullPointerException("Missing EXTRA_DAY_OVERVIEW");
        }

        // get the week overview
        mWeekOverview = getIntent().getParcelableExtra(EXTRA_WEEK_OVERVIEW);
        if (null == mWeekOverview) {
            throw new NullPointerException("Missing EXTRA_WEEK_OVERVIEW");
        }

        // set up the UI
        if (savedInstanceState == null) {
            Fragment fragment = DailyTimeSheetFragment.getInstance(mDayOverview);
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }

        // set up the title
        DateFormat dateFormat = new SimpleDateFormat("EEE d MMMM");
        setTitle(dateFormat.format(mDayOverview.getDate()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_day_overview, menu);
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

    private void startEditor(TimeSheetEntry entry, int requestCode) {
        Intent editor = new Intent(this, EditTimeSheetActivity.class);
        editor.putExtra(EditTimeSheetActivity.EXTRA_DATE, mDayOverview.getDate().getTime());
        editor.putParcelableArrayListExtra(EditTimeSheetActivity.EXTRA_PROJECTS,
                (ArrayList<Project>) mWeekOverview.getProjects());
        editor.putExtra(EditTimeSheetActivity.EXTRA_TIME_SHEET, entry);
        startActivityForResult(editor, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_EDIT:
                if (RESULT_OK == resultCode) {
                    // TODO: Update list with edited entry
                }
                break;
            case REQ_CODE_CREATE:
                if (RESULT_OK == resultCode) {
                    // TODO: Update list with new entry
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onTimeSheetEntrySelected(TimeSheetEntry selected) {
        startEditor(selected, REQ_CODE_EDIT);
    }
}

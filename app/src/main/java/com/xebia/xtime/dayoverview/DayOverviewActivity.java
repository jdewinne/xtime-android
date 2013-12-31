package com.xebia.xtime.dayoverview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import com.xebia.xtime.R;
import com.xebia.xtime.editor.EditTimeCellActivity;
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
    private WeekOverview mWeekOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_overview);

        // get the day overview
        DayOverview dayOverview = getIntent().getParcelableExtra(EXTRA_DAY_OVERVIEW);
        if (null == dayOverview) {
            throw new NullPointerException("Missing EXTRA_DAY_OVERVIEW");
        }

        // get the week overview
        mWeekOverview = getIntent().getParcelableExtra(EXTRA_WEEK_OVERVIEW);
        if (null == mWeekOverview) {
            throw new NullPointerException("Missing EXTRA_WEEK_OVERVIEW");
        }

        // set up the UI
        if (savedInstanceState == null) {
            Fragment fragment = DailyTimeSheetFragment.getInstance(dayOverview);
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }

        // set up the title
        DateFormat dateFormat = new SimpleDateFormat("EEE d MMMM");
        setTitle(dateFormat.format(dayOverview.getDate()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_EDIT && resultCode == RESULT_OK) {
            // TODO: refresh list of time sheets after edit activity finishes
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onTimeSheetEntrySelected(TimeSheetEntry selected) {
        Intent editor = new Intent(this, EditTimeCellActivity.class);
        editor.putExtra(EditTimeCellActivity.EXTRA_TIME_SHEET, selected);
        editor.putParcelableArrayListExtra(EditTimeCellActivity.EXTRA_PROJECTS,
                (ArrayList<Project>) mWeekOverview.getProjects());
        startActivityForResult(editor, REQ_CODE_EDIT);
    }
}

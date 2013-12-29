package com.xebia.xtime.dayoverview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.xebia.xtime.R;
import com.xebia.xtime.editor.EditTimeCellActivity;
import com.xebia.xtime.shared.model.DayOverview;
import com.xebia.xtime.shared.model.TimeSheetEntry;

public class DayOverviewActivity extends ActionBarActivity implements DailyTimeSheetFragment
        .Listener {

    public static final String EXTRA_DAY_OVERVIEW = "com.xebia.xtime.dayoverview.DAY_OVERVIEW";
    public static final String EXTRA_WEEK_OVERVIEW = "com.xebia.xtime.dayoverview.WEEK_OVERVIEW";
    private static final int REQ_CODE_EDIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_overview);

        if (savedInstanceState == null) {
            DayOverview dayOverview = getIntent().getParcelableExtra(EXTRA_DAY_OVERVIEW);
            getSupportFragmentManager().beginTransaction().add(R.id.container,
                    DailyTimeSheetFragment.getInstance(dayOverview)).commit();
        }
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
        startActivityForResult(editor, REQ_CODE_EDIT);
    }
}

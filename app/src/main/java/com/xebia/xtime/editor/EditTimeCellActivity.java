package com.xebia.xtime.editor;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.xebia.xtime.R;
import com.xebia.xtime.shared.model.TimeSheetEntry;

public class EditTimeCellActivity extends ActionBarActivity implements EditTimeSheetFragment
        .Listener {

    public static final String EXTRA_TIME_SHEET = "TIME_SHEET";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_time_cell);

        if (savedInstanceState == null) {
            TimeSheetEntry entry = getIntent().getParcelableExtra(EXTRA_TIME_SHEET);
            getSupportFragmentManager().beginTransaction().add(R.id.container,
                    EditTimeSheetFragment.getInstance(entry)).commit();
        }
    }

    @Override
    public void onChangesSaved() {
        setResult(RESULT_OK);
        finish();
    }
}

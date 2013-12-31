package com.xebia.xtime.editor;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.xebia.xtime.R;
import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.TimeSheetEntry;

import java.util.ArrayList;

public class EditTimeCellActivity extends ActionBarActivity implements EditTimeSheetFragment
        .Listener {

    public static final String EXTRA_TIME_SHEET = "time_sheet";
    public static final String EXTRA_PROJECTS = "projects";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_time_cell);

        TimeSheetEntry entry = getIntent().getParcelableExtra(EXTRA_TIME_SHEET);
        if (null == entry) {
            throw new NullPointerException("Missing EXTRA_TIME_SHEET");
        }

        ArrayList<Project> projects = getIntent().getParcelableArrayListExtra(EXTRA_PROJECTS);
        if (null == projects) {
            throw new NullPointerException("Missing EXTRA_PROJECTS");
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container,
                    EditTimeSheetFragment.getInstance(entry, projects)).commit();
        }
    }

    @Override
    public void onChangesSaved() {
        setResult(RESULT_OK);
        finish();
    }
}

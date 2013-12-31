package com.xebia.xtime.editor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.xebia.xtime.R;
import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.TimeSheetEntry;

import java.util.ArrayList;
import java.util.Date;

public class EditTimeSheetActivity extends ActionBarActivity implements EditTimeSheetFragment
        .Listener {

    /**
     * Key for intent extra that contains the list of projects
     */
    public static final String EXTRA_PROJECTS = "projects";
    /**
     * Key for intent extra that contains time stamp of the day
     */
    public static final String EXTRA_DATE = "date";
    /**
     * Key for (optional) intent extra that contains the TimeSheetEntry to edit,
     * leave null to create a new entry
     */
    public static final String EXTRA_TIME_SHEET = "time_sheet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_time_cell);

        Date date = new Date(getIntent().getLongExtra(EXTRA_DATE, -1));
        if (date.getTime() < 0) {
            throw new NullPointerException("Missing EXTRA_DATE");
        }

        ArrayList<Project> projects = getIntent().getParcelableArrayListExtra(EXTRA_PROJECTS);
        if (null == projects) {
            throw new NullPointerException("Missing EXTRA_PROJECTS");
        }

        TimeSheetEntry entry = getIntent().getParcelableExtra(EXTRA_TIME_SHEET);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container,
                    EditTimeSheetFragment.getInstance(date, projects, entry)).commit();
        }
    }

    @Override
    public void onChangesSaved(TimeSheetEntry entry) {
        // put the time sheet in the result data
        Intent data = new Intent();
        data.putExtra(EXTRA_TIME_SHEET, entry);
        setResult(RESULT_OK, data);
        finish();
    }
}

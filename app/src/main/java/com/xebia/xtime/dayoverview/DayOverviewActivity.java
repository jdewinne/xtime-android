package com.xebia.xtime.dayoverview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.xebia.xtime.R;
import com.xebia.xtime.shared.model.DayOverview;

public class DayOverviewActivity extends ActionBarActivity {

    public static final String EXTRA_DAY_OVERVIEW = "com.xebia.xtime.dayoverview.DAY_OVERVIEW";
    public static final String EXTRA_WEEK_OVERVIEW = "com.xebia.xtime.dayoverview.WEEK_OVERVIEW";

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
}

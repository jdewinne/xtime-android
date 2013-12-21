package com.xebia.xtime.weekoverview;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.xebia.xtime.weekoverview.model.WeekOverview;

import java.util.Date;

public class WeekOverviewLoader extends AsyncTaskLoader<WeekOverview> {

    private final Date mDate;

    public WeekOverviewLoader(Context context, Date date) {
        super(context);
        mDate = date;
    }

    @Override
    public WeekOverview loadInBackground() {
        String response = new WeekOverviewRequest(mDate).submit();
        return WeekOverviewParser.parse(response);
    }

    @Override
    protected void onStartLoading() {
        // keep loading in the background
        forceLoad();
    }
}

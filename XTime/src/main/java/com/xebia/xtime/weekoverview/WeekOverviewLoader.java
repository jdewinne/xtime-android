package com.xebia.xtime.weekoverview;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.xebia.xtime.weekoverview.model.WeekOverview;

public class WeekOverviewLoader extends AsyncTaskLoader<WeekOverview> {

    public WeekOverviewLoader(Context context) {
        super(context);
    }

    @Override
    public WeekOverview loadInBackground() {
        String response = new WeekOverviewRequest().submit();
        return WeekOverviewParser.parse(response);
    }

    @Override
    protected void onStartLoading() {
        // keep loading in the background
        forceLoad();
    }
}

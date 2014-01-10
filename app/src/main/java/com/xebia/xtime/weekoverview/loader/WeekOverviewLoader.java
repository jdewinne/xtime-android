package com.xebia.xtime.weekoverview.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.xebia.xtime.shared.model.WeekOverview;

import org.apache.http.auth.AuthenticationException;

import java.util.Date;

public class WeekOverviewLoader extends AsyncTaskLoader<WeekOverview> {

    private final Date mDate;

    public WeekOverviewLoader(Context context, Date date) {
        super(context);
        mDate = date;
    }

    @Override
    public WeekOverview loadInBackground() {
        try {
            String response = new WeekOverviewRequest(mDate).submit();
            return WeekOverviewParser.parse(response);
        } catch (AuthenticationException e) {
            return null;
        }
    }

    @Override
    protected void onStartLoading() {
        // keep loading in the background
        forceLoad();
    }
}

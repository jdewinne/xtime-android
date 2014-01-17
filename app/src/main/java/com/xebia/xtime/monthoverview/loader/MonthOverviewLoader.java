package com.xebia.xtime.monthoverview.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.xebia.xtime.shared.model.XTimeOverview;
import com.xebia.xtime.shared.parser.XTimeOverviewParser;

import org.apache.http.auth.AuthenticationException;

import java.util.Date;

public class MonthOverviewLoader extends AsyncTaskLoader<XTimeOverview> {

    private final Date mMonth;

    public MonthOverviewLoader(Context context, Date month) {
        super(context);
        mMonth = month;
    }

    @Override
    public XTimeOverview loadInBackground() {
        try {
            String response = new MonthOverviewRequest(mMonth).submit();
            return XTimeOverviewParser.parse(response);
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

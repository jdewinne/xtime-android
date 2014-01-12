package com.xebia.xtime.monthoverview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xebia.xtime.R;
import com.xebia.xtime.monthoverview.loader.MonthOverviewLoader;
import com.xebia.xtime.shared.model.WeekOverview;

import java.util.Date;

public class MonthOverviewFragment extends Fragment implements LoaderManager
        .LoaderCallbacks<WeekOverview> {

    public MonthOverviewFragment() {
        // required default constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_month_overview, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<WeekOverview> onCreateLoader(int id, Bundle args) {
        return new MonthOverviewLoader(getActivity(), new Date());
    }

    @Override
    public void onLoadFinished(Loader<WeekOverview> weekOverviewLoader, WeekOverview weekOverview) {
        // TODO: show fancy UI
    }

    @Override
    public void onLoaderReset(Loader<WeekOverview> weekOverviewLoader) {
        // nothing to do
    }
}

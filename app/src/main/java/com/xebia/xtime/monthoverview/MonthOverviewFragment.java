package com.xebia.xtime.monthoverview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.xebia.xtime.R;
import com.xebia.xtime.monthoverview.loader.MonthOverviewLoader;
import com.xebia.xtime.shared.model.TimeSheetRow;
import com.xebia.xtime.shared.model.WeekOverview;

import java.util.Date;

public class MonthOverviewFragment extends Fragment implements LoaderManager
        .LoaderCallbacks<WeekOverview> {

    private TableLayout mTable;
    private WeekOverview mOverview;

    public MonthOverviewFragment() {
        // required default constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_month_overview, container, false);
        if (null != root) {
            mTable = (TableLayout) root.findViewById(R.id.table);
        }
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mOverview = weekOverview;
        updateTable();
    }

    @Override
    public void onLoaderReset(Loader<WeekOverview> weekOverviewLoader) {
        // nothing to do
    }

    private void updateTable() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (TimeSheetRow timeSheet : mOverview.getTimeSheetRows()) {
                    View row = getActivity().getLayoutInflater().inflate(R.layout
                            .table_month_row, null, false);
                    if (null != row) {
                        TextView project = (TextView) row.findViewById(R.id.project);
                        project.setText(timeSheet.getProject().getName());
                        mTable.addView(row);
                    }
                }
            }
        });
    }
}

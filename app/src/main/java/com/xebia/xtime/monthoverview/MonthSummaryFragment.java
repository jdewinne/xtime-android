package com.xebia.xtime.monthoverview;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xebia.xtime.R;
import com.xebia.xtime.monthoverview.loader.MonthOverviewLoader;
import com.xebia.xtime.shared.TimeSheetUtils;
import com.xebia.xtime.shared.model.TimeSheetRow;
import com.xebia.xtime.shared.model.WeekOverview;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MonthSummaryFragment extends ListFragment implements LoaderManager
        .LoaderCallbacks<WeekOverview> {

    private static final String ARG_MONTH = "month";
    private WeekOverview mOverview;
    private List<TimeSheetRow> mRows;
    private Date mMonth;
    private View mFooterView;

    public MonthSummaryFragment() {
        // Required empty public constructor
    }

    /**
     * @param month Date indicating the start of the week
     * @return Title String of the fragment
     */
    public static String getTitle(Date month) {
        DateFormat formatter = new SimpleDateFormat("MMMM", Locale.getDefault());
        return formatter.format(month);
    }

    /**
     * @param month Date indicating the month to display
     * @return A new instance of fragment MonthSummaryFragment
     */
    public static MonthSummaryFragment newInstance(Date month) {
        MonthSummaryFragment fragment = new MonthSummaryFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_MONTH, month.getTime());
        fragment.setArguments(args);
        return fragment;
    }

    private void showList() {
        if (null == mRows) {
            mRows = new ArrayList<TimeSheetRow>();
            setListAdapter(new TimeSheetRowAdapter(getActivity(), mRows));
        } else {
            mRows.clear();
        }

        if (null != mOverview && null != mOverview.getTimeSheetRows()) {
            mRows.addAll(mOverview.getTimeSheetRows());
        }
        showGrandTotal();

        ((ArrayAdapter) getListAdapter()).notifyDataSetChanged();
    }

    private void showGrandTotal() {
        mFooterView.setVisibility(null != mRows && mRows.size() > 0 ? View.VISIBLE : View.GONE);
        if (null != mOverview) {
            double grandTotal = TimeSheetUtils.getGrandTotalHours(mOverview);
            ((TextView) mFooterView.findViewById(R.id.grand_total)).setText(NumberFormat
                    .getNumberInstance().format(grandTotal));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFooterView = inflater.inflate(R.layout.row_grand_total, null, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            mMonth = new Date(getArguments().getLong(ARG_MONTH, -1));
        }
        if (mMonth.getTime() < 0) {
            throw new NullPointerException("Missing ARG_MONTH argument");
        }

        getListView().addFooterView(mFooterView);

        setEmptyText(getText(R.string.empty_month_summary));

        // start loading the month overview
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<WeekOverview> onCreateLoader(int id, Bundle args) {
        return new MonthOverviewLoader(getActivity(), mMonth);
    }

    @Override
    public void onLoadFinished(Loader<WeekOverview> weekOverviewLoader, WeekOverview weekOverview) {
        mOverview = weekOverview;
        showList();
    }

    @Override
    public void onLoaderReset(Loader<WeekOverview> weekOverviewLoader) {
        // nothing to do
    }
}
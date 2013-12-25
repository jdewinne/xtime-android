package com.xebia.xtime.weekoverview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xebia.xtime.R;
import com.xebia.xtime.weekoverview.model.DailyHours;
import com.xebia.xtime.weekoverview.model.WeekOverview;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class WeekOverviewListFragment extends ListFragment implements LoaderManager
        .LoaderCallbacks<WeekOverview> {

    private static final String ARG_START_DATE = "start_date";
    private static final String TAG = "WeekOverviewListFragment";
    private Date mStartDate;
    private WeekOverview mOverview;
    private WeekOverviewListener mListener;
    private View mBusyIndicator;
    private List<DailyHours> mDailyHours;

    public WeekOverviewListFragment() {
        // Required empty public constructor
    }

    /**
     * @param startDate Date indicating the week to display
     * @return A new instance of fragment WeekOverviewListFragment
     */
    public static WeekOverviewListFragment newInstance(Date startDate) {
        WeekOverviewListFragment fragment = new WeekOverviewListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_START_DATE, startDate.getTime());
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @param startDate Date indicating the start of the week
     * @return Title String of this fragment
     */
    public static String getTitle(Date startDate) {
        Date endDate = new Date(startDate.getTime() + 6 * DateUtils.DAY_IN_MILLIS);
        DateFormat formatter = new SimpleDateFormat("dd/MM");
        return formatter.format(startDate) + " - " + formatter.format(endDate);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDailyHours = new ArrayList<DailyHours>();
        setListAdapter(new DailyHoursListAdapter(getActivity(), mDailyHours));

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStartDate = new Date(getArguments().getLong(ARG_START_DATE, -1));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week_overview, container, false);
        if (null != view) {
            mBusyIndicator = view.findViewById(R.id.week_overview_busy);
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (WeekOverviewListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " +
                    "WeekOverviewListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        DailyHours item = (DailyHours) getListView().getItemAtPosition(position);
        // notify handler
        mListener.onItemClicked(item);
    }

    @Override
    public Loader<WeekOverview> onCreateLoader(int i, Bundle bundle) {
        setBusy(true);
        return new WeekOverviewLoader(getActivity(), mStartDate);
    }

    @Override
    public void onLoadFinished(Loader<WeekOverview> loader, WeekOverview overview) {
        setBusy(false);

        mOverview = overview;
        updateList();
    }

    private void updateList() {
        mDailyHours.clear();
        if (null == mOverview || null == mOverview.getTimeSheetRows()) {
            // no data to show
            Log.d(TAG, "No overview data to show");
        } else {
            mDailyHours.addAll(TimeSheetUtils.dailyHours(mOverview, mStartDate));
        }
        ((ArrayAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<WeekOverview> loader) {
        // nothing to do
    }

    private void setBusy(final boolean busy) {
        if (null != getActivity()) {
            getActivity().runOnUiThread(new Thread() {
                @Override
                public void run() {
                    getListView().setVisibility(busy ? View.GONE : View.VISIBLE);
                    mBusyIndicator.setVisibility(busy ? View.VISIBLE : View.GONE);
                }
            });
        }
    }

    public interface WeekOverviewListener {
        public void onItemClicked(DailyHours dailyHours);
    }

}

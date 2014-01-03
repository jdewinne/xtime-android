package com.xebia.xtime.weekoverview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.xebia.xtime.R;
import com.xebia.xtime.shared.model.DayOverview;
import com.xebia.xtime.shared.model.WeekOverview;
import com.xebia.xtime.weekoverview.loader.WeekOverviewLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Fragment that represents a week as a list of days.
 * <p/>
 * When the activity is created, the fragment kicks off a AsyncTaskLoader to fetch the
 * WeekOverview from the XTime backend.
 * <p/>
 * Each row shows the total amount of work that has been registered for that day. Clicking on a
 * day should open up the day details.
 */
public class DailyHoursListFragment extends ListFragment implements LoaderManager
        .LoaderCallbacks<WeekOverview> {

    private static final String ARG_START_DATE = "start_date";
    private Date mStartDate;
    private WeekOverview mOverview;
    private Listener mListener;
    private View mBusyIndicator;
    private List<DayOverview> mDays;

    public DailyHoursListFragment() {
        // Required empty public constructor
    }

    /**
     * @param startDate Date indicating the week to display
     * @return A new instance of fragment DailyHoursListFragment
     */
    public static DailyHoursListFragment newInstance(Date startDate) {
        DailyHoursListFragment fragment = new DailyHoursListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_START_DATE, startDate.getTime());
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @param startDate Date indicating the start of the week
     * @return Title String of the fragment
     */
    public static String getTitle(Date startDate) {
        Date endDate = new Date(startDate.getTime() + 6 * DateUtils.DAY_IN_MILLIS);
        DateFormat formatter = new SimpleDateFormat("dd/MM", Locale.US);
        return formatter.format(startDate) + " - " + formatter.format(endDate);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDays = new ArrayList<DayOverview>();
        setListAdapter(new DailyHoursListAdapter(getActivity(), mDays));

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
        View view = inflater.inflate(R.layout.list_daily_hours, container, false);
        if (null != view) {
            mBusyIndicator = view.findViewById(R.id.week_overview_busy);
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " +
                    "DailyHoursListFragment.Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mListener.onItemClicked((DayOverview) l.getItemAtPosition(position));
    }

    @Override
    public Loader<WeekOverview> onCreateLoader(int i, Bundle bundle) {
        showLoadingIndicator(true);
        return new WeekOverviewLoader(getActivity(), mStartDate);
    }

    @Override
    public void onLoadFinished(Loader<WeekOverview> loader, WeekOverview overview) {
        showLoadingIndicator(false);

        mOverview = overview;
        updateList();
    }

    private void updateList() {
        mDays.clear();
        if (null != mOverview && null != mOverview.getTimeSheetRows()) {
            mDays.addAll(TimeSheetUtils.weekToDays(mOverview, mStartDate));
        }
        if (mDays.size() <= 0) {
            Toast.makeText(getActivity(), R.string.empty_week_overview, Toast.LENGTH_LONG).show();
        }
        ((ArrayAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<WeekOverview> loader) {
        mDays.clear();
    }

    private void showLoadingIndicator(final boolean busy) {
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

    /**
     * Interface for handling clicks on the list of DayOverviews
     */
    public interface Listener {
        public void onItemClicked(DayOverview overview);
    }
}

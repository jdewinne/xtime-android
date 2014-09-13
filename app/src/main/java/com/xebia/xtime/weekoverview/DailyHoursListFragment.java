package com.xebia.xtime.weekoverview;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xebia.xtime.R;
import com.xebia.xtime.content.XTimeContract.TimeEntries;
import com.xebia.xtime.shared.TimeSheetUtils;
import com.xebia.xtime.shared.model.DayOverview;
import com.xebia.xtime.shared.model.XTimeOverview;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Fragment that represents a week as a list of days.
 * <p/>
 * When the activity is created, the fragment kicks off a AsyncTaskLoader to fetch the
 * XTimeOverview from the XTime backend.
 * <p/>
 * Each row shows the total amount of work that has been registered for that day. Clicking on a
 * day should open up the day details.
 */
public class DailyHoursListFragment extends ListFragment implements LoaderManager
        .LoaderCallbacks<Cursor> {

    public static final String TAG = "DailyHoursListFragment";
    private static final String ARG_START_DATE = "start_date";
    private Date mStartDate;
    private XTimeOverview mOverview;
    private Listener mListener;
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
        if (getArguments() != null) {
            mStartDate = new Date(getArguments().getLong(ARG_START_DATE, -1));
        }
        setEmptyText(getText(R.string.empty_week_overview));

        // start loading the week overview
        getLoaderManager().initLoader(0, null, this);
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = TimeEntries.ENTRY_DATE + " BETWEEN ? AND ?";
        Calendar nextWeek = Calendar.getInstance();
        nextWeek.setTime(mStartDate);
        nextWeek.add(Calendar.WEEK_OF_YEAR, 1);
        String[] selectionArgs = new String[] {Long.toString(mStartDate.getTime()),
                Long.toString(nextWeek.getTime().getTime())};
        String orderBy = TimeEntries.ENTRY_DATE + " DESC";
        return new CursorLoader(getActivity(), TimeEntries.CONTENT_URI, null, selection,
                selectionArgs, orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG, "load finished");
        if (null != cursor && cursor.getCount() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mStartDate);
            int week = calendar.get(Calendar.WEEK_OF_YEAR);
            Log.d(TAG, "Loaded " + cursor.getCount() + " time registrations for week " + week);
            mOverview = TimeSheetUtils.cursorToOverview(cursor);
        } else {
            Log.d(TAG, "No time registrations loaded");
        }
        updateList();
    }

    private void updateList() {
        if (null == mDays) {
            mDays = new ArrayList<>();
            setListAdapter(new DailyHoursListAdapter(getActivity(), mDays));
        } else {
            mDays.clear();
        }

        if (null != mOverview && null != mOverview.getTimeSheetRows()) {
            mDays.addAll(WeekOverviewUtils.weekToDays(mOverview, mStartDate));
        }
        ((ArrayAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDays.clear();
    }

    /**
     * Interface for handling clicks on the list of DayOverviews
     */
    public interface Listener {
        public void onItemClicked(DayOverview overview);
    }
}

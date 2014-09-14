package com.xebia.xtime.dayoverview;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xebia.xtime.R;
import com.xebia.xtime.shared.model.TimeEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that displays a list of {@link com.xebia.xtime.shared.model.TimeEntry} in a ListView.
 * <p>
 * Each row displays the details of the time sheet entry (project, work type,
 * hours registered). Clicking on an item should open up an editor.
 * </p>
 */
public class DailyTimeEntryFragment extends ListFragment {

    private static final String ARG_TIME_CELLS = "time_cells";
    private List<TimeEntry> mTimeEntries;
    private Listener mListener;

    public DailyTimeEntryFragment() {
        // Required empty public constructor
    }

    public static DailyTimeEntryFragment getInstance(ArrayList<TimeEntry> timeSheetEntries) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_TIME_CELLS, timeSheetEntries);
        DailyTimeEntryFragment fragment = new DailyTimeEntryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Callback to refresh the list view when the list of time cells has been changed
     */
    public void onDataSetChanged() {
        ((ArrayAdapter) getListView().getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (null != getArguments()) {
            mTimeEntries = getArguments().getParcelableArrayList(ARG_TIME_CELLS);
        }
        if (null == mTimeEntries) {
            throw new NullPointerException("Missing ARG_TIME_CELLS argument");
        }

        DailyTimeEntryListAdapter adapter = new DailyTimeEntryListAdapter(getActivity(),
                mTimeEntries);
        setListAdapter(adapter);

        setEmptyText(getActivity().getString(R.string.empty_day_overview));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        TimeEntry item = (TimeEntry) l.getItemAtPosition(position);
        mListener.onTimeCellSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " +
                    "DailyTimeSheetFragment.Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Interface for handling clicks on the list of TimeCells
     */
    public interface Listener {
        public abstract void onTimeCellSelected(TimeEntry selected);
    }
}

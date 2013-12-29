package com.xebia.xtime.dayoverview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.xebia.xtime.shared.model.DayOverview;
import com.xebia.xtime.shared.model.TimeSheetEntry;

import java.util.List;

public class DailyTimeSheetFragment extends ListFragment {

    private static final String ARG_DAY_OVERVIEW = "DAY_OVERVIEW";
    private DayOverview mDayOverview;
    private Listener mListener;

    public DailyTimeSheetFragment() {
        // Required empty public constructor
    }

    public static DailyTimeSheetFragment getInstance(DayOverview dayOverview) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_DAY_OVERVIEW, dayOverview);
        DailyTimeSheetFragment fragment = new DailyTimeSheetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != getArguments()) {
            mDayOverview = getArguments().getParcelable(ARG_DAY_OVERVIEW);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<TimeSheetEntry> timeSheetEntries = mDayOverview.getTimeSheetEntries();
        DailyTimeSheetListAdapter adapter = new DailyTimeSheetListAdapter(getActivity(),
                timeSheetEntries);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        TimeSheetEntry item = (TimeSheetEntry) l.getItemAtPosition(position);
        mListener.onTimeSheetEntrySelected(item);
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
     * Interface for handling clicks on the list of TimeSheetEntries
     */
    public interface Listener {
        public abstract void onTimeSheetEntrySelected(TimeSheetEntry selected);
    }
}

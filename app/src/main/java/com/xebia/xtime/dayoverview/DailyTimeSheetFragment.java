package com.xebia.xtime.dayoverview;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.xebia.xtime.shared.model.DayOverview;
import com.xebia.xtime.shared.model.TimeSheetEntry;

import java.util.List;

public class DailyTimeSheetFragment extends ListFragment {

    private static final String ARG_DAY_OVERVIEW = "DAY_OVERVIEW";
    private DayOverview mDayOverview;

    public static DailyTimeSheetFragment getInstance(DayOverview dayOverview) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_DAY_OVERVIEW, dayOverview);
        DailyTimeSheetFragment fragment = new DailyTimeSheetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public DailyTimeSheetFragment() {
        // Required empty public constructor
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
}

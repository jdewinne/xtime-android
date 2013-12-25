package com.xebia.xtime.dayoverview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xebia.xtime.R;
import com.xebia.xtime.shared.model.DayOverview;

public class DayOverviewActivity extends ActionBarActivity {

    public static final String EXTRA_DAY_OVERVIEW = "com.xebia.xtime.dayoverview.DAY_OVERVIEW";
    public static final String EXTRA_WEEK_OVERVIEW = "com.xebia.xtime.dayoverview.WEEK_OVERVIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_overview);

        if (savedInstanceState == null) {
            DayOverview dayOverview = getIntent().getParcelableExtra(EXTRA_DAY_OVERVIEW);
            getSupportFragmentManager().beginTransaction().add(R.id.container,
                    PlaceholderFragment.getInstance(dayOverview)).commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public static PlaceholderFragment getInstance(DayOverview dayOverview) {
            Bundle args = new Bundle();
            args.putParcelable(EXTRA_DAY_OVERVIEW, dayOverview);
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_day_overview, container, false);

            DayOverview overview = getArguments().getParcelable(EXTRA_DAY_OVERVIEW);
            if (null != overview) {
                TextView dayView = (TextView) view.findViewById(R.id.day);
                dayView.setText(overview.getDate().toString() + ", " +
                        overview.getTimeRegistrations() + " projects registered");
            }

            return view;
        }
    }

}

package com.xebia.xtime.weekoverview;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xebia.xtime.R;
import com.xebia.xtime.weekoverview.model.TimeCell;
import com.xebia.xtime.weekoverview.model.TimeSheetRow;
import com.xebia.xtime.weekoverview.model.WeekOverview;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WeekOverviewFragment extends Fragment implements LoaderManager
        .LoaderCallbacks<WeekOverview> {

    private static final String ARG_START_DATE = "start_date";
    private static final String TAG = "WeekOverviewFragment";
    private Date mStartDate;
    private WeekOverview mOverview;
    private OnFragmentInteractionListener mListener;
    private TextView mContentView;
    private View mBusyIndicator;
    private View mMainView;

    public WeekOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * @param date Date indicating the week to display
     * @return A new instance of fragment WeekOverviewFragment.
     */
    public static WeekOverviewFragment newInstance(Date date) {
        WeekOverviewFragment fragment = new WeekOverviewFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_START_DATE, date.getTime());
        fragment.setArguments(args);
        return fragment;
    }

    public static String getTitle(Date startDate) {
        Date endDate = new Date(startDate.getTime() + 6 * DateUtils.DAY_IN_MILLIS);
        DateFormat formatter = new SimpleDateFormat("dd/MM");
        return formatter.format(startDate) + " - " + formatter.format(endDate);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        mBusyIndicator = view.findViewById(R.id.week_overview_busy);
        mMainView = view.findViewById(R.id.week_overview_main);
        mContentView = (TextView) view.findViewById(R.id.content);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " +
                    "OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        showRows();
    }

    private void showRows() {
        if (null == mOverview || null == mOverview.getTimeSheetRows()) {
            // no data to show
            Log.d(TAG, "No overview data to show");
            return;
        }

        String content = "";
        double totalHours = 0;
        for (TimeSheetRow row : mOverview.getTimeSheetRows()) {
            content += row.getClientName() + " " + row.getProjectName();
            double hours = 0;
            for (TimeCell timeCell : row.getTimeCells()) {
                hours += timeCell.getHour();
            }
            totalHours += hours;
            content += ": " + hours + "h\n";
        }
        content += "Total: " + totalHours;

        mContentView.setText(content);
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
                    mMainView.setVisibility(busy ? View.GONE : View.VISIBLE);
                    mBusyIndicator.setVisibility(busy ? View.VISIBLE : View.GONE);
                }
            });
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

package com.xebia.xtime.weekoverview;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xebia.xtime.R;
import com.xebia.xtime.weekoverview.model.WeekOverview;

import java.text.SimpleDateFormat;
import java.util.Date;


public class WeekOverviewFragment extends Fragment implements LoaderManager
        .LoaderCallbacks<WeekOverview> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DATE = "date";
    private static final String TAG = "WeekOverviewFragment";
    private Date mDate;
    private OnFragmentInteractionListener mListener;
    private TextView mCountView;

    public WeekOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param date Date indicating the week to display
     * @return A new instance of fragment WeekOverviewFragment.
     */
    public static WeekOverviewFragment newInstance(Date date) {
        WeekOverviewFragment fragment = new WeekOverviewFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DATE, date.getTime());
        fragment.setArguments(args);
        return fragment;
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
            mDate = new Date(getArguments().getLong(ARG_DATE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_week_overview, container, false);

        TextView titleView = (TextView) view.findViewById(R.id.title);
        titleView.setText(new SimpleDateFormat("yyyy-MM-dd").format(mDate));

        mCountView = (TextView) view.findViewById(R.id.count);
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
        return new WeekOverviewLoader(getActivity(), mDate);
    }

    @Override
    public void onLoadFinished(Loader<WeekOverview> loader, WeekOverview overview) {
        if (null == overview) {
            Log.d(TAG, "Loading week " + mDate + " failed");
        } else {
            Log.d(TAG, "Loaded overview data for week " + mDate);
            mCountView.setText(overview.toString());
        }
    }

    @Override
    public void onLoaderReset(Loader<WeekOverview> loader) {
        // nothing to do
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

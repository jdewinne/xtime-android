package com.xebia.xtime.editor;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.xebia.xtime.R;
import com.xebia.xtime.shared.model.TimeSheetEntry;

import java.text.NumberFormat;

public class EditTimeSheetFragment extends Fragment {

    private static final String ARG_TIME_SHEET = "TIME_SHEET";
    private static final String TAG = "EditTimeSheetFragment";
    private TimeSheetEntry mTimeSheetEntry;
    private EditText mProjectView;
    private EditText mWorkTypeView;
    private EditText mTimeView;
    private Listener mListener;

    public EditTimeSheetFragment() {
        // required empty constructor
    }

    public static EditTimeSheetFragment getInstance(TimeSheetEntry entry) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_TIME_SHEET, entry);
        EditTimeSheetFragment fragment = new EditTimeSheetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != getArguments()) {
            mTimeSheetEntry = getArguments().getParcelable(ARG_TIME_SHEET);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_time_cell, container, false);
        if (null == rootView) {
            return null;
        }

        // link the views
        mProjectView = (EditText) rootView.findViewById(R.id.project);
        mWorkTypeView = (EditText) rootView.findViewById(R.id.work_type);
        mTimeView = (EditText) rootView.findViewById(R.id.time);

        // prefill the views
        if (null != mTimeSheetEntry) {
            mProjectView.setText(mTimeSheetEntry.getProject().getDescription());
            mProjectView.setEnabled(false);
            mWorkTypeView.setText(mTimeSheetEntry.getWorkType().getDescription());
            mWorkTypeView.setEnabled(false);
            mTimeView.setText(NumberFormat.getNumberInstance()
                    .format(mTimeSheetEntry.getTimeCell().getHours()));
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_time_cell, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            onSaveClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " +
                    "EditTimeSheetFragment.Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void onSaveClick() {
        // TODO
        Log.d(TAG, "Save: " + mProjectView.getText() + ", " + mWorkTypeView.getText() + ", " +
                "" + mTimeView.getText());

        mListener.onChangesSaved();
    }

    /**
     * Interface for listening to events from a EditTimeSheetFragment.
     */
    public interface Listener {
        public abstract void onChangesSaved();
    }
}

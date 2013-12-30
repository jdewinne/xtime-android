package com.xebia.xtime.editor;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.xebia.xtime.R;
import com.xebia.xtime.shared.model.TimeSheetEntry;

import java.text.NumberFormat;

public class EditTimeSheetFragment extends Fragment {

    private static final String ARG_TIME_SHEET = "TIME_SHEET";
    private static final String TAG = "EditTimeSheetFragment";
    private TimeSheetEntry mTimeSheetEntry;
    private EditText mProjectView;
    private EditText mWorkTypeView;
    private EditText mDescriptionView;
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
        mDescriptionView = (EditText) rootView.findViewById(R.id.description);
        mTimeView = (EditText) rootView.findViewById(R.id.time);

        // prefill the views
        if (null != mTimeSheetEntry) {
            mProjectView.setText(mTimeSheetEntry.getProject().getDescription());
            mProjectView.setEnabled(false);
            mWorkTypeView.setText(mTimeSheetEntry.getWorkType().getDescription());
            mWorkTypeView.setEnabled(false);
            mDescriptionView.setText(mTimeSheetEntry.getDescription());
            mDescriptionView.setEnabled(false);
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

        if (TextUtils.isEmpty(mTimeView.getText())) {
            mTimeView.setError(getActivity().getString(R.string.error_field_required));
            mTimeView.requestFocus();
            return;
        }

        double time = getTimeInput();
        if (time < 0) {
            mTimeView.setError(getActivity().getString(R.string.error_invalid_time));
            mTimeView.requestFocus();
            return;
        }

        mTimeSheetEntry.getTimeCell().setHours(time);
        new SaveTask().execute(mTimeSheetEntry);
    }

    private double getTimeInput() {
        double time = -1;
        String timeString = mTimeView.getText() + "";
        try {
            time = Double.parseDouble(timeString);
        } catch (NumberFormatException e) {
            Log.w(TAG, "Failed to parse time input: " + mTimeView.getText());
        }
        return time;
    }

    /**
     * Interface for listening to events from a EditTimeSheetFragment.
     */
    public interface Listener {
        public abstract void onChangesSaved();
    }

    /**
     * Asynchronous task to save the changes to this time cell.
     */
    private class SaveTask extends AsyncTask<TimeSheetEntry, Void, Boolean> {

        @Override
        protected Boolean doInBackground(TimeSheetEntry... params) {
            if (null == params || params.length < 1) {
                Log.d(TAG, "Missing required parameter!");
                return null;
            }
            TimeSheetEntry entry = params[0];
            return new SaveTimeSheetRequest(entry).submit();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (null != result && result) {
                mListener.onChangesSaved();
            } else {
                Toast.makeText(getActivity(), R.string.toast_request_failed,
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}

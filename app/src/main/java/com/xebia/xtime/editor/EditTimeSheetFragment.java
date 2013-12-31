package com.xebia.xtime.editor;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.xebia.xtime.R;
import com.xebia.xtime.editor.worktypesloader.WorkTypeListLoader;
import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.TimeSheetEntry;
import com.xebia.xtime.shared.model.WorkType;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditTimeSheetFragment extends Fragment implements LoaderManager
        .LoaderCallbacks<List<WorkType>> {

    private static final String ARG_TIME_SHEET = "time_sheet";
    private static final String ARG_PROJECTS = "projects";
    private static final String TAG = "EditTimeSheetFragment";
    private TimeSheetEntry mTimeSheetEntry;
    private Spinner mProjectView;
    private EditText mWorkTypeView;
    private EditText mDescriptionView;
    private EditText mTimeView;
    private Listener mListener;
    private List<Project> mProjects;
    private List<WorkType> mWorkTypes;

    public EditTimeSheetFragment() {
        // required empty constructor
    }

    public static EditTimeSheetFragment getInstance(TimeSheetEntry entry,
                                                    ArrayList<Project> projects) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_TIME_SHEET, entry);
        args.putParcelableArrayList(ARG_PROJECTS, projects);
        EditTimeSheetFragment fragment = new EditTimeSheetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != getArguments()) {
            mTimeSheetEntry = getArguments().getParcelable(ARG_TIME_SHEET);
            mProjects = getArguments().getParcelableArrayList(ARG_PROJECTS);
            mWorkTypes = new ArrayList<WorkType>();
        }

        if (null != mTimeSheetEntry) {
            Bundle args = new Bundle();
            args.putParcelable("project", mTimeSheetEntry.getProject());
            args.putLong("date", mTimeSheetEntry.getTimeCell().getEntryDate().getTime());
            getActivity().getSupportLoaderManager().initLoader(0, args, this);
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
        mProjectView = (Spinner) rootView.findViewById(R.id.project);
        mWorkTypeView = (EditText) rootView.findViewById(R.id.work_type);
        mDescriptionView = (EditText) rootView.findViewById(R.id.description);
        mTimeView = (EditText) rootView.findViewById(R.id.time);

        // set up the views
        if (null != mTimeSheetEntry) {
            initProjectView();
            initWorkTypeView();
            mDescriptionView.setText(mTimeSheetEntry.getDescription());
            mDescriptionView.setEnabled(false);
            mTimeView.setText(NumberFormat.getNumberInstance()
                    .format(mTimeSheetEntry.getTimeCell().getHours()));
        }

        return rootView;
    }

    private void initWorkTypeView() {
        mWorkTypeView.setText(mTimeSheetEntry.getWorkType().getDescription());
        mWorkTypeView.setEnabled(false);
    }

    private void initProjectView() {
        // set up the spinner adapter
        ArrayAdapter<Project> adapter = new ArrayAdapter<Project>(getActivity(),
                android.R.layout.simple_spinner_item, mProjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mProjectView.setAdapter(adapter);

        // match projects on project ID, somehow the project description is not constant
        // e.g. 'Internal projects' vs. 'Internal Project'
        int index = -1;
        String projectId = mTimeSheetEntry.getProject().getId();
        for (int i = 0; i < mProjects.size(); i++) {
            if (mProjects.get(i).getId().equals(projectId)) {
                index = i;
                break;
            }
        }

        if (index >= 0) {
            mProjectView.setSelection(index);
            mProjectView.setEnabled(false);
        }
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

    @Override
    public Loader<List<WorkType>> onCreateLoader(int id, Bundle args) {
        Project project = args.getParcelable("project");
        Date date = new Date(args.getLong("date"));
        return new WorkTypeListLoader(getActivity(), project, date);
    }

    @Override
    public void onLoadFinished(Loader<List<WorkType>> listLoader, List<WorkType> workTypes) {
        if (null == workTypes) {
            Toast.makeText(getActivity(), R.string.toast_work_types_fail, Toast.LENGTH_LONG).show();
            return;
        }

        mWorkTypes.addAll(workTypes);

        Log.d(TAG, "Loaded " + workTypes.size() + " work types");
        for (WorkType workType : mWorkTypes) {
            Log.d(TAG, workType.getDescription());
        }
    }

    @Override
    public void onLoaderReset(Loader<List<WorkType>> listLoader) {
        mWorkTypes.clear();
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
                Toast.makeText(getActivity(), R.string.toast_save_fail, Toast.LENGTH_LONG).show();
            }
        }
    }
}

package com.xebia.xtime.editor.save;

import android.os.AsyncTask;

import com.xebia.xtime.shared.model.TimeEntry;

import org.apache.http.auth.AuthenticationException;

/**
 * Asynchronous task to save the changes to a time sheet entry
 */
public class SaveEntryTask extends AsyncTask<TimeEntry, Void, Boolean> {

    private final Listener mListener;

    public SaveEntryTask(Listener listener) {
        mListener = listener;
    }

    @Override
    protected Boolean doInBackground(TimeEntry... params) {
        if (null == params || params.length < 1) {
            throw new NullPointerException("Missing TimeSheetEntry parameter");
        }

        String response;
        try {
            response = new SaveEntryRequest(params[0]).submit();
        } catch (AuthenticationException e) {
            return null;
        }

        if (null != response) {
            return true;
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mListener.onSaveComplete(result);
    }

    /**
     * Interface for listening for results from the SaveEntryTask
     */
    public interface Listener {
        public abstract void onSaveComplete(Boolean result);
    }
}

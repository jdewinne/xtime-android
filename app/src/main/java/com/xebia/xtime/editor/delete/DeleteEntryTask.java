package com.xebia.xtime.editor.delete;

import android.os.AsyncTask;

import com.xebia.xtime.shared.model.TimeSheetEntry;

import org.apache.http.auth.AuthenticationException;

/**
 * Asynchronous task to delete a time sheet entry
 */
public class DeleteEntryTask extends AsyncTask<TimeSheetEntry, Void, Boolean> {

    private final Listener mListener;

    public DeleteEntryTask(Listener listener) {
        mListener = listener;
    }

    @Override
    protected Boolean doInBackground(TimeSheetEntry... params) {
        if (null == params || params.length < 1) {
            throw new NullPointerException("Missing TimeSheetEntry parameter");
        }

        try {
            String response = new DeleteEntryRequest(params[0]).submit();
            return DeleteEntryResponseParser.parse(response);
        } catch (AuthenticationException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mListener.onDeleteComplete(result);
    }

    /**
     * Interface for listening for results from the DeleteEntryTask
     */
    public interface Listener {
        public abstract void onDeleteComplete(Boolean result);
    }
}

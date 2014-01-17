package com.xebia.xtime.monthoverview.approve;

import android.os.AsyncTask;

import org.apache.http.auth.AuthenticationException;

import java.util.Date;

/**
 * Asynchronous task to submit a request to approve the data for a month.
 */
public class ApproveTask extends AsyncTask<Double, Void, Boolean> {

    private final Listener mListener;

    public ApproveTask(Listener listener) {
        mListener = listener;
    }

    @Override
    protected Boolean doInBackground(Double... params) {
        if (null == params || params.length < 2) {
            throw new NullPointerException("Missing month or hours parameter");
        }
        double hours = params[0];
        long month = Math.round(params[1]);

        try {
            new ApproveRequest(hours, new Date(month)).submit();
        } catch (AuthenticationException e) {
            return null;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mListener.onApproveComplete(result);
    }

    /**
     * Interface for listening for results from the SaveEntryTask
     */
    public interface Listener {
        public abstract void onApproveComplete(Boolean result);
    }

}

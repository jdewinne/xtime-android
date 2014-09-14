package com.xebia.xtime.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.xebia.xtime.authenticator.Authenticator;
import com.xebia.xtime.content.XTimeContract.Tasks;
import com.xebia.xtime.content.XTimeContract.TimeEntries;

import java.io.IOException;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "SyncAdapter";
    private static final int MAX_RETRIES = 1;
    private int mRetryCount;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "Performing sync for " + account);

        String cookie = "";
        try {
            cookie = getCookie(account);
            new SyncHelper().performSync(cookie, provider, syncResult);

            // notify any listeners that the sync is done
            getContext().getContentResolver().notifyChange(TimeEntries.CONTENT_URI, null);
            getContext().getContentResolver().notifyChange(Tasks.CONTENT_URI, null);

        } catch (IOException e) {
            Log.w(TAG, "Failed to authenticate! Connection error: '" + e.getMessage() + "'");
            syncResult.stats.numIoExceptions++;
        } catch (OperationCanceledException | AuthenticatorException e) {
            Log.w(TAG, "Failed to sync! Authentication error: '" + e.getMessage() + "'");
            syncResult.stats.numAuthExceptions++;
        } catch (SyncHelper.CookieExpiredException e) {
            Log.w(TAG, "Cookie expired!");
            AccountManager accountManager = AccountManager.get(getContext());
            accountManager.invalidateAuthToken(Authenticator.ACCOUNT_TYPE, cookie);
            if (mRetryCount < MAX_RETRIES) {
                mRetryCount++;
                Log.d(TAG, "Retry sync...");
                onPerformSync(account, extras, authority, provider, syncResult);
            } else {
                Log.d(TAG, "Too many failed retries");
                syncResult.stats.numAuthExceptions++;
            }
        }
    }

    private String getCookie(final Account account) throws AuthenticatorException,
            OperationCanceledException, IOException {
        AccountManager accountManager = AccountManager.get(getContext());
        return accountManager.blockingGetAuthToken(account, Authenticator.AUTH_TYPE, false);
    }
}

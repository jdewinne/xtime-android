package com.xebia.xtime.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.xebia.xtime.authenticator.Authenticator;
import com.xebia.xtime.shared.model.XTimeOverview;
import com.xebia.xtime.shared.parser.XTimeOverviewParser;
import com.xebia.xtime.shared.webservice.XTimeWebService;

import java.io.IOException;
import java.util.Date;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "SyncAdapter";
    private static final int MAX_RETRIES = 1;
    private int mRetryCount;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        // This form of the constructor maintains compatibility with Android 3.0 and up
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "Performing sync for account '" + account.name + "'");
        String cookie = "";
        try {
            cookie = getCookie(account);
            XTimeOverview overview = requestOverview(cookie);
            if (null != overview) {
                Log.d(TAG, "Parsed overview");
                mRetryCount = 0;
                // TODO: Store overview
            } else {
                Log.w(TAG, "Parse failed");
                syncResult.stats.numParseExceptions++;
            }
        } catch (OperationCanceledException | AuthenticatorException e) {
            Log.w(TAG, "Failed to sync! Authentication error: '" + e.getMessage() + "'");
            syncResult.stats.numAuthExceptions++;
        } catch (IOException e) {
            Log.w(TAG, "Failed to sync! Connection error: '" + e.getMessage() + "'");
            syncResult.stats.numIoExceptions++;
        } catch (CookieExpiredException e) {
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

    private XTimeOverview requestOverview(final String cookie) throws CookieExpiredException,
            IOException {
        String response = XTimeWebService.getInstance().getWeekOverview(new Date(), cookie);
        if (response.contains("UsernameNotFoundException")) {
            throw new CookieExpiredException("UsernameNotFoundException");
        }
        return XTimeOverviewParser.parse(response);
    }

    private static class CookieExpiredException extends Exception {
        public CookieExpiredException(String message) {
            super(message);
        }
    }
}

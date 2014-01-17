package com.xebia.xtime.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xebia.xtime.R;

import org.apache.http.auth.AuthenticationException;

/**
 * Activity which displays a login screen to the user
 */
public class LoginActivity extends ActionBarActivity {

    public static final String PREF_USERNAME = "com.xebia.xtime.extra.USERNAME";
    public static final String PREF_PASSWORD = "com.xebia.xtime.extra.PASSWORD";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private View mLoginStatusView;
    private TextView mLoginStatusMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        String username = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(PREF_USERNAME, null);
        mUsernameView.setText(username);

        mPasswordView = (EditText) findViewById(R.id.password);
        String password = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(PREF_PASSWORD, null);
        mPasswordView.setText(password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                Log.d("TAG", "event " + id);
                if (id == R.id.login || id == EditorInfo.IME_NULL ||
                        id == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mLoginStatusView = findViewById(R.id.login_status);
        mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form. If there are form
     * errors (invalid email, missing fields, etc.), the errors are presented and no actual login
     * attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // get the username and password
        String username = ("" + mUsernameView.getText()).trim();
        String password = ("" + mPasswordView.getText()).trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first form field with an error
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to perform the login attempt
            mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
            showProgress(true);
            mAuthTask = new UserLoginTask();
            mAuthTask.execute(username.trim(), password.trim());
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allows for very easy
        // animations. If available, use these APIs to fade-in the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener
                    (new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener
                    (new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login task used to authenticate the user. Expects Strings for
     * username and password as execution parameters.
     */
    private class UserLoginTask extends AsyncTask<String, Void, Boolean> {

        private String mUsername;
        private String mPassword;

        @Override
        protected Boolean doInBackground(String... params) {
            if (null == params || params.length < 2) {
                return null;
            }

            // get login arguments
            mUsername = params[0];
            mPassword = params[1];

            String response;
            try {
                response = new LoginRequest(mUsername, mPassword).submit();
            } catch (AuthenticationException e) {
                return false;
            }

            if (null != response) {
                return true;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            mAuthTask = null;
            showProgress(false);

            if (null == result) {
                Toast.makeText(LoginActivity.this, R.string.error_request_failed,
                        Toast.LENGTH_LONG).show();

            } else if (result) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences
                        (LoginActivity.this).edit();
                editor.putString(PREF_USERNAME, mUsername);
                editor.putString(PREF_PASSWORD, mPassword);
                editor.commit();

                setResult(RESULT_OK);
                finish();

            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

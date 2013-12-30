package com.xebia.xtime.login;

import android.util.Log;

import com.xebia.xtime.shared.XTimeRequest;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;

public class LoginRequest extends XTimeRequest {

    private static final String TAG = "LoginRequest";
    private static final String LOGIN_URL = "https://xtime.xebia.com/xtime/j_spring_security_check";
    private String mUsername;
    private String mPassword;

    /**
     * Constructor.
     *
     * @param username Xebia username, without the '@xebia.com' postfix
     * @param password Xebia password (unhashed!)
     */
    public LoginRequest(String username, String password) {
        mUsername = username;
        mPassword = password;
    }

    /**
     * Submits the login request to the XTime backend.
     *
     * @return <code>true</code> if login was successful, <code>false</code> if the login was
     * denied, or <code>null</code> if the request failed for another reason.
     */
    public Boolean submit() {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(LOGIN_URL);

            // setting a cookie manager magically makes sure the cookies are stored
            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);

            // blindly trust all certificates
            trustAllCertificates();

            urlConnection = (HttpURLConnection) url.openConnection();

            // do not follow redirects, we need the Location header to see if the login worked
            urlConnection.setInstanceFollowRedirects(false);

            urlConnection.setDoOutput(true);
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            writeCredentials(out);

            // login was successful if the Location header does not redirect to an error page
            String location = urlConnection.getHeaderField("Location");
            return location != null && !location.contains("error=true");

        } catch (IOException e) {
            Log.w(TAG, "Login request failed", e);
            return null;
        } catch (GeneralSecurityException e) {
            Log.e(TAG, "Security problem performing request", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private String getLoginData() throws IOException {
        String username = URLEncoder.encode(mUsername, "UTF-8");
        String password = URLEncoder.encode(mPassword, "UTF-8");
        return "j_username=" + username + "&j_password=" + password;
    }

    private void writeCredentials(OutputStream out) throws IOException {
        String data = getLoginData();
        out.write(data.getBytes());
        out.flush();
        out.close();
    }
}

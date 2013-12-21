package com.xebia.xtime.login;

import android.util.Log;

import com.xebia.xtime.shared.XTimeRequest;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.List;

public class LoginRequest extends XTimeRequest {

    private static final String TAG = "LoginRequest";
    private static final String LOGIN_URL = "https://xtime.xebia.com/xtime/j_spring_security_check";
    private String mUsername;
    private String mPassword;

    public LoginRequest(String username, String password) {
        mUsername = username;
        mPassword = password;
    }

    public boolean submit() {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(LOGIN_URL);

            // TODO: do not blindly allow all host names...
            trustAllCertificates();

            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);

            urlConnection = (HttpURLConnection) url.openConnection();

            // do not follow redirects, we need the Location header to see if the login worked
            urlConnection.setInstanceFollowRedirects(false);

            urlConnection.setDoOutput(true);
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            writeCredentials(out);

            String setCookie = urlConnection.getHeaderField("Set-Cookie");
            List<HttpCookie> cookies = HttpCookie.parse(setCookie);
            for (HttpCookie cookie : cookies) {
                cookieManager.getCookieStore().add(new URI("https://xtime.xebia.com/"), cookie);
            }

            // login was successful if the Location header does not redirect to an error page
            String location = urlConnection.getHeaderField("Location");
            return location != null && !location.contains("error=true");

        } catch (IOException e) {
            Log.w(TAG, "Login request failed", e);
            return false;
        } catch (GeneralSecurityException e) {
            Log.e(TAG, "Security problem performing request", e);
            return false;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
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

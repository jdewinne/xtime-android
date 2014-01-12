package com.xebia.xtime.login;

import com.xebia.xtime.shared.XTimeRequest;

import org.apache.http.auth.AuthenticationException;

import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URLEncoder;

public class LoginRequest extends XTimeRequest {

    private static final String XTIME_URL = "https://xtime.xebia.com/xtime/j_spring_security_check";
    private final String mUsername;
    private final String mPassword;

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

    @Override
    public String submit() throws AuthenticationException {

        // setting a cookie manager magically makes sure the cookies are stored
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        return super.submit();
    }

    @Override
    public String getContentType() {
        return CONTENT_TYPE_FORM;
    }

    @Override
    public String getRequestData() {
        String username = null;
        String password = null;
        try {
            username = URLEncoder.encode(mUsername, "UTF-8");
            password = URLEncoder.encode(mPassword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // should not happen
        }
        return "j_username=" + username + "&j_password=" + password;
    }

    @Override
    public String getUrl() {
        return XTIME_URL;
    }
}

package com.xebia.xtime;

import com.xebia.xtime.shared.XTimeRequest;

import org.apache.http.auth.AuthenticationException;

import java.net.CookieHandler;
import java.net.CookieManager;

public class LogoutRequest extends XTimeRequest {

    private static final String XTIME_URL = "https://xtime.xebia.com/xtime/logout.jsp";

    @Override
    public String submit() throws AuthenticationException {

        // setting a cookie manager magically makes sure the cookies are stored
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        return super.submit();
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public String getRequestData() {
        return null;
    }

    @Override
    public String getUrl() {
        return XTIME_URL;
    }
}

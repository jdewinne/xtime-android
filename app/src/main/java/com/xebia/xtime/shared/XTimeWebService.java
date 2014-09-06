package com.xebia.xtime.shared;

import android.net.Uri;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class XTimeWebService {

    private static final XTimeWebService INSTANCE = new XTimeWebService();
    private final OkHttpClient mHttpClient = new OkHttpClient();
    private static final Uri BASE_URI = Uri.parse("https://xtime.xebia.com/xtime");
    private static final MediaType FORM_ENCODED = MediaType.parse
            ("application/x-www-form-urlencoded");

    private XTimeWebService() {
        // do not instantiate
    }

    public static XTimeWebService getInstance() {
        return INSTANCE;
    }

    /**
     * @param username XTime account username (without '@xebia.com' postfix)
     * @param password XTime account password
     * @return Cookie header value
     * @throws IOException
     */
    public String login(final String username, final String password) throws IOException {
        // create request data
        String requestData;
        try {
            requestData = "j_username=" + URLEncoder.encode(username, "UTF-8")
                    + "&j_password=" + URLEncoder.encode(password, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // should not happen
            throw new RuntimeException(e);
        }
        // perform request
        Request request = new Request.Builder()
                .url(BASE_URI.buildUpon().appendPath("j_spring_security_check").build().toString())
                .post(RequestBody.create(FORM_ENCODED, requestData))
                .build();
        Response response = mHttpClient.newCall(request).execute();
        while (null != response.priorResponse()) {
            response = response.priorResponse();
        }
        return response.header("Set-Cookie");
    }
}

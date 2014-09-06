package com.xebia.xtime.shared.webservice;

import android.net.Uri;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class XTimeWebService {

    private static final XTimeWebService INSTANCE = new XTimeWebService();
    private static final Uri BASE_URI = Uri.parse("https://xtime.xebia.com/xtime");
    private final OkHttpClient mHttpClient = new OkHttpClient();

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
        RequestBody body = new LoginRequestBuilder().username(username).password(password).build();
        // perform request
        Request request = new Request.Builder()
                .url(BASE_URI.buildUpon().appendPath("j_spring_security_check").build().toString())
                .post(body)
                .build();
        Response response = mHttpClient.newCall(request).execute();
        while (null != response.priorResponse()) {
            response = response.priorResponse();
        }
        return response.header("Set-Cookie");
    }
}

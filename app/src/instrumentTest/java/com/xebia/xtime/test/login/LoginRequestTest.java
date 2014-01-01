package com.xebia.xtime.test.login;

import com.xebia.xtime.login.LoginRequest;

import junit.framework.TestCase;

import java.io.IOException;

public class LoginRequestTest extends TestCase {

    public void testRequestData() {
        LoginRequest request = new LoginRequest("simple", "characters");
        String expected = "j_username=simple&j_password=characters";
        String data = null;
        try {
            data = request.getRequestData();
        } catch (IOException e) {
            // should not happen
        }
        assertEquals(expected, data);
    }

    public void testRequestDataEncoding() {
        LoginRequest request = new LoginRequest("encode", "ok?");
        String encoded = "j_username=encode&j_password=ok%3F";
        String data = null;
        try {
            data = request.getRequestData();
        } catch (IOException e) {
            // should not happen
        }
        assertEquals(encoded, data);
    }
}

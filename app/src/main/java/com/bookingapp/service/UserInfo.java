package com.bookingapp.service;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;

public class UserInfo {
    private static String token;

    public void setToken(String t) {
        token = t;
    }

    public String getToken() {
        return token;
    }

    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

    public JSONObject getPayload() throws JSONException {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = token.split("\\.");
        JSONObject header = new JSONObject(decode(chunks[0]));
        JSONObject payload = new JSONObject(decode(chunks[1]));
        return payload;
    }

    public String getEmail() throws JSONException {
        return getPayload().getString("sub");
    }
}

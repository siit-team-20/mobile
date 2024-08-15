package com.bookingapp.service;

import com.bookingapp.model.UserType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;

public class UserInfo {
    private static String token;

    public static void setToken(String t) {
        token = t;
    }

    public static String getToken() {
        return token;
    }

    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

    public static JSONObject getPayload() throws JSONException {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = token.split("\\.");
        JSONObject header = new JSONObject(decode(chunks[0]));
        JSONObject payload = new JSONObject(decode(chunks[1]));
        return payload;
    }

    public static String getEmail() throws JSONException {
        return getPayload().getString("sub");
    }

    public static String getName() throws JSONException {
        return getPayload().getString("name");
    }

    public static String getSurname() throws JSONException {
        return getPayload().getString("surname");
    }

    public static UserType getType() throws JSONException {
        return UserType.valueOf(getPayload().getString("type"));
    }
}

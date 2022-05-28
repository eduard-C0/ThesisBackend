package com.example.backend.config;

import com.example.backend.domain.User;
import com.google.gson.Gson;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class DecodedJWT {
    public User user;
    public Object roles;
    public Object exp;

    public static DecodedJWT getDecoded(String encodedToken) throws UnsupportedEncodingException {
        try {
            String[] pieces = encodedToken.split("\\.");
            String b64payload = pieces[1];
            String jsonString = new String(Base64.decodeBase64(b64payload), StandardCharsets.UTF_8);

            return new Gson().fromJson(jsonString, DecodedJWT.class);
        }catch (Exception e) {
            throw new UnsupportedEncodingException();
        }
    }
}

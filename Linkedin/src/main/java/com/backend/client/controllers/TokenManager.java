package com.backend.client.controllers;

import org.json.JSONObject;

import java.util.prefs.Preferences;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.exceptions.JWTDecodeException;


public class TokenManager {

    private static final Preferences prefs = Preferences.userNodeForPackage(TokenManager.class);
    private static final String TOKEN_KEY = "authToken";

    /**
     * Store the token in Preferences storage.
     *
     * @param token The token to store.
     */
    public static void storeToken(String token) {
        if (token != null && !token.trim().isEmpty()) {
            prefs.put(TOKEN_KEY, token);
        } else {
            System.out.println("Invalid token. Cannot store.");
        }
    }

    /**
     * Retrieve the token from Preferences storage.
     *
     * @return The stored token, or null if not found.
     */
    public static String getToken() {
        String token = prefs.get(TOKEN_KEY, null);
        if (token == null) {
            System.out.println("No token found.");
        }
        return token;
    }

    /**
     * Clear the stored token from Preferences storage.
     */
    public static void clearToken() {
        prefs.remove(TOKEN_KEY);
    }

    /**
     * Decode the JWT token and extract the email.
     *
     * @param token The JWT token.
     * @return The email extracted from the token, or null if not found.
     */
    public static String getEmailFromToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim("email").asString();
        } catch (JWTDecodeException e) {
            System.err.println("Failed to decode JWT token: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieve the stored token and extract the email from it.
     *
     * @return The email extracted from the stored token, or null if not found.
     */
    public static String getEmailFromStoredToken() {
        String token = getToken();
        if (token != null) {
            return getEmailFromToken(token);
        }
        return null;
    }

    /**
     * Extract token from a JSON response string.
     *
     * @param jsonResponse JSON response containing the token.
     * @return Extracted token, or null if not found.
     */
    public static String extractTokenFromResponse(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            return jsonObject.optString("token", null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
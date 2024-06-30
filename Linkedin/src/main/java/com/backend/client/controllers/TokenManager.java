package com.backend.client.controllers;

import org.json.JSONObject;

import java.util.prefs.Preferences;

public class TokenManager {

    private static final Preferences prefs = Preferences.userNodeForPackage(TokenManager.class);
    private static final String TOKEN_KEY = "authToken";

    /**
     * Store the token in the Preferences storage.
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


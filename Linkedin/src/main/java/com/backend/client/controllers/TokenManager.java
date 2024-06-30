package com.backend.client.controllers;

import java.util.prefs.Preferences;

public class TokenManager {

    private static final Preferences prefs = Preferences.userNodeForPackage(TokenManager.class);
    private static final String TOKEN_KEY = "authToken";

    // Store the token
    public static void storeToken(String token) {
        prefs.put(TOKEN_KEY, token);
    }

    // Retrieve the token
    public static String getToken() {
        return prefs.get(TOKEN_KEY, null);
    }

    // Clear the token (e.g., on logout)
    public static void clearToken() {
        prefs.remove(TOKEN_KEY);
    }
}

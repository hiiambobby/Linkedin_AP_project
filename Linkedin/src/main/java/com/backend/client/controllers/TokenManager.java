package com.backend.client.controllers;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TokenManager {
    private static final String TOKEN_FILE = "token.txt";

    public static void saveToken(String token) {
        try (FileWriter fileWriter = new FileWriter(TOKEN_FILE)) {
            fileWriter.write(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadToken() {
        try {
            return new String(Files.readAllBytes(Paths.get(TOKEN_FILE)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

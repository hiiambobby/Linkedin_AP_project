package com.backend.client.controllers;

public class ControllerManager {
    private static ProfileController profileController;

    // Set the ProfileController reference
    public static void setProfileController(ProfileController controller) {
        profileController = controller;
    }

    // Get the ProfileController reference
    public static ProfileController getProfileController() {
        return profileController;
    }
}

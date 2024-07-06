package com.backend.client.controllers;

public class ControllerManager {
    private static ProfileController profileController;
    private static TimeLineController timeLineController;

    // Set the ProfileController reference
    public static void setProfileController(ProfileController controller) {
        profileController = controller;
    }

    public static void setTimeLineController(TimeLineController tlController){
        timeLineController = tlController;
    }
    // Get the ProfileController reference
    public static ProfileController getProfileController() {
        return profileController;
    }
    public static TimeLineController getTimeLineController(){return timeLineController;}
}

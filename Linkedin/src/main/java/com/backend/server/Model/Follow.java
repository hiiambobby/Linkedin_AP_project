package com.backend.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Follow {
    @JsonProperty("follower")
    private String follower;

    @JsonProperty("following")
    private String following;

    // Default constructor
    public Follow() {
    }

    // Parameterized constructor
    public Follow(String follower, String following) {
        this.follower = follower;
        this.following = following;
    }

    // Getter and Setter for follower
    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    // Getter and Setter for following
    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    @Override
    public String toString() {
        return "Follow{" +
                "follower='" + follower + '\'' +
                ", following='" + following + '\'' +
                '}';
    }
}

package com.backend.LinkedinServer.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Follow {
@JsonProperty("follower")
    private String follower;
@JsonProperty("followed")
    private String followed;

//empty constructor
    public Follow()
    {

    }

    public Follow(String follower,String followed)
    {
        this.followed = followed;
        this.follower = follower;
    }

    public void setFollowed(String followed) {
        this.followed = followed;
    }

    public String getFollowed() {
        return followed;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getFollower() {
        return follower;
    }
    public String toString() {
        return "Follow{" +
                ", follower='" + follower + '\'' +
                ", followed='" + followed + '\'' +
                '}';
    }
}



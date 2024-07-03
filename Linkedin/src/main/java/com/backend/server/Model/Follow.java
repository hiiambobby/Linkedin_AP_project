package com.backend.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Follow {
@JsonProperty("follower")
    private String follower;
@JsonProperty("following")
    private String following;

//empty constructor
    public Follow()
    {

    }

    public Follow(String follower,String following)
    {
        this.follower = follower;
        this.following = following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getFollowed() {
        return following;
    }

    public String getFollowing() {
        return following;
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
                ", following='" + following + '\'' +
                '}';
    }
}



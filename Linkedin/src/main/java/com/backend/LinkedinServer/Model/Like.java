package com.backend.LinkedinServer.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Like {
    @JsonProperty("liked")
    private boolean liked;

    ///empty constructor
    public Like()
    {

    }
    public Like(boolean liked)
    {
        this.liked = liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
    public boolean getLike()
    {
        return liked;
    }
}

package com.backend.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Comment {
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("comment")
    private String comment;

    public Comment()
    {

    }
    public Comment(String userId,String comment)
    {
        this.comment = comment;
        this.userId = userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}

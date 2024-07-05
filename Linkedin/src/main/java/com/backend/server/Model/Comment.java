package com.backend.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;



public class Comment {

    @JsonProperty("id")
    private int id;

    @JsonProperty("postId")
    private int postId;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("text")
    private String text;

    @JsonProperty("image")
    private String image; // Single image URL

    @JsonProperty("video")
    private String video; // Single video URL

    // Default constructor
    public Comment() {}

    // Parameterized constructor
    public Comment(int postId, String userId, String text, String image, String video) {
        this.postId = postId;
        this.userId = userId;
        this.text = text;
        this.image = image;
        this.video = video;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", postId=" + postId +
                ", userId='" + userId + '\'' +
                ", text='" + text + '\'' +
                ", image='" + image + '\'' +
                ", video='" + video + '\'' +
                '}';
    }
}

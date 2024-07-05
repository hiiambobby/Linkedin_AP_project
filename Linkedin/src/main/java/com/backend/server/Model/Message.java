package com.backend.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {


    @JsonProperty("sender")
    private String sender;

    @JsonProperty("receiver")
    private String receiver;

    @JsonProperty("text")
    private String text;

    @JsonProperty("image")
    private String image;

    @JsonProperty("file")
    private String File;

    @JsonProperty("video")
    private String video;

    // Default constructor for JSON deserialization
    public Message() {
        // Default constructor
    }

    // Constructor with all fields
    public Message(String sender, String receiver, String text, String image, String File,String video) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.image = image;
        this.File = File;
        this.video = video;
    }

    // Getters and Setters
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
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

    public String getFile() {
        return File;
    }

    public void setFile(String file) {
        this.File = file;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", text='" + text + '\'' +
                ", image='" + image + '\'' +
                ", file='" + File + '\'' +
                ", video='" + video + '\'' +
                '}';
    }
}
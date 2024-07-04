package com.backend.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;

public class Message {

    @JsonProperty("sender")
    private String sender;

    @JsonProperty("receiver")
    private String receiver;

    @JsonProperty("text")
    private String text;

    @JsonProperty("video")
    private String video;

    @JsonProperty("textFile")
    private String textFile;

    // Default constructor for JSON deserialization
    public Message() {
        // Default constructor
    }

    // Constructor with all fields
    public Message(String sender, String receiver, String text, String video, String textFile) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.video = video;
        this.textFile = textFile;
    }

    // Getters and Setters
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTextFile() {
        return textFile;
    }

    public void setTextFile(String textFile) {
        this.textFile = textFile;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", text='" + text + '\'' +
                ", video='" + video + '\'' +
                ", textFile='" + textFile + '\'' +
                '}';
    }
}
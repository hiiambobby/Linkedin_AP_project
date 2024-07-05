package com.backend.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.source.tree.UsesTree;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Post {
    @JsonProperty("id")
    private int id;
    @JsonProperty("sender")
    private String sender;

    @JsonProperty("text")
    private String text;


    @JsonProperty("video")
    private List<String> video;

    @JsonProperty("image")
    private List<String> image;


    //default constructor
    public Post() {

    }


    public Post(String sender, String text, List<String> video, List<String> image) {
        this.image = image;
        this.sender = sender;
        this.text = text;
        this.video = video;
    }


    public void setImage(List<String> image) {
        this.image = image;
    }

    public void setVideo(List<String> video) {
        this.video = video;
    }

    public void setText(String text) {
        this.text = text;
    }



    public String getVideoJson() {
        return convertListToJson(video);
    }

    public String getImageJson() {
        return convertListToJson(image);
    }

    private String convertListToJson(List<String> list) {
        try {
            return new ObjectMapper().writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting list to JSON", e);
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }


    public String getText() {
        return text;
    }





    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", text='" + text + '\'' +
                ", video=" + video +
                ", image=" + image +
                '}';
    }

}

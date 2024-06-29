package com.backend.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;

public class Post {
    //for now we will just have text format
    @JsonProperty("text")
    private String text;

    //for saving comments of a post
    @JsonProperty("comments")
    private HashMap<String,ArrayList<Comment>> comments = new HashMap<>();

    public Post(String text)
    {
        this.text = text;
    }
    //default constructor
    Post()
    {

    }
    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

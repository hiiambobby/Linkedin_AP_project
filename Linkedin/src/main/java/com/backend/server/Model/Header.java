package com.backend.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Header {
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("header")
    private String header;

    ///empty constructor
    public Header()
    {

    }
    public Header(String header)
    {
        this.header = header;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}

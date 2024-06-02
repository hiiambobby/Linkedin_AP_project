package com.backend.LinkedinServer.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONPropertyName;

public class Header {
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
    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}

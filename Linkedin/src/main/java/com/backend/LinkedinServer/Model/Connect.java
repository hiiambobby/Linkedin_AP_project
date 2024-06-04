package com.backend.LinkedinServer.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Connect {
    @JsonProperty("senderName")
    private String senderName;

    @JsonProperty("senderFamilyName")
    private String familyName;


}




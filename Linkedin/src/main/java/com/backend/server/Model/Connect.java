package com.backend.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Connect {
    @JsonProperty("sender")
    private String senderName;

    @JsonProperty("receiver")
    private String receiverName;

    @JsonProperty("notes")
    private String notes;
    @JsonProperty("accepted")
    private boolean accepted;

    // No-argument constructor is required for Jackson
    public Connect() {
    }


    public Connect(String senderName, String receiverName, String notes, boolean accepted) {
        this.notes = notes;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.accepted = accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public boolean isAccepted() {
        return accepted;
    }


    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getNotes() {
        return notes;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getSenderName() {
        return senderName;
    }

    @Override
    public String toString() {
        return "Connect{" +
                ", sender='" + senderName + '\'' +
                ", receiver='" + receiverName + '\'' +
                ", notes='" + notes + '\'' +
                ",  accepted='" + accepted + +
                '}';
    }
}

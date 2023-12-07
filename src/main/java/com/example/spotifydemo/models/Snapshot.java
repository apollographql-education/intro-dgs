package com.example.spotifydemo.models;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Snapshot {

    String snapshot_id;

    String error;


    @JsonSetter("snapshot_id")
    public void setId(String snapshot_id) {
        this.snapshot_id = snapshot_id;
    }

    public String getId() {
        return this.snapshot_id;
    }

    public String getError() {
        return this.error;
    }

}
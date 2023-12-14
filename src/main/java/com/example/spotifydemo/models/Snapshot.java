package com.example.spotifydemo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Snapshot(@JsonProperty("snapshot_id") String id, String error) { }
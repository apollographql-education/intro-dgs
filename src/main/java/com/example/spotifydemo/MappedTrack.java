package com.example.spotifydemo;

import com.example.spotifydemo.generated.types.Track;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;

public class MappedTrack extends Track {
    public Track getTrack() {
        return this;
    }
    @JsonSetter("track")
    public void setTrackProperties(JsonNode trackObject) {
        this.setId(trackObject.get("id").asText());
        this.setName(trackObject.get("name").asText());
        this.setDurationMs(trackObject.get("duration_ms").asInt());
        this.setExplicit(trackObject.get("explicit").asBoolean());
        this.setUri(trackObject.get("uri").asText());
    }
}

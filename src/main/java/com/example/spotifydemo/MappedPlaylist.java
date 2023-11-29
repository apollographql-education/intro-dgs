package com.example.spotifydemo;

import com.example.spotifydemo.generated.types.Playlist;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class MappedPlaylist extends Playlist {
    @JsonSetter("tracks")
    public void retrieveTracks(JsonNode tracks) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode items = tracks.get("items");

        List<MappedTrack> trackList = mapper.readValue(items.traverse(), new TypeReference<>() {});
        this.setTracks(trackList.stream().map(MappedTrack::getTrack).toList());
    }
}

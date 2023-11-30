package com.example.spotifydemo.models;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.util.List;

public class FeaturedPlaylists {

    List<MappedPlaylist> playlists;

    @JsonGetter("playlists")
    public List<MappedPlaylist> getPlaylists() {
        return this.playlists;
    }

    @JsonSetter("playlists")
    public void setPlaylists(JsonNode playlists) throws IOException {
        JsonNode playlistItems = playlists.get("items");

        ObjectMapper mapper = new ObjectMapper();
        this.playlists = mapper.readValue(playlistItems.traverse(), new TypeReference<List<MappedPlaylist>>(){});
    }

}

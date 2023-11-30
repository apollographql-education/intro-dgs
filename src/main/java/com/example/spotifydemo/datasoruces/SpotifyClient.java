package com.example.spotifydemo.datasoruces;

import com.example.spotifydemo.models.FeaturedPlaylists;
import com.example.spotifydemo.models.MappedPlaylist;
import com.example.spotifydemo.models.Snapshot;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class SpotifyClient {
    private static String SPOTIFY_API_URL = "http://localhost:3001/v1";
    private WebClient builder = WebClient.builder().baseUrl(SPOTIFY_API_URL).build();

    public FeaturedPlaylists featuredPlaylists() {
        return builder
                .get()
                .uri("/browse/featured-playlists")
                .retrieve()
                .bodyToMono(FeaturedPlaylists.class)
                .block();
    }

    public Snapshot addItemsToPlaylist(String playlistId, Integer position, String joinedUris) {
        return builder
            .post()
            .uri(uriBuilder -> uriBuilder
                    .path("/playlists/{playlist_id}/tracks")
                    .queryParam("position", position)
                    .queryParam("uris", joinedUris)
                    .build(playlistId))
            .retrieve()
            .bodyToMono(Snapshot.class)
            .block();
    }

    public MappedPlaylist getPlaylist(String playlistId) {
        return builder
                .get()
                .uri("/playlists/{playlist_id}", playlistId)
                .retrieve()
                .bodyToMono(MappedPlaylist.class)
                .block();
    }
}

package com.example.spotifydemo.datasources;

import com.example.spotifydemo.models.FeaturedPlaylists;
import com.example.spotifydemo.models.MappedPlaylist;
import com.example.spotifydemo.models.Snapshot;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class SpotifyClient {
    private static final String SPOTIFY_API_URL = "https://spotify-demo-api-fe224840a08c.herokuapp.com/v1";
    private final WebClient builder = WebClient.builder().baseUrl(SPOTIFY_API_URL).build();

    public FeaturedPlaylists featuredPlaylistsRequest() {
        return builder
                .get()
                .uri("/browse/featured-playlists")
                .retrieve()
                .bodyToMono(FeaturedPlaylists.class)
                .block();
    }

    public MappedPlaylist playlistRequest(String playlistId) {
        return builder
                .get()
                .uri("/playlists/{playlist_id}", playlistId)
                .retrieve()
                .bodyToMono(MappedPlaylist.class)
                .block();
    }

    public Snapshot addItemsToPlaylist(String playlistId, Integer position, String uris) {
        return builder
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/playlists/{playlist_id}/tracks")
                        .queryParam("position", position)
                        .queryParam("uris", uris)
                        .build(playlistId))
                .retrieve()
                .bodyToMono(Snapshot.class)
                .block();
    }
}

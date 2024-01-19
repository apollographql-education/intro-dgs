package com.example.spotifydemo.datasources;

import com.example.spotifydemo.models.PlaylistList;
import com.example.spotifydemo.models.MappedPlaylist;
import com.example.spotifydemo.models.Snapshot;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class SpotifyClient {
    private static final String SPOTIFY_API_URL = "https://spotify-demo-api-fe224840a08c.herokuapp.com/v1";

    private final RestClient builder = RestClient.builder().baseUrl(SPOTIFY_API_URL).build();

    public PlaylistList featuredPlaylistsRequest() {
        return client
                .get()
                .uri("/browse/featured-playlists")
                .retrieve()
                .body(PlaylistList.class);
    }

    public MappedPlaylist playlistRequest(String playlistId) {
        return client
                .get()
                .uri("/playlists/{playlist_id}", playlistId)
                .retrieve()
                .body(MappedPlaylist.class);
    }

    public Snapshot addItemsToPlaylist(String playlistId, Integer position, String uris) {
        return client
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/playlists/{playlist_id}/tracks")
                        .queryParam("position", position)
                        .queryParam("uris", uris)
                        .build(playlistId))
                .retrieve()
                .body(Snapshot.class);
    }
}

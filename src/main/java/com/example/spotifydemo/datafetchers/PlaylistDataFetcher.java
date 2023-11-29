package com.example.spotifydemo.datafetchers;


import com.example.spotifydemo.FeaturedPlaylists;
import com.example.spotifydemo.MappedPlaylist;
import com.example.spotifydemo.Snapshot;
import com.example.spotifydemo.generated.types.AddItemsToPlaylistInput;
import com.example.spotifydemo.generated.types.AddItemsToPlaylistPayload;
import com.example.spotifydemo.generated.types.Playlist;
import com.netflix.graphql.dgs.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

@DgsComponent
public class PlaylistDataFetcher {

    String url = "http://localhost:3001/v1";
    WebClient builder = WebClient.builder().baseUrl(url).build();

    @DgsQuery
    public List<MappedPlaylist> featuredPlaylists() {

        FeaturedPlaylists featuredPlaylists = builder
                .get()
                .uri("/browse/featured-playlists")
                .retrieve()
                .bodyToMono(FeaturedPlaylists.class)
                .block();

        assert featuredPlaylists != null;
        return featuredPlaylists.getPlaylists();

    }

    @DgsMutation
    public AddItemsToPlaylistPayload addItemsToPlaylist(@InputArgument AddItemsToPlaylistInput input) {
        String playlistId = input.getPlaylistId();
        Integer position = input.getPosition();
        List<String> uris = input.getUris();

        String joinedUris = String.join(",", uris);

        AddItemsToPlaylistPayload payload = new AddItemsToPlaylistPayload();

        try {
            Snapshot snapshot = builder
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/playlists/{playlist_id}/tracks")
                            .queryParam("position", position)
                            .queryParam("uris", joinedUris)
                            .build(playlistId))
                    .retrieve()
                    .bodyToMono(Snapshot.class)
                    .block();

            assert snapshot != null;
            String snapshotId = snapshot.getId();
            assert Objects.equals(snapshotId, playlistId);

            Playlist playlist = new Playlist();
            playlist.setId(playlistId);


            payload.setCode(200);
            payload.setMessage("success");
            payload.setSuccess(true);
            payload.setPlaylist(playlist);

        } catch(Throwable error) {
            payload.setCode(500);
            payload.setMessage(error.getMessage());
            payload.setSuccess(false);
            payload.setPlaylist(null);

        }

        return payload;
    }


    @DgsData(parentType="AddItemsToPlaylistPayload", field="playlist")
    public MappedPlaylist updatedPlaylist(DgsDataFetchingEnvironment dfe) {
        AddItemsToPlaylistPayload payload = dfe.getSource();

        Playlist playlist = payload.getPlaylist();


        if (playlist != null) {
            String playlistId = playlist.getId();
            return requestPlaylist(playlistId);
        }


        return null;
    }


    @DgsQuery
    public MappedPlaylist playlist(@InputArgument String id) {
        return requestPlaylist(id);
    }

    public MappedPlaylist requestPlaylist(@InputArgument String playlistId) {
        return builder
                .get()
                .uri("/playlists/{playlist_id}", playlistId)
                .retrieve()
                .bodyToMono(MappedPlaylist.class)
                .block();
    }

}

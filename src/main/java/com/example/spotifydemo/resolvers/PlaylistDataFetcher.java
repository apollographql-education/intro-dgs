package com.example.spotifydemo.resolvers;

import com.example.spotifydemo.datasoruces.SpotifyClient;
import com.example.spotifydemo.generated.types.AddItemsToPlaylistInput;
import com.example.spotifydemo.generated.types.AddItemsToPlaylistPayload;
import com.example.spotifydemo.generated.types.Playlist;
import com.example.spotifydemo.models.FeaturedPlaylists;
import com.example.spotifydemo.models.MappedPlaylist;
import com.example.spotifydemo.models.Snapshot;
import com.netflix.graphql.dgs.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@DgsComponent
public class PlaylistDataFetcher {

    private final SpotifyClient spotifyClient;

    @Autowired
    public PlaylistDataFetcher(SpotifyClient spotifyClient) {
        this.spotifyClient = spotifyClient;
    }

    @DgsQuery
    public List<MappedPlaylist> featuredPlaylists() {

        FeaturedPlaylists featuredPlaylists = spotifyClient.featuredPlaylists();

        if (featuredPlaylists != null) {
            return featuredPlaylists.getPlaylists();
        }

        return new ArrayList<>();
    }

    @DgsMutation
    public AddItemsToPlaylistPayload addItemsToPlaylist(@InputArgument AddItemsToPlaylistInput input) {
        String playlistId = input.getPlaylistId();
        Integer position = input.getPosition();
        List<String> uris = input.getUris();

        AddItemsToPlaylistPayload payload = new AddItemsToPlaylistPayload();

        Snapshot snapshot = spotifyClient.addItemsToPlaylist(playlistId, position, String.join(",", uris));

        if (snapshot != null) {
            String snapshotId = snapshot.getId();
            if (snapshotId == playlistId) {
                Playlist playlist = new Playlist();
                playlist.setId(playlistId);

                payload.setCode(200);
                payload.setMessage("success");
                payload.setSuccess(true);
                payload.setPlaylist(playlist);

                return payload;
            }
        }

        payload.setCode(500);
        payload.setMessage("could not update playlist");
        payload.setSuccess(false);
        payload.setPlaylist(null);

        return payload;
    }

    @DgsData(parentType="AddItemsToPlaylistPayload", field="playlist")
    public MappedPlaylist updatedPlaylist(DgsDataFetchingEnvironment dfe) {
        AddItemsToPlaylistPayload payload = dfe.getSource();
        Playlist playlist = payload.getPlaylist();

        if (playlist != null) {
            String playlistId = playlist.getId();
            return spotifyClient.getPlaylist(playlistId);
        }

        return null;
    }

    @DgsQuery
    public MappedPlaylist playlist(@InputArgument String id) {
        return spotifyClient.getPlaylist(id);
    }
}

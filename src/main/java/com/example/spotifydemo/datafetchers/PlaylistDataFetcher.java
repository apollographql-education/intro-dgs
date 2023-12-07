package com.example.spotifydemo.datafetchers;
import com.example.spotifydemo.generated.types.AddItemsToPlaylistInput;
import com.example.spotifydemo.generated.types.AddItemsToPlaylistPayload;
import com.example.spotifydemo.models.FeaturedPlaylists;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.example.spotifydemo.models.MappedPlaylist;
import java.util.List;
import com.example.spotifydemo.datasources.SpotifyClient;
import org.springframework.beans.factory.annotation.Autowired;
import com.netflix.graphql.dgs.InputArgument;
import com.example.spotifydemo.models.Snapshot;
import com.example.spotifydemo.generated.types.Playlist;
import java.util.Objects;
@DgsComponent
public class PlaylistDataFetcher {
    private final SpotifyClient spotifyClient;

    @Autowired
    public PlaylistDataFetcher(SpotifyClient spotifyClient) {
        this.spotifyClient = spotifyClient;
    }
    @DgsQuery
    public List<MappedPlaylist> featuredPlaylists() {
        FeaturedPlaylists response = spotifyClient.featuredPlaylistsRequest();
        return response.getPlaylists();
    };

    @DgsQuery
    public MappedPlaylist playlist(@InputArgument String id) {
        return spotifyClient.playlistRequest(id);
    }

    @DgsMutation
    public AddItemsToPlaylistPayload addItemsToPlaylist(@InputArgument AddItemsToPlaylistInput input) {
        String playlistId = input.getPlaylistId();
        Integer position = input.getPosition();
        List<String> uris = input.getUris();

        Snapshot snapshot = spotifyClient.addItemsToPlaylist(playlistId, position, String.join(",", uris));

        AddItemsToPlaylistPayload payload = new AddItemsToPlaylistPayload();

        if (snapshot != null) {
            String snapshotId = snapshot.getId();
            if (Objects.equals(snapshotId, playlistId)) {
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
    public MappedPlaylist getPayloadPlaylist(DgsDataFetchingEnvironment dfe) {
        AddItemsToPlaylistPayload payload = dfe.getSource();
        Playlist playlist = payload.getPlaylist();

        if (playlist != null) {
            String playlistId = playlist.getId();
            return spotifyClient.playlistRequest(playlistId);
        }

        return null;
    }

}

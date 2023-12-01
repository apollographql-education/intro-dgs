package com.example.spotifydemo;

import com.example.spotifydemo.datasources.SpotifyClient;
import com.example.spotifydemo.datafetchers.PlaylistDataFetcher;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {DgsAutoConfiguration.class, PlaylistDataFetcher.class, SpotifyClient.class})
class SpotifyDemoApplicationTests {

	@Autowired
	DgsQueryExecutor dgsQueryExecutor;

	@Test
	void featuredPlaylists() {
		List<String> ids = dgsQueryExecutor.executeAndExtractJsonPath(
				"{ featuredPlaylists { id } }",
				"data.featuredPlaylists[*].id");

		assertFalse(ids.isEmpty());
	}
}

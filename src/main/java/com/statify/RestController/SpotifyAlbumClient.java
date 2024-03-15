package com.statify.RestController;

import com.statify.model.*;
import net.minidev.json.JSONArray;
import org.springframework.http.*;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SpotifyAlbumClient {

    @GetMapping("/t")
    public List<Track> getTopTracks(OAuth2Authentication details) {
        String jwt = ((OAuth2AuthenticationDetails) details.getDetails()).getTokenValue();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<TrackResponse> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me/top/tracks",
                HttpMethod.GET,
                httpEntity,
                TrackResponse.class
        );

        List<Track> songs = new ArrayList<>();

        if (response.getBody() != null && response.getBody().getItems() != null) {
            for (Track track : response.getBody().getItems()) {
                songs.add(track);
            }
        }
        return songs;
    }




    @GetMapping("/album/{authorName}")
    public SpotifyAlbum getAlbumsByAuthor(OAuth2Authentication details, @PathVariable String authorName) {
        String jwt = ((OAuth2AuthenticationDetails)details.getDetails()).getTokenValue();


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<SpotifyAlbum> exchange = restTemplate.exchange("https://api.spotify.com/v1/search?q="+ authorName + "&type=track&market=US&limit=10&offset=5",
                HttpMethod.GET,
                httpEntity,
                SpotifyAlbum.class);
        return exchange.getBody();
    }

    @GetMapping("/a")
    public List<String> getTopArtistsNames(OAuth2Authentication details) {
        String jwt = ((OAuth2AuthenticationDetails) details.getDetails()).getTokenValue();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<ArtistResponse> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me/top/artists",
                HttpMethod.GET,
                httpEntity,
                ArtistResponse.class
        );

        List<String> artistNames = new ArrayList<>();
        if (response.getBody() != null && response.getBody().getItems() != null) {
            for (Artist artist : response.getBody().getItems()) {
                artistNames.add(artist.getName());
                artistNames.add(artist.getImages().toString());
            }
        }
        return artistNames;
    }

    //to delete
    @GetMapping("/tracks")
    public SpotifyAlbum getTopTrackss(OAuth2Authentication details) {
        String jwt = ((OAuth2AuthenticationDetails)details.getDetails()).getTokenValue();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<SpotifyAlbum> exchange = restTemplate.exchange("https://api.spotify.com/v1/me/top/tracks",
                HttpMethod.GET,
                httpEntity,
                SpotifyAlbum.class);
        return exchange.getBody();
    }

}

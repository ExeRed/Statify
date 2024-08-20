package com.statify.service;

import com.statify.model.ArtistResponse;
import com.statify.model.Playlist;
import com.statify.model.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlaylistService {

    public String createPlaylist(String userId, String accessToken) {


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "My Top Tracks Playlist");
        requestBody.put("description", "A playlist of my top tracks.");
        requestBody.put("public", true);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, httpHeaders);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.spotify.com/v1/users/" + userId + "/playlists",
                HttpMethod.POST,
                entity,
                Map.class
        );



        return (String) response.getBody().get("id");
    }

    public void addTracksToPlaylist(String playlistId, List<String> trackUris, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("uris", trackUris);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        restTemplate.exchange(
                "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks",
                HttpMethod.POST,
                entity,
                Void.class);
    }

    public Playlist getPlaylist(String playlistId, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Playlist> response = restTemplate.exchange(
                "https://api.spotify.com/v1/playlists/" + playlistId,
                HttpMethod.GET,
                entity,
                Playlist.class
        );

        return response.getBody();
    }
}

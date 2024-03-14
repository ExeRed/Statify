package com.statify.controller;

import com.statify.model.Track;
import com.statify.model.TrackResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SpotifyTrackController {

    @Autowired
    private SpotifyTrackController spotifyTrackController;

    @GetMapping("/topTrack")
    public String topTracks(OAuth2Authentication details, Model model) {
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

        model.addAttribute("tracks", songs);
        return "topTracks";
    }



}
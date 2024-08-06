package com.statify.service;

import com.statify.model.ArtistResponse;
import com.statify.model.TrackResponse;
import com.statify.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

@Service
public class UserService {
    private static final String SPOTIFY_USER_INFO_URL = "https://api.spotify.com/v1/me";

    public User getCurrentUser(String jwt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<User> response = restTemplate.exchange(
                SPOTIFY_USER_INFO_URL,
                HttpMethod.GET,
                httpEntity,
                User.class
        );

        return response.getBody();
    }



    public TrackResponse getTopTracks(String accessToken, String timePeriod) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<TrackResponse> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me/top/tracks?time_range=" + timePeriod + "&limit=50",
                HttpMethod.GET,
                entity,
                TrackResponse.class
        );

        return response.getBody();
    }

    public ArtistResponse getTopArtists(String jwt, String timePeriod) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<ArtistResponse> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me/top/artists?time_range=" + timePeriod + "&limit=50",
                HttpMethod.GET,
                httpEntity,
                ArtistResponse.class
        );

        return response.getBody();
    }


}

package com.statify.service;

import com.statify.model.Artist;
import com.statify.model.Track;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ArtistService {

    public Artist getArtist(String accessToken, String id) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Artist> response = restTemplate.exchange(
                "https://api.spotify.com/v1/artists/" + id,
                HttpMethod.GET,
                entity,
                Artist.class
        );

        return response.getBody();
    }

}

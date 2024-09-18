package com.statify.service;

import com.statify.model.Track;
import com.statify.model.TrackResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;


@Service
public class TrackService {

    private static final Logger logger = LoggerFactory.getLogger(TrackService.class);

    public Track getTrack(String accessToken, String id) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Track> response = restTemplate.exchange(
                    "https://api.spotify.com/v1/tracks/" + id,
                    HttpMethod.GET,
                    entity,
                    Track.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error occurred: Status Code: {}, Response Body: {}",
                    e.getStatusCode().value(), e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Unexpected error occurred: {}", e.getMessage());
        }

        return null;
    }

    public List<Track> getTracks(String accessToken, List<String> ids) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Составляем строку из ID треков для параметра ids
        String idsString = String.join(",", ids);

        try {
            ResponseEntity<TrackResponse> response = restTemplate.exchange(
                    "https://api.spotify.com/v1/tracks?ids=" + idsString,
                    HttpMethod.GET,
                    entity,
                    TrackResponse.class
            );

            // Получаем список треков из ответа
            List<Track> tracks = response.getBody().getTracks();

            return tracks;
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error occurred: Status Code: {}, Response Body: {}",
                    e.getStatusCode().value(), e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Unexpected error occurred: {}", e.getMessage());
        }

        return null;
    }


}

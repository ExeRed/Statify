package com.statify.service;

import com.statify.model.Track;
import com.statify.model.TrackResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;




@Service
public class TrackService {

    private static final Logger logger = LoggerFactory.getLogger(TrackService.class);

    public Track getTrack(String accessToken, String id) {
        logger.info("Fetching track with ID: {}", id);
        if (id == null || id.length() != 22) { // Example validation length, adjust as needed
            logger.error("Invalid track ID: {}", id);
            return null;
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Track> response = null;
        try {
            response = restTemplate.exchange(
                    "https://api.spotify.com/v1/tracks/" + id,
                    HttpMethod.GET,
                    entity,
                    Track.class
            );
        } catch (HttpClientErrorException e) {
            String errorMessage = String.format("HTTP Status Code: %d, Status Text: %s, Response Body: %s",
                    e.getStatusCode().value(),
                    e.getStatusText(),
                    e.getResponseBodyAsString());

            logger.error("Request failed with error: {}", errorMessage);

            // Handle specific status codes
            switch (e.getStatusCode()) {
                case BAD_REQUEST:
                    logger.error("Bad Request: Check the request parameters or payload.");
                    break;
                case UNAUTHORIZED:
                    logger.error("Unauthorized: Check the authentication details.");
                    break;
                case FORBIDDEN:
                    logger.error("Forbidden: You do not have permission to access this resource.");
                    break;
                case NOT_FOUND:
                    logger.error("Not Found: The requested resource could not be found.");
                    break;
                default:
                    logger.error("Unexpected error: {}", e.getMessage());
                    break;
            }
            return null;
        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage());
            return null;
        }

        return response != null ? response.getBody() : null;
    }

}

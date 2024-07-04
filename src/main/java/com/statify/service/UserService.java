package com.statify.service;

import com.statify.model.User;
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
}

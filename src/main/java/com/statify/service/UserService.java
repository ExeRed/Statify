package com.statify.service;

import com.statify.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public List<String> getTopGenres(String jwt, String timePeriod) {
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

        List<String> allGenres = new ArrayList<>(); // all genres, not sorted, can repeat

        for (Artist artist : response.getBody().getItems()) {
            if (artist.getGenres() != null) {
                for (Genres genreList : artist.getGenres()) {   // Loop through each genre in the artist
                    // Get the list of genres from the Genres object
                    List<String> artistGenres = genreList.getGenres();
                    // Add each genre from the artist to the allGenres list
                    allGenres.addAll(artistGenres);
                }
            }
        }

        Map<String, Integer> genreCounts = new HashMap<>(); // to store genre counts

        //count occurrences of each genre
        for (String genre : allGenres) {
            if (genreCounts.containsKey(genre)) {
                genreCounts.put(genre, genreCounts.get(genre) + 1);
            } else {
                genreCounts.put(genre, 1);
            }
        }

        List<Map.Entry<String, Integer>> sortedGenres = new ArrayList<>(genreCounts.entrySet());
        sortedGenres.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<String> topGenresList = new ArrayList<>();

        for (int i = 0; i < sortedGenres.size(); i++) {
            topGenresList.add(sortedGenres.get(i).getKey());
        }

        return  topGenresList;
    }

}

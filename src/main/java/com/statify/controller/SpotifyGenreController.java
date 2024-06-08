package com.statify.controller;

import com.statify.model.Artist;
import com.statify.model.ArtistResponse;
import com.statify.model.Genres;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SpotifyGenreController {

    @GetMapping("/topGenres")
    public String topTracks(@RequestParam(value = "timePeriod", defaultValue = "short_term") String timePeriod,
                            OAuth2Authentication details, Model model) {
        String jwt = ((OAuth2AuthenticationDetails) details.getDetails()).getTokenValue();

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

        if (timePeriod.equals("long_term")) {
            model.addAttribute("time", "of all time");
        } else if (timePeriod.equals("medium_term")) {
            model.addAttribute("time", "from last 6 months");
        } else {
            model.addAttribute("time", "from last 4 weeks");
        }

        model.addAttribute("selectedOption", timePeriod);
        model.addAttribute("topGenresList", topGenresList);

        return "topGenres";

    }
}

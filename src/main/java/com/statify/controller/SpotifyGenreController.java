package com.statify.controller;

import com.statify.model.Artist;
import com.statify.model.ArtistResponse;
import com.statify.model.Genres;
import com.statify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    private UserService userService;

    @GetMapping("/topGenre")
    public String topTracks(@RequestParam(value = "timePeriod", defaultValue = "short_term") String timePeriod,
                            OAuth2AuthenticationToken authentication, Model model) {

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());

        String jwt = client.getAccessToken().getTokenValue();

        List<String> topGenresList = userService.getTopGenres(jwt, timePeriod);

        if (timePeriod.equals("long_term")) {
            model.addAttribute("time", "of all time");
        } else if (timePeriod.equals("medium_term")) {
            model.addAttribute("time", "from last 6 months");
        } else {
            model.addAttribute("time", "from last 4 weeks");
        }

        model.addAttribute("selectedOption", timePeriod);
        model.addAttribute("topGenresList", topGenresList);
        model.addAttribute("loggedIn", true);

        return "topGenres";

    }
}

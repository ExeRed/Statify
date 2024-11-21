package com.statify.controller;

import com.statify.model.Artist;
import com.statify.model.ArtistResponse;
import com.statify.model.Genres;
import com.statify.model.User;
import com.statify.service.SpotifyTokenService;
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
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    private SpotifyTokenService spotifyTokenService;
    @GetMapping({"/topGenre", "/{userId:[a-zA-Z0-9]+}/topGenre"})
    public String topGenres(@PathVariable(required = false) String userId,
                            @RequestParam(value = "timePeriod", defaultValue = "short_term") String timePeriod,
                            OAuth2AuthenticationToken authentication, Model model) {

        String accessToken;
        User currentUser;
        boolean isOwnProfile;

        // Check if viewing the own profile or another user's profile
        if (userId == null) {
            // Own profile: Get the access token of the authenticated user
            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                    authentication.getAuthorizedClientRegistrationId(),
                    authentication.getName());
            accessToken = client.getAccessToken().getTokenValue();
            currentUser = userService.getCurrentUser(accessToken);
            isOwnProfile = true;
        } else {
            // Another user's profile: Refresh access token using userId
            accessToken = spotifyTokenService.refreshAccessToken(userId);
            currentUser = userService.getCurrentUser(accessToken);
            isOwnProfile = false;
        }

        // Fetch top genres for the given time period
        List<String> topGenresList = userService.getTopGenres(accessToken, timePeriod);

        // Set the time period label for display
        if (timePeriod.equals("long_term")) {
            model.addAttribute("time", "of all time");
        } else if (timePeriod.equals("medium_term")) {
            model.addAttribute("time", "from last 6 months");
        } else {
            model.addAttribute("time", "from last 4 weeks");
        }

        // Add attributes to the model
        model.addAttribute("selectedOption", timePeriod);
        model.addAttribute("topGenresList", topGenresList);
        model.addAttribute("loggedIn", true);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isOwnProfile", isOwnProfile);

        return "topGenres";
    }


}

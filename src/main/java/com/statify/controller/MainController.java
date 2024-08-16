package com.statify.controller;

import com.statify.model.*;
import com.statify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/")
    public String home(@RequestParam(value = "timePeriod", defaultValue = "short_term") String timePeriod,
                       OAuth2AuthenticationToken authentication, Model model) {

        if (authentication != null) {

            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                    authentication.getAuthorizedClientRegistrationId(),
                    authentication.getName());

            String jwt = client.getAccessToken().getTokenValue();

            // Получение имени пользователя
            User currentUser = userService.getCurrentUser(jwt);

            List<Track> songs = new ArrayList<>();
            List<Artist> artists = new ArrayList<>();
            List<String> topGenresList = userService.getTopGenres(jwt, timePeriod);

            TrackResponse trackResponse = userService.getTopTracks(jwt, timePeriod);
            ArtistResponse artistResponse = userService.getTopArtists(jwt, timePeriod);
            PlayedTrackResponse recentlyPlayedResponse = userService.getRecentlyPlayed(jwt);
            List<PlayedTrack> recentlyPlayed = recentlyPlayedResponse.getItems();
            TrackResponse currentlyPlayingTrack = userService.getCurrentlyPlayingTrack(jwt);

            if (trackResponse != null && trackResponse.getItems() != null) {
                songs.addAll(trackResponse.getItems());
            }

            if (artistResponse != null && artistResponse.getItems() != null) {
                artists.addAll(artistResponse.getItems());
            }

            if (recentlyPlayedResponse.getItems() != null) {
                recentlyPlayed.addAll(recentlyPlayedResponse.getItems());
            }

            if (currentlyPlayingTrack != null && currentlyPlayingTrack.getItem() != null) {
                model.addAttribute("currentlyPlaying", currentlyPlayingTrack.getItem());
            }

            model.addAttribute("recentlyPlayed", recentlyPlayed);
            model.addAttribute("tracks", songs);
            model.addAttribute("artists", artists);
            model.addAttribute("loggedIn", true);
            model.addAttribute("topGenres", topGenresList);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("selectedOption", timePeriod);
        }

        return "home"; // Имя шаблона для главной страницы
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Имя шаблона для страницы входа
    }


}

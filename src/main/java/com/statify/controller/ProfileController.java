package com.statify.controller;

import com.statify.model.*;
import com.statify.service.SpotifyTokenService;
import com.statify.service.TrackService;
import com.statify.service.UserService;
import com.statify.serviceDB.SpotifyUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private TrackService trackService;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    private UserService userService;
    @Autowired
    private SpotifyTokenService spotifyTokenService;

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @GetMapping("favicon.ico")
    @ResponseBody
    public void returnNoFavicon() {
        // Возвращает пустой ответ для favicon.ico
    }


    /*  @GetMapping("/{userId:[a-zA-Z0-9]+}")
    public String profile(@PathVariable String userId,
                          @RequestParam(value = "timePeriod", defaultValue = "short_term") String timePeriod,
                          OAuth2AuthenticationToken authentication, Model model) {
        List<UserTopTrackDB> topTracks;
        try {
            topTracks = userTopTrackService.getTopTracksForUser(userId, timePeriod);
        } catch (RuntimeException e) {
            // Логируем ошибку и возвращаем сообщение об ошибке или пустую страницу
            logger.error("Error fetching top tracks for user {}: {}", userId, e.getMessage());
            model.addAttribute("error", "User not found or has no top tracks.");
            return "error"; // Название представления для отображения ошибки
        }

        List<String> ids = topTracks.stream()
                .map(UserTopTrackDB::getTrackId)
                .collect(Collectors.toList());

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());

        String jwt = client.getAccessToken().getTokenValue();

        List<Track> songs = trackService.getTracks(jwt, ids);


        model.addAttribute("tracks", songs);
        model.addAttribute("timePeriod", timePeriod);
        model.addAttribute("loggedIn", true);

        return "home";
    }


    @GetMapping("/{userId:[a-zA-Z0-9]+}")
    public String profile(@PathVariable String userId,
                          @RequestParam(value = "timePeriod", defaultValue = "short_term") String timePeriod,
                          Model model) {

        // Обновляем access token для пользователя
        String accessToken = spotifyTokenService.refreshAccessToken(userId);
        User currentUser = userService.getCurrentUser(accessToken);

        List<Track> songs = new ArrayList<>();
        List<Artist> artists = new ArrayList<>();
        List<String> topGenresList = userService.getTopGenres(accessToken, timePeriod);

        TrackResponse trackResponse = userService.getTopTracks(accessToken, timePeriod);
        ArtistResponse artistResponse = userService.getTopArtists(accessToken, timePeriod);
        PlayedTrackResponse recentlyPlayedResponse = userService.getRecentlyPlayed(accessToken);
        List<PlayedTrack> recentlyPlayed = recentlyPlayedResponse.getItems();
        TrackResponse currentlyPlayingTrack = userService.getCurrentlyPlayingTrack(accessToken);

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

        model.addAttribute("tracks", songs);
        model.addAttribute("timePeriod", timePeriod);
        model.addAttribute("loggedIn", true);
        model.addAttribute("isOwnProfile", false);
        return "home";
    } */




}

package com.statify.controller;

import com.statify.imageGenerator.AllStatsImageGenerator;
import com.statify.imageGenerator.TopTracksImageGenerator;
import com.statify.model.*;
import com.statify.service.PrivacySettingsService;
import com.statify.service.SpotifyTokenService;
import com.statify.service.TrackService;
import com.statify.serviceDB.SpotifyUserService;
import com.statify.service.UserService;
import com.statify.serviceDB.SubscriptionService;
import com.statify.table.PrivacySettingsDB;
import com.statify.table.SpotifyUserDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private SpotifyUserService spotifyUserService;

    @Autowired
    private SpotifyTokenService spotifyTokenService;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private PrivacySettingsService privacySettingsService;

    private static final Logger logger = LoggerFactory.getLogger(TrackService.class);
    @GetMapping({"/", "/{userId:[a-zA-Z0-9]+}"})
    public String profileOrHome(@PathVariable(required = false) String userId,
                                @RequestParam(value = "timePeriod", defaultValue = "short_term") String timePeriod,
                                OAuth2AuthenticationToken authentication, Model model) {

        String accessToken;
        User currentUser;
        boolean isOwnProfile = false;
        SpotifyUserDB user;

        if (userId == null) {
            // If userId is null, we're dealing with the authenticated user's profile
            if (authentication != null) {
                OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());
                accessToken = client.getAccessToken().getTokenValue();
                currentUser = userService.getCurrentUser(accessToken);

                String refreshToken = client.getRefreshToken() != null ? client.getRefreshToken().getTokenValue() : null;
                Instant expiresAtInstant = client.getAccessToken().getExpiresAt();
                LocalDateTime expiresAt = LocalDateTime.ofInstant(expiresAtInstant, ZoneOffset.UTC);

                SpotifyUserDB userEntity = spotifyUserService.findOrCreateUser(currentUser);
                spotifyUserService.updateUser(userEntity);
                spotifyTokenService.saveTokens(currentUser.getId(), accessToken, refreshToken, expiresAt);

                user = spotifyUserService.getUser(currentUser.getId());

                isOwnProfile = true;
            } else {
                model.addAttribute("loggedIn", false);
                return "home"; // Redirect to login if unauthenticated and accessing "/"
            }
        } else {
            // If userId is provided, we fetch the profile for another user
            accessToken = spotifyTokenService.refreshAccessToken(userId);
            currentUser = userService.getCurrentUser(accessToken);

            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                    authentication.getAuthorizedClientRegistrationId(),
                    authentication.getName());
            String accessToken2 = client.getAccessToken().getTokenValue();

            User user2 = userService.getCurrentUser(accessToken2);
            SpotifyUserDB currentUserReal = spotifyUserService.getUser(user2.getId());
            user = spotifyUserService.getUser(currentUser.getId());
            if (currentUserReal != user) {
                model.addAttribute("isFollowing", subscriptionService.isAlreadyFollowing(currentUserReal, user));
            }

        }

        // Fetch data for the profile
        PrivacySettingsDB privacySettingsDB = privacySettingsService.getPrivacySettingsByUser(user);
        populateModelWithProfileData(accessToken, timePeriod, model, currentUser, isOwnProfile, user, privacySettingsDB);

        return "home"; // Return the same view for both "/" and "/{userId}"
    }

    // Helper method to populate model with user data
    private void populateModelWithProfileData(String accessToken, String timePeriod, Model model,
                                              User currentUser, boolean isOwnProfile, SpotifyUserDB user, PrivacySettingsDB privacySettingsDB) {
        List<Track> songs = new ArrayList<>();
        List<Artist> artists = new ArrayList<>();
        List<String> topGenresList = userService.getTopGenres(accessToken, timePeriod);
        List<PlayedTrack> recentlyPlayed = new ArrayList<>();

        // Populate lists if responses are not null and settings are true
        if (privacySettingsDB.isShowTopTracks() || isOwnProfile) {
            TrackResponse trackResponse = userService.getTopTracks(accessToken, timePeriod);

            if (trackResponse != null && trackResponse.getItems() != null) {
                songs.addAll(trackResponse.getItems());
            }
        }

        if (privacySettingsDB.isShowTopArtists() || isOwnProfile) {
            ArtistResponse artistResponse = userService.getTopArtists(accessToken, timePeriod);

            if (artistResponse != null && artistResponse.getItems() != null) {
                artists.addAll(artistResponse.getItems());
            }
        }

        if (privacySettingsDB.isShowRecentlyPlayed() || isOwnProfile) {
            PlayedTrackResponse recentlyPlayedResponse = userService.getRecentlyPlayed(accessToken);

            if (recentlyPlayedResponse.getItems() != null) {
                recentlyPlayed.addAll(recentlyPlayedResponse.getItems());
            }
        }

        TrackResponse currentlyPlayingTrack = userService.getCurrentlyPlayingTrack(accessToken);


        // Add currently playing track to the model if available
        if (currentlyPlayingTrack != null && currentlyPlayingTrack.getItem() != null) {
            model.addAttribute("currentlyPlaying", currentlyPlayingTrack.getItem());
        }


        // Списки подписчиков и подписок
        if (privacySettingsDB.isShowFollowers() || isOwnProfile) {
            model.addAttribute("followers", subscriptionService.getFollowers(user));
        }
        if (privacySettingsDB.isShowFollowing() || isOwnProfile) {
            model.addAttribute("following", subscriptionService.getFollowing(user));
        }

        if (isOwnProfile) {
            AllStatsImageGenerator imageGenerator = new AllStatsImageGenerator();
            String base64EncodedImage;
            if (timePeriod.equals("long_term")) {
                base64EncodedImage = imageGenerator.generateBase64Image(user.getUsername(), artists, songs, topGenresList, "year", "png");
            } else if (timePeriod.equals("medium_term")) {
                base64EncodedImage = imageGenerator.generateBase64Image(user.getUsername(), artists, songs, topGenresList, "6 months", "png");
            } else {
                base64EncodedImage = imageGenerator.generateBase64Image(user.getUsername(), artists, songs, topGenresList, "month", "png");
            }
            model.addAttribute("base64EncodedImage", base64EncodedImage);

        }

        // Populate the model with all data
        model.addAttribute("customId", user.getCustomId());
        model.addAttribute("recentlyPlayed", recentlyPlayed);
        model.addAttribute("tracks", songs);
        model.addAttribute("artists", artists);
        model.addAttribute("topGenres", topGenresList);
        model.addAttribute("loggedIn", true);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("selectedOption", timePeriod);
        model.addAttribute("isOwnProfile", isOwnProfile);
    }



    @GetMapping("/login")
    public String login() {
        return "login"; // Имя шаблона для страницы входа
    }


    /* @GetMapping("/")
    public String home(@RequestParam(value = "timePeriod", defaultValue = "short_term") String timePeriod,
                       OAuth2AuthenticationToken authentication, Model model) {

        if (authentication != null) {

            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                    authentication.getAuthorizedClientRegistrationId(),
                    authentication.getName());

            String jwt = client.getAccessToken().getTokenValue();
            String refreshToken = client.getRefreshToken() != null ? client.getRefreshToken().getTokenValue() : null;
            Instant expiresAtInstant = client.getAccessToken().getExpiresAt();
            LocalDateTime expiresAt = LocalDateTime.ofInstant(expiresAtInstant, ZoneOffset.UTC);  // если UTC используется для токенов

            // Получение имени пользователя
            User currentUser = userService.getCurrentUser(jwt);


            logger.info("Saving tokens for user {}: accessToken={}, refreshToken={}, expiresAt={}",
                    currentUser.getId(), jwt, refreshToken, expiresAt);

            // save or create user
            SpotifyUserDB userEntity = spotifyUserService.findOrCreateUser(currentUser);
            spotifyUserService.updateUser(userEntity);

            spotifyTokenService.saveTokens(currentUser.getId(), jwt, refreshToken, expiresAt);


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
            model.addAttribute("isOwnProfile", true);
        }

        return "home"; // Имя шаблона для главной страницы
    } */


}

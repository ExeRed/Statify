package com.statify.controller;

import com.statify.imageGenerator.TopArtistsImageGenerator;
import com.statify.model.Artist;
import com.statify.model.ArtistResponse;
import com.statify.model.Track;
import com.statify.model.User;
import com.statify.service.ArtistService;
import com.statify.service.PrivacySettingsService;
import com.statify.service.SpotifyTokenService;
import com.statify.service.UserService;
import com.statify.serviceDB.SpotifyUserService;
import com.statify.table.PrivacySettingsDB;
import com.statify.table.SpotifyUserDB;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
public class SpotifyArtistController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    @Autowired
    private SpotifyTokenService spotifyTokenService;

    @Autowired
    private PrivacySettingsService privacySettingsService;

    @Autowired
    private SpotifyUserService spotifyUserService;

    @GetMapping({"/topArtist", "/{userId:[a-zA-Z0-9]+}/topArtist"})
    public String topArtists(@PathVariable(required = false) String userId,
                             @RequestParam(value = "timePeriod", defaultValue = "short_term") String timePeriod,
                             OAuth2AuthenticationToken authentication, Model model) {

        String accessToken;
        User currentUser;
        boolean isOwnProfile;
        SpotifyUserDB user;

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

        user = spotifyUserService.getUser(currentUser.getId());
        PrivacySettingsDB privacySettingsDB = privacySettingsService.getPrivacySettingsByUser(user);

        String userName = currentUser.getDisplay_name();
        List<Artist> artists = new ArrayList<>();

        // Fetch top artists for the given time period
        if (privacySettingsDB.isShowTopArtists() || isOwnProfile) {
            ArtistResponse artistResponse = userService.getTopArtists(accessToken, timePeriod);

            if (artistResponse != null && artistResponse.getItems() != null) {
                artists.addAll(artistResponse.getItems());
            }
        }

        // Generate image for the top artists
        TopArtistsImageGenerator imageGenerator = new TopArtistsImageGenerator();
        String base64EncodedImage;

        if (timePeriod.equals("long_term")) {
            model.addAttribute("time", "of all time");
            base64EncodedImage = imageGenerator.generateBase64Image(userName, artists, "all time", "png");
        } else if (timePeriod.equals("medium_term")) {
            model.addAttribute("time", "from last 6 months");
            base64EncodedImage = imageGenerator.generateBase64Image(userName, artists, "last 6 months", "png");
        } else {
            model.addAttribute("time", "from last 4 weeks");
            base64EncodedImage = imageGenerator.generateBase64Image(userName, artists, "last 4 weeks", "png");
        }

        // Update model with attributes
        model.addAttribute("base64EncodedImage", base64EncodedImage);
        model.addAttribute("selectedOption", timePeriod);
        model.addAttribute("artists", artists);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isOwnProfile", isOwnProfile);
        model.addAttribute("loggedIn", true);

        return "topArtists";
    }



    @GetMapping("/artists/{id}")
    public String getArtist(@PathVariable("id") String id,
                           OAuth2AuthenticationToken authentication, Model model) {

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());

        String jwt = client.getAccessToken().getTokenValue();

        Artist artist = artistService.getArtist(jwt, id);

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        String formattedFollowers = numberFormat.format(artist.getFollowers().getTotal());

        artist.setFormattedFollowers(formattedFollowers);
        model.addAttribute("artist", artist);
        model.addAttribute("loggedIn", true);

        return "artist";
    }
}

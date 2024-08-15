package com.statify.controller;

import com.statify.imageGenerator.TopTracksImageGenerator;
import com.statify.model.Track;
import com.statify.model.TrackResponse;
import com.statify.model.User;
import com.statify.service.TrackService;
import com.statify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SpotifyTrackController {

    @Autowired
    private UserService userService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/topTrack")
    public String topTracks(@RequestParam(value = "timePeriod", defaultValue = "short_term") String timePeriod,
                            OAuth2AuthenticationToken authentication, Model model) {

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());

        String jwt = client.getAccessToken().getTokenValue();

        // Получение имени пользователя
        User currentUser = userService.getCurrentUser(jwt);
        String userName = currentUser.getDisplay_name();

        List<Track> songs = new ArrayList<>();

        TrackResponse trackResponse = userService.getTopTracks(jwt, timePeriod);

        if (trackResponse != null && trackResponse.getItems() != null) {
            songs.addAll(trackResponse.getItems());
        }

        // Generate image
        TopTracksImageGenerator imageGenerator = new TopTracksImageGenerator();
        String base64EncodedImage;

        if (timePeriod.equals("long_term")) {
            model.addAttribute("time", "of all time");
            base64EncodedImage = imageGenerator.generateBase64Image(userName, songs, "all time", "png");
        } else if (timePeriod.equals("medium_term")) {
            model.addAttribute("time", "from last 6 months");
            base64EncodedImage = imageGenerator.generateBase64Image(userName, songs, "last 6 months", "png");
        } else {
            model.addAttribute("time", "from last 4 weeks");
            base64EncodedImage = imageGenerator.generateBase64Image(userName, songs, "last 4 weeks", "png");
        }

        // Update model with Base64 encoded image
        model.addAttribute("base64EncodedImage", base64EncodedImage);
        model.addAttribute("selectedOption", timePeriod);
        model.addAttribute("tracks", songs);
        model.addAttribute("loggedIn", true);
        return "topTracks";
    }

    @GetMapping("/tracks/{id}")
    public String getTrack(@PathVariable("id") String id,
                            OAuth2AuthenticationToken authentication, Model model) {

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());

        String jwt = client.getAccessToken().getTokenValue();

        Track track = trackService.getTrack(jwt, id);

        model.addAttribute("track", track);
        model.addAttribute("loggedIn", true);

        return "track";
    }


}
package com.statify.controller;

import com.statify.SpotifyStatsImageGenerator;
import com.statify.model.Track;
import com.statify.model.TrackResponse;
import com.statify.model.User;
import com.statify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class SpotifyTrackController {

    @Autowired
    private UserService userService;

    @GetMapping("/topTrack")
    public String topTracks(@RequestParam(value = "timePeriod", defaultValue = "short_term") String timePeriod,
                            OAuth2Authentication details, Model model, HttpSession session) {
        String jwt = ((OAuth2AuthenticationDetails) details.getDetails()).getTokenValue();

        Long userId = (Long) session.getAttribute("userId");

        model.addAttribute("userId", userId);



        // Получение имени пользователя
        User currentUser = userService.getCurrentUser(jwt);
        String userName = currentUser.getDisplay_name();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<TrackResponse> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me/top/tracks?time_range=" + timePeriod + "&limit=50",
                HttpMethod.GET,
                httpEntity,
                TrackResponse.class
        );


        List<Track> songs = new ArrayList<>();

        if (response.getBody() != null && response.getBody().getItems() != null) {
            songs.addAll(response.getBody().getItems());
        }

        // Generate image
        SpotifyStatsImageGenerator imageGenerator = new SpotifyStatsImageGenerator();
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
        return "topTracks";
    }




}
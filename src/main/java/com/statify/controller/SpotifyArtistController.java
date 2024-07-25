package com.statify.controller;

import com.statify.imageGenerator.TopArtistsImageGenerator;
import com.statify.model.Artist;
import com.statify.model.ArtistResponse;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class SpotifyArtistController {

    @Autowired
    private UserService userService;

    @GetMapping("/topArtist")
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

        List<Artist> artists = new ArrayList<>();

        // Получение имени пользователя
        User currentUser = userService.getCurrentUser(jwt);
        String userName = currentUser.getDisplay_name();

        if (response.getBody() != null && response.getBody().getItems() != null) {
            artists.addAll(response.getBody().getItems());
        }

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

        model.addAttribute("base64EncodedImage", base64EncodedImage);
        model.addAttribute("selectedOption", timePeriod);
        model.addAttribute("artists", artists);

        return "topArtists";

    }
}

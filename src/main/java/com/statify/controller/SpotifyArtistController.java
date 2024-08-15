package com.statify.controller;

import com.statify.imageGenerator.TopArtistsImageGenerator;
import com.statify.model.Artist;
import com.statify.model.ArtistResponse;
import com.statify.model.Track;
import com.statify.model.User;
import com.statify.service.ArtistService;
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

    @GetMapping("/topArtist")
    public String topTracks(@RequestParam(value = "timePeriod", defaultValue = "short_term") String timePeriod,
                            OAuth2AuthenticationToken authentication, Model model) {

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());

        String jwt = client.getAccessToken().getTokenValue();


        List<Artist> artists = new ArrayList<>();

        ArtistResponse artistResponse = userService.getTopArtists(jwt, timePeriod);

        // Получение имени пользователя
        User currentUser = userService.getCurrentUser(jwt);
        String userName = currentUser.getDisplay_name();

        if (artistResponse != null && artistResponse.getItems() != null) {
            artists.addAll(artistResponse.getItems());
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

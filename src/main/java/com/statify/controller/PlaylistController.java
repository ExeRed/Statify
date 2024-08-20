package com.statify.controller;

import com.statify.model.Playlist;
import com.statify.model.User;
import com.statify.service.PlaylistService;
import com.statify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PlaylistController {

    @Autowired
    UserService userService;

    @Autowired
    PlaylistService playlistService;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @PostMapping("/createPlaylist")
    public String createPlaylist(
            @RequestParam("trackUris") List<String> trackUris,
            OAuth2AuthenticationToken authentication,
            Model model) {

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());

        String jwt = client.getAccessToken().getTokenValue();

        User user = userService.getCurrentUser(jwt);
        String userId = user.getId();

        String playlistId = playlistService.createPlaylist(userId, jwt);

        playlistService.addTracksToPlaylist(playlistId, trackUris, jwt);

        Playlist playlist = playlistService.getPlaylist(playlistId, jwt);

        model.addAttribute("playlist", playlist);
        model.addAttribute("playlistId", playlistId);
        return "playlist-result";
    }
}

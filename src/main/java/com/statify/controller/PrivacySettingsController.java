package com.statify.controller;

import com.statify.model.User;
import com.statify.service.UserService;
import com.statify.serviceDB.SpotifyUserService;
import com.statify.table.PrivacySettingsDB;
import com.statify.table.SpotifyUserDB;
import com.statify.repository.PrivacySettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PrivacySettingsController {

    @Autowired
    private UserService userService;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    @Autowired
    private PrivacySettingsRepository privacySettingsRepository;
    @Autowired
    private SpotifyUserService spotifyUserService;

    @GetMapping("/settings")
    public String getPrivacySettings(OAuth2AuthenticationToken authentication, Model model) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());
        String accessToken = client.getAccessToken().getTokenValue();
        User currentUser = userService.getCurrentUser(accessToken);
        SpotifyUserDB user = spotifyUserService.getUser(currentUser.getId());

        PrivacySettingsDB privacySettings = user.getPrivacySettings();
        model.addAttribute("privacySettings", privacySettings);
        model.addAttribute("loggedIn", true);
        return "settings";
    }

    @PostMapping("/updatePrivacySettings")
    public String updatePrivacySettings(
            OAuth2AuthenticationToken authentication,
            @RequestParam(defaultValue = "false") boolean showFollowers,
            @RequestParam(defaultValue = "false") boolean showFollowing,
            @RequestParam(defaultValue = "false") boolean showTopArtists,
            @RequestParam(defaultValue = "false") boolean showTopTracks,
            @RequestParam(defaultValue = "false") boolean showRecentlyPlayed
    ) {

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());
        String accessToken = client.getAccessToken().getTokenValue();
        User currentUser = userService.getCurrentUser(accessToken);
        SpotifyUserDB user = spotifyUserService.getUser(currentUser.getId());

        PrivacySettingsDB privacySettings = user.getPrivacySettings();
        privacySettings.setShowFollowers(showFollowers);
        privacySettings.setShowFollowing(showFollowing);
        privacySettings.setShowTopArtists(showTopArtists);
        privacySettings.setShowTopTracks(showTopTracks);
        privacySettings.setShowRecentlyPlayed(showRecentlyPlayed);
        privacySettingsRepository.save(privacySettings);
        return "redirect:/settings";
    }
}

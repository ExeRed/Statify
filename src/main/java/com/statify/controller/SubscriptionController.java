package com.statify.controller;

import com.statify.model.User;
import com.statify.service.UserService;
import com.statify.serviceDB.SpotifyUserService;
import com.statify.serviceDB.SubscriptionService;
import com.statify.table.SpotifyUserDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SpotifyUserService spotifyUserService;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    private UserService userService;


    @PostMapping("/{userId}/follow")
    public ResponseEntity<Void> followUser(@PathVariable String userId, OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());
        String accessToken = client.getAccessToken().getTokenValue();
        User user = userService.getCurrentUser(accessToken);
        SpotifyUserDB currentUser = spotifyUserService.getUser(user.getId());
        SpotifyUserDB followedUser = spotifyUserService.getUser(userId);
        subscriptionService.followUser(currentUser, followedUser);

        // Возвращаем пустой ответ с кодом 200
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/unfollow")
    public ResponseEntity<Void> unfollowUser(@PathVariable String userId, OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());

        String accessToken = client.getAccessToken().getTokenValue();
        User user = userService.getCurrentUser(accessToken);
        SpotifyUserDB currentUser = spotifyUserService.getUser(user.getId());
        SpotifyUserDB followedUser = spotifyUserService.getUser(userId);
        subscriptionService.unfollowUser(currentUser, followedUser);

        // Возвращаем пустой ответ с кодом 200
        return ResponseEntity.ok().build();
    }


    // Поиск пользователей
    @GetMapping("/search")
    @ResponseBody
    public List<SpotifyUserDB> searchUsers(@RequestParam("query") String query) {
        return spotifyUserService.searchUsers(query);
    }





}

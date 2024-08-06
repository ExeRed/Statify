package com.statify.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {
    String clientId = "a0afdb6374de488da6dfdd578b2b150e";
    String redirectUri = "http://localhost:8080/";

  //  @GetMapping("/login")
    public RedirectView login() {
        String spotifyLoginUrl = "https://accounts.spotify.com/authorize?" +
                "client_id=" + clientId +
                "&response_type=code" +
                "&redirect_uri=" + redirectUri +
                "&scope=user-read-private user-read-email" + // Укажите необходимые разрешения
                "&show_dialog=true"; // Параметр для показа диалогового окна каждый раз

        return new RedirectView(spotifyLoginUrl);
    }



}

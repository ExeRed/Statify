package com.statify.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session, Authentication authentication) {
        if (authentication instanceof OAuth2Authentication) {
            session.removeAttribute("token");
        }
        session.invalidate();

        return "home";
    }
}

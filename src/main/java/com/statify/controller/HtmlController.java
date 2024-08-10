package com.statify.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HtmlController {
    @GetMapping("/footer.html")
    public String getFooter() {
        return "footer";
    }

    @GetMapping("/header.html")
    public String getHeader(OAuth2AuthenticationToken authentication, Model model) {
        if (authentication != null) {
            model.addAttribute("loggedIn", true);
        }
        return "header";
    }
}
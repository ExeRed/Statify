package com.statify.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "home"; // Имя шаблона для главной страницы
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Имя шаблона для страницы входа
    }


}

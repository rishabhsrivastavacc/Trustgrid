package com.trustgrid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String home() {
        return "home";  // returns templates/home.html
    }
    @GetMapping("/chatbot")
    public String chatbot() {
        return "chatbot";  // returns templates/home.html
    }
}

package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String notes() {
        return "base";
    }
    @GetMapping("/login")
    public String login() {
        return "base";
    }
    @GetMapping("/register")
    public String register() {
        return "base";
    }
}

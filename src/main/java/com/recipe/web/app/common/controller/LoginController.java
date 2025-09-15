package com.recipe.web.app.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/local-login")
    public String loginPage() {
        return "login"; // login.html (Thymeleaf)
    }

    @GetMapping("/")
    public String home() {
        return "home"; // home.html
    }

    @GetMapping("/admin/dashboard")
    public String admin() {
        return "admin/dashboard";
    }
}
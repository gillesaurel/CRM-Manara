package com.manara.portail.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping("/")
    public String pageAccueil(HttpServletRequest request, Model model) {

        getCookies(request, model, "role");
        // Return the Thymeleaf template to render
        return "index";
    }


    private void getCookies(HttpServletRequest request, Model model, String cookieName) {

        // Get cookies from the request
        Cookie[] cookies = request.getCookies();

        String cookieValue = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Example: Check for a cookie named "userSession"
                if (cookieName.equals(cookie.getName())) {
                    cookieValue = cookie.getValue();
                    break;  // Exit once we find the cookie
                }
            }
        }
        System.out.println("cookieValue: " + cookieValue);
        // Add the cookie value to the model, so it can be used in Thymeleaf
        model.addAttribute("cookieValue", cookieValue);
    }
}

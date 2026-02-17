package com.manara.portail.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class demoController {

@GetMapping("/")
public String pageDemo(Model model){
    return "demo";
}
}

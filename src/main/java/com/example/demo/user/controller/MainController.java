package com.example.demo.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/")
    public String mainPage(@RequestParam(value = "message", required = false) String message,
                           @RequestParam(value = "error", required = false) String error,
                           Model model) {
        if (message != null) {
            model.addAttribute("message", message);
        }
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "main";
    }
}

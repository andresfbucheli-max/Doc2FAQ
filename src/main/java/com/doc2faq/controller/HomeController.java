package com.doc2faq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("appName", "Doc2FAQ");
        model.addAttribute("appDescription",
                "Transform your documents into structured FAQs automatically.");
        return "index";
    }

}

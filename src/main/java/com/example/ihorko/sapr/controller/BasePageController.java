package com.example.ihorko.sapr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BasePageController {

    @GetMapping("/arrays")
    public String arrayController(Model model) {
        String[] continents = {"testCOm",
                "Africa", "Antarctica", "Asia", "Australia",
                "Europe", "North America", "Sourth America"
        };

        model.addAttribute("continents", continents);

        return "continents";
    }
}

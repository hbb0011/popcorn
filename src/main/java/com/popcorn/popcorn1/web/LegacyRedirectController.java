package com.popcorn.popcorn1.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LegacyRedirectController {

    @GetMapping("/home")
    public String homeAlias() {
        return "redirect:/";
    }

    @GetMapping({"/movie", "/movies"})
    public String moviesAlias() {
        return "redirect:/list";
    }

    @GetMapping("/add")
    public String addAlias() {
        return "redirect:/admin/movie/create";
    }
}

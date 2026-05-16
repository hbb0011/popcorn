package com.popcorn.popcorn1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Laravel layout links to these paths; they are placeholders until implemented.
 */
@Controller
public class StubPagesController {

    @GetMapping({"/watchlist", "/community"})
    public String redirectStubs() {
        return "redirect:/";
    }
}

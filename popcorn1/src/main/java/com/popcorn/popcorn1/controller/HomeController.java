package com.popcorn.popcorn1.controller;

import com.popcorn.popcorn1.model.Movie;
import com.popcorn.popcorn1.repository.MovieRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final MovieRepository movieRepository;

    public HomeController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        List<Movie> trending = movieRepository.findByType("trending");
        List<Movie> topRated = movieRepository.findByType("top_rated");
        List<Movie> upcoming = movieRepository.findByType("upcoming");

        model.addAttribute("trending", trending);
        model.addAttribute("topRated", topRated);
        model.addAttribute("upcoming", upcoming);

        model.addAttribute("page", "home");
        return "layout";
    }
}
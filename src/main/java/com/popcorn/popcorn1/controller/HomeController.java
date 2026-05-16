package com.popcorn.popcorn1.controller;

import com.popcorn.popcorn1.model.Movie;
import com.popcorn.popcorn1.repository.MovieRepository;
import org.springframework.data.domain.PageRequest;
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

    @GetMapping("/")
    public String index(Model model) {
        PageRequest eight = PageRequest.of(0, 8);
        List<Movie> trending = movieRepository.findByType("trending", eight);
        List<Movie> topRated = movieRepository.findByType("top_rated", eight);
        List<Movie> upcoming = movieRepository.findByType("upcoming", eight);
        List<Movie> series = movieRepository.findByType("series", eight);

        model.addAttribute("trending", trending);
        model.addAttribute("topRated", topRated);
        model.addAttribute("upcoming", upcoming);
        model.addAttribute("series", series);

        model.addAttribute("activeNav", "home");
        return "home";
    }
}

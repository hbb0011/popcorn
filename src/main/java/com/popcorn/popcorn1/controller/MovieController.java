package com.popcorn.popcorn1.controller;

import com.popcorn.popcorn1.model.Movie;
import com.popcorn.popcorn1.repository.MovieRepository;
import com.popcorn.popcorn1.service.OmdbClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class MovieController {

    private final MovieRepository movieRepository;
    private final OmdbClient omdbClient;

    public MovieController(MovieRepository movieRepository, OmdbClient omdbClient) {
        this.movieRepository = movieRepository;
        this.omdbClient = omdbClient;
    }

    @GetMapping("/movie/{id}")
    public String show(@PathVariable Long id, Model model) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Movie not found"));
        model.addAttribute("movie", movie);
        model.addAttribute("activeNav", "movies");
        model.addAttribute("addMovieDetailCss", true);
        return "movie";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
        model.addAttribute("activeNav", "list");
        return "list";
    }

    @GetMapping("/admin/movie/create")
    public String createForm(Model model) {
        model.addAttribute("activeNav", "admin");
        return "admin/create";
    }

    @PostMapping("/admin/movie/store")
    public String store(@RequestParam("movie_name") String movieName, RedirectAttributes redirectAttributes) {
        try {
            Movie m = omdbClient.fetchByTitle(movieName.trim());
            movieRepository.save(m);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/admin/movie/create";
        }
        return "redirect:/";
    }
}

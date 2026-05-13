package com.popcorn.popcorn1.controller;

import com.popcorn.popcorn1.model.Movie;
import com.popcorn.popcorn1.model.User;
import com.popcorn.popcorn1.repository.MovieRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

@Controller
public class MovieController {

    @Value("${omdb.api.key}")
    private String apiKey;

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/movie/{id}")
    public String show(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        model.addAttribute("movie", movie);

        return "movie";
    }

    @GetMapping({"/movie", "/movies"})
    public String movies(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        model.addAttribute("movies", movieRepository.findAll());

        model.addAttribute("page", "movies");

        return "layout";
    }

    @GetMapping("/add")
    public String addPage(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/home";
        }

        return "addMovie";
    }

    @PostMapping("/add")
    public String addMovie(@RequestParam String movie_name, HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/home";
        }

        String url = "http://www.omdbapi.com/?apikey=" + apiKey + "&t=" + movie_name;

        RestTemplate rt = new RestTemplate();
        Map<String, Object> movie = rt.getForObject(url, Map.class);

        Movie m = new Movie();

        m.setTitle((String) movie.get("Title"));
        m.setDiscreption((String) movie.get("Plot"));
        m.setGenre((String) movie.get("Genre"));
        m.setPoster((String) movie.get("Poster"));
        try {
            m.setYear(Integer.parseInt(movie.get("Year").toString()));
        } catch (Exception e) {
            m.setYear(0);
        }
        m.setType((String) movie.get("Type"));

        try {
            m.setRate((int) Math.round(Double.parseDouble(movie.get("imdbRating").toString())));
        } catch (Exception e) {
            m.setRate(0);
        }

        m.setRank(0);

        movieRepository.save(m);

        return "redirect:/movies";
    }


}
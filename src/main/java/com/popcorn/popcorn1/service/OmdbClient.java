package com.popcorn.popcorn1.service;

import com.popcorn.popcorn1.model.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Service
public class OmdbClient {

    @Value("${omdb.api.key}")
    private String apiKey;

    public Movie fetchByTitle(String title) {

        URI uri = UriComponentsBuilder
                .fromUriString("http://www.omdbapi.com/")
                .queryParam("apikey", apiKey)
                .queryParam("t", title)
                .build()
                .toUri();

        RestTemplate rt = new RestTemplate();

        Map<String, Object> movie = rt.getForObject(uri, Map.class);

        if (movie == null || movie.get("Response").equals("False")) {
            throw new RuntimeException("Movie not found");
        }

        Movie m = new Movie();

        m.setTitle((String) movie.get("Title"));
        m.setDescription((String) movie.get("Plot"));
        m.setGenre((String) movie.get("Genre"));
        m.setPoster((String) movie.get("Poster"));

        m.setActors((String) movie.get("Actors"));
        m.setDirector((String) movie.get("Director"));
        m.setLanguage((String) movie.get("Language"));
        m.setCountry((String) movie.get("Country"));
        m.setAwards((String) movie.get("Awards"));
        m.setRuntime((String) movie.get("Runtime"));
        m.setType((String) movie.get("Type"));

        // YEAR
        try {
            m.setYear(movie.get("Year").toString().substring(0,4));
        } catch (Exception e) {
            m.setYear("0");
        }

        // RATING
        try {
            String rating = movie.get("imdbRating").toString();

            if (rating.equals("N/A")) {
                m.setRate("0");
            } else {
                m.setRate(rating);
            }

        } catch (Exception e) {
            m.setRate("0");
        }

        return m;
    }
}
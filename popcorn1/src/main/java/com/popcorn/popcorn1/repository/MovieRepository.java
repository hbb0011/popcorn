package com.popcorn.popcorn1.repository;

import com.popcorn.popcorn1.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByType(String type);
}
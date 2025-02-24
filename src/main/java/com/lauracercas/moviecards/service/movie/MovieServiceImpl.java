package com.lauracercas.moviecards.service.movie;


import com.lauracercas.moviecards.model.Movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Autor: Laura Cercas Ramos
 * Proyecto: TFM Integración Continua con GitHub Actions
 * Fecha: 04/06/2024
 */
@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    RestTemplate template;

    String url = "https://moviecards-service-delhoyo.azurewebsites.net/movies";

    @Override
    public List<Movie> getAllMovies() {
        Movie[] movies = template.getForObject(url, Movie[].class);
        List<Movie> movieList = List.of(movies);

        return movieList;
    }

    @Override
    public Movie save(Movie movie) {
        Movie savedMovie = template.postForObject(url, movie, Movie.class);
        return savedMovie;
    }

    @Override
    public Movie getMovieById(Integer movieId) {
        Movie movie = template.getForObject(url + "/" + movieId, Movie.class);
        return movie;
    }
}

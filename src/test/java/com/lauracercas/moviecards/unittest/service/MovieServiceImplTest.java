package com.lauracercas.moviecards.unittest.service;

import com.lauracercas.moviecards.model.Movie;
import com.lauracercas.moviecards.service.movie.MovieServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * Autor: Laura Cercas Ramos
 * Proyecto: TFM Integración Continua con GitHub Actions
 * Fecha: 04/06/2024
 */
class MovieServiceImplTest {

    private MockRestServiceServer mockServer;
    private RestTemplate restTemplate = new RestTemplate();

    @Mock
    private MovieServiceImpl sut;
    private AutoCloseable closeable;

    private static final String BASE_URL = "https://moviecards-service-delhoyo.azurewebsites.net/movies";

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
        mockServer = MockRestServiceServer.createServer(restTemplate);
        sut = new MovieServiceImpl(restTemplate);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void shouldGetAllMovies() {

        String jsonResponse = "[{\"id\":1, \"title\":\"Sample Movie\", \"releaseDate\":\"2000-01-01T23:00:00.000+00:00\", \"genre\":\"Action\", \"actors\":[]}, {\"id\":2, \"title\":\"Sample Movie 2\", \"releaseDate\":\"2000-01-01T23:00:00.000+00:00\", \"genre\":\"Action\", \"actors\":[]}]";

        mockServer.expect(MockRestRequestMatchers.requestTo(BASE_URL))
                .andRespond(MockRestResponseCreators.withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        List<Movie> result = sut.getAllMovies();

        assertEquals(2, result.size());

        mockServer.verify();
    }

    @Test
    public void shouldGetMovieById() {

        String jsonResponse = "{\"id\":1, \"title\":\"Sample Movie\", \"releaseDate\":\"2000-01-01T23:00:00.000+00:00\", \"genre\":\"Action\", \"actors\":[]}";

        mockServer.expect(MockRestRequestMatchers.requestTo(BASE_URL + "/1"))
                .andRespond(MockRestResponseCreators.withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        Movie result = sut.getMovieById(1);

        assertEquals(1, result.getId());
        assertEquals("Sample Movie", result.getTitle());
    }

    @Test
    public void shouldSaveMovie() {

        String jsonResponse = "{\"id\":1, \"title\":\"New Movie\"}";

        mockServer.expect(MockRestRequestMatchers.requestTo(BASE_URL))
                .andRespond(MockRestResponseCreators.withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        Movie movie = new Movie();
        movie.setTitle("New Movie");

        Movie result = sut.save(movie);
        assertEquals("New Movie", result.getTitle());

        mockServer.verify();
    }


}
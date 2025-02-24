package com.lauracercas.moviecards.unittest.service;

import com.lauracercas.moviecards.model.Actor;
import com.lauracercas.moviecards.service.actor.ActorServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * Autor: Laura Cercas Ramos
 * Proyecto: TFM Integración Continua con GitHub Actions
 * Fecha: 04/06/2024
 */
class ActorServiceImplTest {

    private MockRestServiceServer mockServer;
    private RestTemplate restTemplate = new RestTemplate();

    @Mock
    private ActorServiceImpl sut;
    private AutoCloseable closeable;

    private static final String BASE_URL = "https://moviecards-service-delhoyo.azurewebsites.net/actors";

    @BeforeEach
    void setUp() {
        closeable = openMocks(this);
        mockServer = MockRestServiceServer.createServer(restTemplate);
        sut = new ActorServiceImpl(restTemplate);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void shouldGetAllActors() {

        String jsonResponse = "[{\"id\":1, \"name\":\"Sample Actor\", \"birthDate\":\"2000-01-01T23:00:00.000+00:00\", \"country\":\"Spain\", \"movies\":[]}]";

        mockServer.expect(MockRestRequestMatchers.requestTo(BASE_URL))
                .andRespond(MockRestResponseCreators.withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        List<Actor> result = sut.getAllActors();

        assertEquals(1, result.size());

        mockServer.verify();
    }

    @Test
    public void shouldGetActorById() {

        String jsonResponse = "{\"id\":1, \"name\":\"Sample Actor\", \"birthDate\":\"2000-01-01T23:00:00.000+00:00\", \"country\":\"Spain\", \"movies\":[]}";

        mockServer.expect(MockRestRequestMatchers.requestTo(BASE_URL + "/1"))
                .andRespond(MockRestResponseCreators.withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        Actor result = sut.getActorById(1);

        assertEquals(1, result.getId());
        assertEquals("Sample Actor", result.getName());

        mockServer.verify();
    }

    @Test
    public void shouldSaveActor() throws Exception {
        String jsonRequest = "{\"name\":\"New Actor\"}";
        String jsonResponse = "{\"id\":1, \"name\":\"New Actor\"}";
        
        mockServer.expect(MockRestRequestMatchers.requestTo(BASE_URL))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
            .andExpect(MockRestRequestMatchers.content().json(jsonRequest))
            .andRespond(MockRestResponseCreators.withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        Actor actor = new Actor();
        actor.setName("New Actor");

        Actor result = sut.save(actor);
        assertEquals("New Actor", result.getName());

        mockServer.verify();
    }

}
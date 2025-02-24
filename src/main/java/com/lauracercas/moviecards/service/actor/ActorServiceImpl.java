package com.lauracercas.moviecards.service.actor;


import com.lauracercas.moviecards.model.Actor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Autor: Laura Cercas Ramos
 * Proyecto: TFM Integración Continua con GitHub Actions
 * Fecha: 04/06/2024
 */
@Service
public class ActorServiceImpl implements ActorService {

    @Autowired
    RestTemplate template;

    String url = "https://moviecards-service-delhoyo.azurewebsites.net/actors";

    @Override
    public List<Actor> getAllActors() {
        Actor[] actores = template.getForObject(url, Actor[].class);
        List<Actor> actorList = Arrays.asList(actores);

        return actorList;
    }

    @Override
    public Actor save(Actor actor) {
        Actor savedActor = template.postForObject(url, actor, Actor.class);
        return savedActor;
    }

    @Override
    public Actor getActorById(Integer actorId) {
        Actor actor = template.getForObject(url + "/" + actorId, Actor.class);
        return actor;
    }
}

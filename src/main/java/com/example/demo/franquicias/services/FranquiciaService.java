package com.example.demo.franquicias.services;

import com.example.demo.franquicias.models.Franquicia;
import com.example.demo.franquicias.models.Sucursal;
import com.example.demo.franquicias.repositories.FranquiciaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Clase encargada de crear nuevas franquicias
 */
@Service
public class FranquiciaService {
    private final FranquiciaRepository franquiciaRepository;

    /**
     * Constructor de la clase {@link FranquiciaService}
     * @param franquiciaRepository repositorio de franquicia
     */
    public FranquiciaService(FranquiciaRepository franquiciaRepository) {
        this.franquiciaRepository = franquiciaRepository;
    }

    /**
     * Método encargado de crear una nueva franquicia
     * @param franquicia datos de la franquicia
     * @return datos de la franquicia creada
     */
    public Mono<Franquicia> createFranquicia(Franquicia franquicia) {
        return franquiciaRepository.save(franquicia);
    }

    public Flux<Franquicia> getAllFranquicias() {
        return franquiciaRepository.findAll();
    }
}

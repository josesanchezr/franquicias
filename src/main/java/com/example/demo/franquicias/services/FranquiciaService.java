package com.example.demo.franquicias.services;

import com.example.demo.franquicias.exceptions.FranquiciaNotFoundException;
import com.example.demo.franquicias.models.Franquicia;
import com.example.demo.franquicias.repositories.FranquiciaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    /**
     * Método encargado de obtener todas las franquicias
     * @return listado de todas las franquicias
     */
    public Flux<Franquicia> getAllFranquicias() {
        return franquiciaRepository.findAll();
    }

    /**
     * Método encargado de actualizar el nombre de una franquicia
     * @param franquiciaId ID de la franquicia
     * @param newNombre nuevo nombre de la franquicia
     * @return datos de la franquicia actualizada
     */
    public Mono<Franquicia> updateNombreFranquicia(Long franquiciaId, String newNombre) {
        return franquiciaRepository.findById(franquiciaId)
                .switchIfEmpty(
                        Mono.error(
                                new FranquiciaNotFoundException(
                                        String.format("Franquicia con id %d no encontrada", franquiciaId)))
                ).flatMap(franquicia -> {
                    franquicia.setNombre(newNombre);
                    return franquiciaRepository.save(franquicia);
                });
    }
}

package com.example.demo.franquicias.repositories;

import com.example.demo.franquicias.models.Franquicia;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Repositorio para el manejo de los datos de franquicias
 */
public interface FranquiciaRepository extends ReactiveCrudRepository<Franquicia, Long> {
}

package com.example.demo.franquicias.repositories;

import com.example.demo.franquicias.models.Sucursal;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Repositorio para el manejo de datos de sucursales
 */
public interface SucursalRepository extends ReactiveCrudRepository<Sucursal, Long> {
}

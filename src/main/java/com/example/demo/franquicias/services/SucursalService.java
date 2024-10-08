package com.example.demo.franquicias.services;

import com.example.demo.franquicias.exceptions.SucursalNotFoundException;
import com.example.demo.franquicias.models.Sucursal;
import com.example.demo.franquicias.repositories.SucursalRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Clase encargada de crea nuevas sucursales
 */
@Service
public class SucursalService {

    private final SucursalRepository sucursalRepository;

    /**
     * Constructor de la clase {@link SucursalService}
     * @param sucursalRepository repositorio de sucursal
     */
    public SucursalService(SucursalRepository sucursalRepository) {
        this.sucursalRepository = sucursalRepository;
    }

    /**
     * Método encargado de crear una nueva sucursal
     * @param sucursal datos de la nueva sucursal
     * @return datos de la sucursal creada
     */
    public Mono<Sucursal> createSucursal(Sucursal sucursal) {
        return sucursalRepository.save(sucursal);
    }

    public Mono<Sucursal> updateNombreSucursal(Long sucursalId, String newNombre) {
        return sucursalRepository.findById(sucursalId)
                .switchIfEmpty(Mono.error(
                        new SucursalNotFoundException(
                                String.format("Sucursal con id %d no encontrada", sucursalId)
                        )
                ))
                .flatMap(sucursal -> {
                    sucursal.setNombre(newNombre);
                    return sucursalRepository.save(sucursal);
                });
    }
}

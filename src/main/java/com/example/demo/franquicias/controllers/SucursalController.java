package com.example.demo.franquicias.controllers;

import com.example.demo.franquicias.models.Sucursal;
import com.example.demo.franquicias.services.SucursalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Controlador que expone endpoints de sucursales
 */
@RestController
@RequestMapping("/api/sucursales")
@Slf4j
public class SucursalController {

    private final SucursalService sucursalService;

    /**
     * Constructor de la clase {@link SucursalController}
     * @param sucursalService service de sucursal
     */
    public SucursalController(SucursalService sucursalService) {
        this.sucursalService = sucursalService;
    }

    /**
     * Endpoint para crear una nueva sucursal
     * @param sucursal datos de la nueva sucursal
     * @return datos de la sucursal creada
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Sucursal> createSucursal(@RequestBody Sucursal sucursal) {
        log.info("Creando nueva sucursal con los datos: {}", sucursal.toString());
        return sucursalService.createSucursal(sucursal);
    }

    /**
     * Endpoint para actualizar el nombre de una sucursal
     * @param sucursalId ID de la sucursal
     * @param newNombre nuevo nombre de la sucursal
     * @return datos de la sucursal actualizada
     */
    @PutMapping("/{sucursalId}/update-nombre")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Sucursal> updateNombreSucursal(
            @PathVariable("sucursalId") Long sucursalId,
            @RequestParam("newNombre") String newNombre) {
        log.info("Actualizando el nombre de la sucursal {}", sucursalId);
        return sucursalService.updateNombreSucursal(sucursalId, newNombre);
    }
}

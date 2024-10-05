package com.example.demo.franquicias.controllers;

import com.example.demo.franquicias.models.Franquicia;
import com.example.demo.franquicias.models.Producto;
import com.example.demo.franquicias.models.Sucursal;
import com.example.demo.franquicias.services.FranquiciaService;
import com.example.demo.franquicias.services.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controlador que expone endpoints de franquicias
 */
@RestController
@RequestMapping("/api/franquicias")
@Slf4j
public class FranquiciaController {

    private final FranquiciaService franquiciaService;
    private final ProductoService productoService;

    /**
     * Constructor de la clase {@link FranquiciaController}
     * @param franquiciaService service de franquicia
     * @param productoService service de producto
     */
    public FranquiciaController(FranquiciaService franquiciaService, ProductoService productoService) {
        this.franquiciaService = franquiciaService;
        this.productoService = productoService;
    }

    /**
     * Endpoint para crear una nueva franquicia
     * @param franquicia datos de la nueva franquicia
     * @return datos de la franquicia creada
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franquicia> createFranquicia(@RequestBody Franquicia franquicia) {
        log.info("Creando nueva franquicia con el nombre: {}", franquicia.getNombre());
        return franquiciaService.createFranquicia(franquicia);
    }

    /**
     * Endpoint para obtener un listado de todas las franquicias
     * @return listado con los datos de todas las franquicias
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Franquicia> getAllFranquicias() {
        log.info("Obteniendo todas las franquicias");
        return franquiciaService.getAllFranquicias();
    }

    /**
     * Endpoint para obtener los productos con mayor stock de cada sucursal de una franquicia puntual
     * @param franquiciaId ID de la franquicia
     * @return Listado de productos con mayor stock
     */
    @GetMapping("/{franquiciaId}/productos-max-stock")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Producto> obtenerProductoConMayorStockPorSucursal(
            @PathVariable("franquiciaId") Long franquiciaId) {
        return productoService.obtenerProductoConMayorStockPorSucursal(franquiciaId);
    }
}

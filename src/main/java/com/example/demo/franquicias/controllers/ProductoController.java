package com.example.demo.franquicias.controllers;

import com.example.demo.franquicias.models.Producto;
import com.example.demo.franquicias.services.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Controlador que expone endpoints de productos
 */
@RestController
@RequestMapping("/api/productos")
@Slf4j
public class ProductoController {

    private final ProductoService productoService;

    /**
     * Constructor de la clase {@link ProductoController}
     * @param productoService service de producto
     */
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * Endpoint para crear un nuevo producto
     * @param producto datos del nuevo producto
     * @return datos del producto creado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Producto> createProducto(@RequestBody Producto producto) {
        log.info("Creando nuevo producto con los datos: {}", producto.toString());
        return productoService.createProducto(producto);
    }

    /**
     * Endpoint para eliminar un producto de una sucursal
     * @param productoId ID del producto a eliminar
     * @param sucursalId ID de la sucursal asociada al producto a eliminar
     * @return
     */
    @DeleteMapping("/{productoId}/sucursal/{sucursalId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> deleteProducto(
            @PathVariable("productoId") Long productoId,
            @PathVariable("sucursalId") Long sucursalId) {
        log.info("Eliminando el producto con el id: {} de la sucursal con el id: {}", productoId, sucursalId);
        return productoService.deleteProducto(productoId, sucursalId);
    }

    /**
     * Endpoint para actualizar el stock de un producto
     * @param productoId ID del producto
     * @param newStock nuevo stock del producto
     * @return datos del producto con el stock actualizado
     */
    @PutMapping("/{productoId}/stock")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Producto> updateProductoStock(
            @PathVariable("productoId") Long productoId,
            @RequestParam("newStock") Integer newStock) {
        return productoService.updateProductoStock(productoId, newStock);
    }
}

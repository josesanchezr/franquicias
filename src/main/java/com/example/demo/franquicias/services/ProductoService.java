package com.example.demo.franquicias.services;

import com.example.demo.franquicias.exceptions.ProductoNotFoundException;
import com.example.demo.franquicias.models.Producto;
import com.example.demo.franquicias.repositories.ProductoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Clase encargada de crear nuevos productos
 */
@Service
@Slf4j
public class ProductoService {

    private final ProductoRepository productoRepository;

    /**
     * Constructor de la clase {@link ProductoService}
     * @param productoRepository repositorio de producto
     */
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Método encargado de crear un nuevo producto
     * @param producto datos del nuevo producto
     * @return datos del producto creado
     */
    public Mono<Producto> createProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * Método encargado de eliminar un producto de una sucursal
     * @param productoId ID del producto
     * @param sucursalId ID de la sucursal
     * @return
     */
    public Mono<String> deleteProducto(Long productoId, Long sucursalId) {
        return productoRepository.deleteByIdAndSucursalId(productoId, sucursalId)
                .flatMap(count -> {
                    if (count == 0) {
                        String errorMessage = String.format("No se encontró un producto con id %d asociado a una sucursal con id %d", productoId, sucursalId);
                        log.error(errorMessage);
                        return Mono.error(new ProductoNotFoundException(errorMessage));
                    }
                    return Mono.just("Producto eliminado");
                });
    }

    /**
     * Método encargado de actualizar el stock de un producto
     * @param productoId ID del producto
     * @param newStock nuevo stock del producto
     * @return datos del producto con el stock actualizado
     */
    public Mono<Producto> updateProductoStock(Long productoId, Integer newStock) {
        return productoRepository.findById(productoId)
                .switchIfEmpty(
                        Mono.error(
                                new ProductoNotFoundException(String.format("Producto con id %d no encontrado",
                                        productoId)
                                )
                        )
                )
                .flatMap(producto -> {
                    producto.setStock(newStock);
                    return productoRepository.save(producto);
                });
    }

    /**
     * Método para obtener el producto con mayor stop por sucursal
     * @param franquiciaId ID de la franquicia
     * @return Listado de productos con mayor stock
     */
    public Flux<Producto> obtenerProductoConMayorStockPorSucursal(Long franquiciaId) {
        return productoRepository.findMaxStockByFranquicia(franquiciaId);
    }

    /**
     * Método encargado de actualizar el nombre de un producto
     * @param productoId ID del producto
     * @param newNombre nuevo nombre del producto
     * @return datos del producto actualizado
     */
    public Mono<Producto> updateNombreProducto(Long productoId, String newNombre) {
        return productoRepository.findById(productoId)
                .switchIfEmpty(Mono.error(
                        new ProductoNotFoundException(
                                String.format("Producto con id %d no encontrado", productoId)
                        )
                ))
                .flatMap(producto -> {
                    producto.setNombre(newNombre);
                    return productoRepository.save(producto);
                });
    }
}

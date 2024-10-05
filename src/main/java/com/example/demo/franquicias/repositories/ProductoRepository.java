package com.example.demo.franquicias.repositories;

import com.example.demo.franquicias.models.Producto;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio para el manejo de los datos de productos
 */
public interface ProductoRepository extends ReactiveCrudRepository<Producto, Long> {

    /**
     * Método para eliminar un producto de una sucursal
     * @param productoId ID del producto
     * @param sucursalId ID de la sucursal
     * @return cantidad de productos eliminadas
     */
    public Mono<Integer> deleteByIdAndSucursalId(Long productoId, Long sucursalId);

    /**
     * Método para obtener el producto con mayor stop por sucursal
     * de una franquicia especifica
     * @param franquiciaId ID de la franquicia
     * @return Listado de productos con mayor stock
     */
    @Query(value = """
        SELECT p.* 
        FROM producto p 
        JOIN sucursal s ON p.sucursal_id = s.id 
        JOIN franquicia f ON s.franquicia_id = f.id 
        WHERE f.id = :franquiciaId 
        AND p.stock = (
            SELECT MAX(p2.stock) 
            FROM producto p2 
            WHERE p2.sucursal_id = s.id
        )
        """)
    Flux<Producto> findMaxStockByFranquicia(Long franquiciaId);
}

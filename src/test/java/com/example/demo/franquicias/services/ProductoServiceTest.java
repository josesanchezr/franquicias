package com.example.demo.franquicias.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import com.example.demo.franquicias.exceptions.ProductoNotFoundException;
import com.example.demo.franquicias.models.Producto;
import com.example.demo.franquicias.repositories.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

/**
 * Clase para los unit tests de la clase ProductoService
 */
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test que verifica que se haya guardado un producto nuevo
     */
    @Test
    void testCreateProducto() {
        Producto producto = Producto.builder()
                .nombre("Producto 1")
                .stock(100)
                .sucursalId(1L)
                .build();

        // When
        when(productoRepository.save(any(Producto.class))).thenReturn(Mono.just(producto));

        Mono<Producto> result = productoService.createProducto(producto);

        // Verify
        StepVerifier.create(result)
                .expectNext(producto) // Verifica que el producto devuelto es el esperado
                .verifyComplete(); // Verifica que el Mono complete sin errores
    }

    /**
     * Test que verifica que se haya eliminado un producto
     */
    @Test
    void testDeleteProducto() {
        Long productoId = 1L; // ID del producto a eliminar
        Long sucursalId = 1L; // ID de la sucursal asociada

        // When
        when(productoRepository.deleteByIdAndSucursalId(productoId, sucursalId)).thenReturn(Mono.just(1));

        Mono<String> result = productoService.deleteProducto(productoId, sucursalId);

        // Verify
        StepVerifier.create(result)
                .expectNext("Producto eliminado") // Verifica que el mensaje devuelto sea el esperado
                .verifyComplete(); // Verifica que el Mono complete sin errores
    }

    /**
     * Test que verifica que se lance la excepción ProductoNotFoundException
     * al intentar eliminar un producto que no existe
     */
    @Test
    void testDeleteProductoNotFound() {
        Long productoId = 1L; // ID del producto a eliminar
        Long sucursalId = 1L; // ID de la sucursal asociada

        // When
        when(productoRepository.deleteByIdAndSucursalId(productoId, sucursalId)).thenReturn(Mono.just(0));

        Mono<String> result = productoService.deleteProducto(productoId, sucursalId);

        // Verify
        StepVerifier.create(result)
                .expectError(ProductoNotFoundException.class) // Verifica que se lanzó la excepción esperada
                .verify(); // Verifica que la secuencia complete con un error
    }

    /**
     * Test para actualizar el stock de un producto
     */
    @Test
    void testUpdateProductoStock_success() {
        Long productoId = 1L; // ID del producto
        Integer newStock = 50; // Nuevo stock del producto

        // Crea un objeto Producto de ejemplo
        Producto productoExistente = Producto.builder()
                .id(productoId)
                .stock(10)
                .build();

        // When
        when(productoRepository.findById(productoId)).thenReturn(Mono.just(productoExistente));
        when(productoRepository.save(productoExistente)).thenReturn(Mono.just(productoExistente));

        Mono<Producto> result = productoService.updateProductoStock(productoId, newStock);

        // Verify
        StepVerifier.create(result)
                .expectNextMatches(producto -> producto.getStock().equals(newStock)) // Verifica que el stock se haya actualizado
                .verifyComplete(); // Verifica que el Mono complete sin errores

        // Verifica que el productoRepository.save fue llamado
        verify(productoRepository, times(1)).save(productoExistente);
    }

    /**
     * Test para actualizar el stock de un producto, donde el producto no existe
     */
    @Test
    void testUpdateProductoStock_productoNotFound() {
        Long productoId = 1L; // ID del producto que no existe
        Integer newStock = 50; // Nuevo stock del producto

        // When
        when(productoRepository.findById(productoId)).thenReturn(Mono.empty());

        Mono<Producto> result = productoService.updateProductoStock(productoId, newStock);

        // Verify
        StepVerifier.create(result)
                .expectError(ProductoNotFoundException.class) // Verifica que se lanzó la excepción esperada
                .verify(); // Verifica que la secuencia complete con un error

        // Verifica que productoRepository.save nunca fue llamado
        verify(productoRepository, times(0)).save(any());
    }

    /**
     * Test para obtener producto con mayor stock por sucursal, donde se obtienen resultados
     */
    @Test
    void testObtenerProductoConMayorStockPorSucursal_success() {
        Long franquiciaId = 1L; // ID de la franquicia para la cual se buscan los productos

        // Crea algunos objetos Producto de ejemplo
        Producto producto1 = Producto.builder()
                .id(1L)
                .nombre("Producto 1")
                .stock(100)
                .build();

        Producto producto2 = Producto.builder()
                .id(2L)
                .nombre("Producto 2")
                .stock(150)
                .build();

        // Simula los productos devueltos por el repositorio
        List<Producto> productos = Arrays.asList(producto1, producto2);

        // When
        when(productoRepository.findMaxStockByFranquicia(franquiciaId)).thenReturn(Flux.fromIterable(productos));

        Flux<Producto> result = productoService.obtenerProductoConMayorStockPorSucursal(franquiciaId);

        // Verify
        StepVerifier.create(result)
                .expectNext(producto1) // Verifica que el primer producto sea el esperado
                .expectNext(producto2) // Verifica que el segundo producto sea el esperado
                .verifyComplete(); // Verifica que el Flux complete sin errores

        // Verifica que findMaxStockByFranquicia fue llamado exactamente una vez
        verify(productoRepository, times(1)).findMaxStockByFranquicia(franquiciaId);
    }

    /**
     * Test para obtener producto con mayor stock por sucursal, donde no se encuentran resultados
     */
    @Test
    void testObtenerProductoConMayorStockPorSucursal_noProductsFound() {
        Long franquiciaId = 1L; // ID de la franquicia para la cual se buscan los productos

        // Configura el mock para que devuelva un Flux vacío cuando no haya productos
        when(productoRepository.findMaxStockByFranquicia(franquiciaId)).thenReturn(Flux.empty());

        // Llama al método a probar
        Flux<Producto> result = productoService.obtenerProductoConMayorStockPorSucursal(franquiciaId);

        // Verifica que el Flux esté vacío
        StepVerifier.create(result)
                .expectNextCount(0) // Verifica que no se emitieron productos
                .verifyComplete(); // Verifica que el Flux complete sin errores

        // Verifica que findMaxStockByFranquicia fue llamado exactamente una vez
        verify(productoRepository, times(1)).findMaxStockByFranquicia(franquiciaId);
    }

    /**
     * Test para actualizar el nombre de un producto
     */
    @Test
    void testUpdateNombreProducto_success() {
        Long productoId = 1L; // ID del producto a actualizar
        String newNombre = "Nuevo Nombre";

        // Crea un producto de ejemplo
        Producto producto = Producto.builder()
                .id(productoId)
                .nombre("Nombre Viejo")
                .stock(100)
                .build();

        // When
        when(productoRepository.findById(productoId)).thenReturn(Mono.just(producto));
        when(productoRepository.save(producto)).thenReturn(Mono.just(producto));

        Mono<Producto> result = productoService.updateNombreProducto(productoId, newNombre);

        // Verifica el resultado utilizando StepVerifier
        StepVerifier.create(result)
                .expectNextMatches(updatedProducto -> updatedProducto.getNombre().equals(newNombre)) // Verifica que el nombre haya sido actualizado correctamente
                .verifyComplete(); // Verifica que el Mono complete sin errores

        // Verifica que el método findById fue llamado una vez con el productoId correcto
        verify(productoRepository, times(1)).findById(productoId);

        // Verifica que el método save fue llamado una vez con el producto actualizado
        verify(productoRepository, times(1)).save(producto);
    }

    /**
     * Test para actualizar el nombre de un producto, donde el producto no existe
     */
    @Test
    void testUpdateNombreProducto_notFound() {
        Long productoId = 1L; // ID del producto a actualizar
        String newNombre = "Nuevo Nombre";

        // When
        when(productoRepository.findById(productoId)).thenReturn(Mono.empty());

        Mono<Producto> result = productoService.updateNombreProducto(productoId, newNombre);

        // Verify
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ProductoNotFoundException
                        && throwable.getMessage().equals(String.format("Producto con id %d no encontrado", productoId)))
                .verify(); // Verifica que el Mono termine con el error esperado

        // Verifica que el método findById fue llamado una vez con el productoId correcto
        verify(productoRepository, times(1)).findById(productoId);

        // Verifica que el método save no fue llamado ya que el producto no existe
        verify(productoRepository, times(0)).save(null); // Verificación de que no se llama a save
    }
}

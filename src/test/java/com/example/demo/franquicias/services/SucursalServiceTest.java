package com.example.demo.franquicias.services;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import com.example.demo.franquicias.exceptions.SucursalNotFoundException;
import com.example.demo.franquicias.models.Sucursal;
import com.example.demo.franquicias.repositories.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Clase para los unit tests de la clase SucursalService
 */
public class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @InjectMocks
    private SucursalService sucursalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test que valida que se haya creado con exito una sucursal nueva
     */
    @Test
    void testCreateSucursal_success() {
        // Crea un objeto Sucursal de ejemplo
        Sucursal sucursal = Sucursal.builder()
                .id(1L)
                .nombre("Sucursal 1")
                .build();

        // When
        when(sucursalRepository.save(sucursal)).thenReturn(Mono.just(sucursal));

        Mono<Sucursal> result = sucursalService.createSucursal(sucursal);

        // Verify
        StepVerifier.create(result)
                .expectNextMatches(savedSucursal ->
                        savedSucursal.getNombre().equals("Sucursal 1") && savedSucursal.getId().equals(1L))
                .verifyComplete(); // Verifica que el Mono complete sin errores

        // Verifica que el método save fue llamado una vez con la sucursal correcta
        verify(sucursalRepository, times(1)).save(sucursal);
    }

    /**
     * Test que valida que no se haya creado con exito una nueva sucursal
     */
    @Test
    void testCreateSucursal_error() {
        // Crea un objeto Sucursal de ejemplo con datos incompletos o erróneos
        Sucursal sucursal = Sucursal.builder()
                .nombre("Sucursal Nueva")
                .build();

        // When
        when(sucursalRepository.save(sucursal)).thenReturn(Mono.error(new RuntimeException("Error al guardar la sucursal")));
        Mono<Sucursal> result = sucursalService.createSucursal(sucursal);

        // Verify
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("Error al guardar la sucursal"))
                .verify(); // Verifica que el Mono termine con el error esperado

        // Verifica que el método save fue llamado una vez con la sucursal correcta
        verify(sucursalRepository, times(1)).save(sucursal);
    }

    /**
     * Test para actualizar el nombre de una sucursal con exito.
     */
    @Test
    void testUpdateNombreSucursal_success() {
        // Datos de entrada
        Long sucursalId = 1L;
        String nuevoNombre = "Sucursal Actualizada";

        // Crea un objeto Sucursal de ejemplo
        Sucursal sucursal = Sucursal.builder()
                .id(sucursalId)
                .nombre("Sucursal 1")
                .build();

        // When
        when(sucursalRepository.findById(sucursalId)).thenReturn(Mono.just(sucursal));
        when(sucursalRepository.save(sucursal)).thenReturn(Mono.just(sucursal));

        Mono<Sucursal> result = sucursalService.updateNombreSucursal(sucursalId, nuevoNombre);

        // Verify
        StepVerifier.create(result)
                .expectNextMatches(updatedSucursal ->
                        updatedSucursal.getId().equals(sucursalId) &&
                                updatedSucursal.getNombre().equals(nuevoNombre)
                )
                .verifyComplete(); // Verifica que el Mono complete sin errores

        // Verifica que el método findById y save fueron llamados
        verify(sucursalRepository, times(1)).findById(sucursalId);
        verify(sucursalRepository, times(1)).save(sucursal);
    }

    /**
     * Test para actualizar el nombre de una sucursal, donde la sucursal no existe
     */
    @Test
    void testUpdateNombreSucursal_sucursalNotFound() {
        // Datos de entrada
        Long sucursalId = 1L;
        String nuevoNombre = "Sucursal Actualizada";

        // When
        when(sucursalRepository.findById(sucursalId)).thenReturn(Mono.empty());

        Mono<Sucursal> result = sucursalService.updateNombreSucursal(sucursalId, nuevoNombre);

        // Verify
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof SucursalNotFoundException
                        && throwable.getMessage().equals(String.format("Sucursal con id %d no encontrada", sucursalId)))
                .verify(); // Verifica que el Mono termine con el error esperado

        // Verifica que el método findById fue llamado pero no save
        verify(sucursalRepository, times(1)).findById(sucursalId);
        verify(sucursalRepository, times(0)).save(null); // save no debe ser llamado
    }
}

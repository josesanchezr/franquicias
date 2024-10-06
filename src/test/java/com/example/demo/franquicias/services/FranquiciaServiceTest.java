package com.example.demo.franquicias.services;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import com.example.demo.franquicias.exceptions.FranquiciaNotFoundException;
import com.example.demo.franquicias.models.Franquicia;
import com.example.demo.franquicias.repositories.FranquiciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

/**
 * Clase para los unit tests de la clase FranquiciaService
 */
public class FranquiciaServiceTest {

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @InjectMocks
    private FranquiciaService franquiciaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test para crear una franquicia con exito
     */
    @Test
    void testCreateFranquicia_success() {
        // Datos de entrada
        Franquicia franquicia = Franquicia.builder()
                .nombre("Franquicia 1")
                .build();

        // When
        when(franquiciaRepository.save(franquicia)).thenReturn(Mono.just(franquicia));

        Mono<Franquicia> result = franquiciaService.createFranquicia(franquicia);

        // Verify
        StepVerifier.create(result)
                .expectNextMatches(savedFranquicia ->
                        savedFranquicia.getNombre().equals("Franquicia 1")
                )
                .verifyComplete(); // Verifica que el Mono complete sin errores

        // Verifica que el método save fue llamado
        verify(franquiciaRepository, times(1)).save(franquicia);
    }

    /**
     * Test para validar error al guardar una franquicia
     */
    @Test
    void testCreateFranquicia_error() {
        // Datos de entrada
        Franquicia franquicia = Franquicia.builder()
                .nombre("Franquicia 1")
                .build();

        // When
        when(franquiciaRepository.save(franquicia)).thenReturn(Mono.error(new RuntimeException("Error al guardar franquicia")));

        Mono<Franquicia> result = franquiciaService.createFranquicia(franquicia);

        // Verify
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("Error al guardar franquicia"))
                .verify(); // Verifica que el Mono termine con el error esperado

        // Verifica que el método save fue llamado
        verify(franquiciaRepository, times(1)).save(franquicia);
    }

    /**
     * Test que obtiene todos las franquicias con exito
     */
    @Test
    void testGetAllFranquicias_success() {
        // Datos de entrada: una lista de franquicias
        Franquicia franquicia1 = Franquicia.builder()
                .nombre("Franquicia 1")
                .build();

        Franquicia franquicia2 = Franquicia.builder()
                .nombre("Franquicia 2")
                .build();

        // When
        when(franquiciaRepository.findAll()).thenReturn(Flux.fromIterable(Arrays.asList(franquicia1, franquicia2)));

        Flux<Franquicia> result = franquiciaService.getAllFranquicias();

        // When
        StepVerifier.create(result)
                .expectNextMatches(franquicia -> franquicia.getNombre().equals("Franquicia 1"))
                .expectNextMatches(franquicia -> franquicia.getNombre().equals("Franquicia 2"))
                .verifyComplete(); // Verifica que el Flux complete sin errores

        // Verifica que el método findAll fue llamado una vez
        verify(franquiciaRepository, times(1)).findAll();
    }

    /**
     * Test para validar que no hay franquicias creadas
     */
    @Test
    void testGetAllFranquicias_empty() {
        // When
        when(franquiciaRepository.findAll()).thenReturn(Flux.empty());

        Flux<Franquicia> result = franquiciaService.getAllFranquicias();

        // Verify
        StepVerifier.create(result)
                .expectNextCount(0)  // Verifica que no haya elementos en el Flux
                .verifyComplete();    // Verifica que el Flux complete sin errores

        // Verifica que el método findAll fue llamado una vez
        verify(franquiciaRepository, times(1)).findAll();
    }

    /**
     * Test que actualiza el nombre de una franquicia con exito
     */
    @Test
    void testUpdateNombreFranquicia_success() {
        // Datos de entrada: una franquicia existente
        Long franquiciaId = 1L;
        String nuevoNombre = "Nuevo Nombre";
        Franquicia franquicia = Franquicia.builder()
                .id(franquiciaId)
                .nombre("Viejo nombre")
                .build();

        // When
        when(franquiciaRepository.findById(franquiciaId)).thenReturn(Mono.just(franquicia));
        when(franquiciaRepository.save(franquicia)).thenReturn(Mono.just(franquicia));

        Mono<Franquicia> result = franquiciaService.updateNombreFranquicia(franquiciaId, nuevoNombre);

        // Verify
        StepVerifier.create(result)
                .expectNextMatches(f -> f.getNombre().equals(nuevoNombre)) // Verifica que el nombre se ha actualizado
                .verifyComplete(); // Verifica que el Mono complete sin errores

        // Verifica que los métodos del repositorio fueron llamados correctamente
        verify(franquiciaRepository, times(1)).findById(franquiciaId);
        verify(franquiciaRepository, times(1)).save(franquicia);
    }

    /**
     * Test para actualizar el nombre de una franquicia, donde no existe la franquicia
     */
    @Test
    void testUpdateNombreFranquicia_notFound() {
        // Datos de entrada: ID de franquicia no existente
        Long franquiciaId = 2L;
        String nuevoNombre = "Nuevo Nombre";

        // When
        when(franquiciaRepository.findById(franquiciaId)).thenReturn(Mono.empty());

        Mono<Franquicia> result = franquiciaService.updateNombreFranquicia(franquiciaId, nuevoNombre);

        // Verify
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof FranquiciaNotFoundException &&
                                throwable.getMessage().equals(String.format("Franquicia con id %d no encontrada", franquiciaId))
                )
                .verify();

        // Verifica que el repositorio buscó la franquicia pero no intentó guardarla
        verify(franquiciaRepository, times(1)).findById(franquiciaId);
        verify(franquiciaRepository, times(0)).save(null); // No debe llamar al método save
    }
}

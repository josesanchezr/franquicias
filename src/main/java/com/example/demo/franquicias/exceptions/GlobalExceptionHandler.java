package com.example.demo.franquicias.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

/**
 * Manejador de excepciones global
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Método para manejar las excepciones de tipo {@link ProductoNotFoundException}
     * @param exception excepción con el mensaje de error
     * @return excepción con mensaje de error
     */
    @ExceptionHandler(ProductoNotFoundException.class)
    public Mono<ResponseEntity<ResponseError>> handleProductoNotFoundException(ProductoNotFoundException exception) {
        return Mono.just(
                ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(this.createResponseError(exception.getMessage())));
    }

    /**
     * Método para manejar las excepciones de tipo {@link SucursalNotFoundException}
     * @param exception excepción con el mensaje de error
     * @return excepción con mensaje de error
     */
    @ExceptionHandler(SucursalNotFoundException.class)
    public Mono<ResponseEntity<ResponseError>> handleSucursalNotFoundException(SucursalNotFoundException exception) {
        return Mono.just(
                ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(this.createResponseError(exception.getMessage())));
    }

    /**
     * Método para manejar las excepciones de tipo {@link FranquiciaNotFoundException}
     * @param exception excepción con el mensaje de error
     * @return excepción con mensaje de error
     */
    @ExceptionHandler(FranquiciaNotFoundException.class)
    public Mono<ResponseEntity<ResponseError>> handleFranquiciaNotFoundException(FranquiciaNotFoundException exception) {
        return Mono.just(
                ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(this.createResponseError(exception.getMessage())));
    }

    /**
     * Método para crear un objeto de repuesta de error
     * @param message mensaje de error
     * @return objeto de error
     */
    private ResponseError createResponseError(String message) {
        return ResponseError.builder().message(message).build();
    }
}

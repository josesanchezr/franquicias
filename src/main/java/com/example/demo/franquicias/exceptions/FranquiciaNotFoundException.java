package com.example.demo.franquicias.exceptions;

/**
 * Excepción lanzada cuando no se encuentra una franquicia
 */
public class FranquiciaNotFoundException extends RuntimeException {

    /**
     * Constructor de la clase {@link FranquiciaNotFoundException}
     * @param message mensaje de error
     */
    public FranquiciaNotFoundException(String message) {
        super(message);
    }
}

package com.example.demo.franquicias.exceptions;

/**
 * Excepción lanzada cuando no se encuentra una sucursal
 */
public class SucursalNotFoundException extends RuntimeException {

    /**
     * Constructor de la clase {@link SucursalNotFoundException}
     * @param message mensaje de error
     */
    public SucursalNotFoundException(String message) {
        super(message);
    }
}
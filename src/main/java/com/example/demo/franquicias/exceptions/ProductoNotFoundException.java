package com.example.demo.franquicias.exceptions;

/**
 * Excepción lanzada cuando no se encuentra un producto
 */
public class ProductoNotFoundException extends RuntimeException {

    /**
     * Constructor de la clase {@link ProductoNotFoundException}
     * @param message mensaje de error
     */
    public ProductoNotFoundException(String message) {
        super(message);
    }
}

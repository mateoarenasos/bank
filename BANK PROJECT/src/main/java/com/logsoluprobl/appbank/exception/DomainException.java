package com.logsoluprobl.appbank.exception;

public class DomainException extends RuntimeException {

    /**
     * Constructor con solo mensaje.
     * @param message Detalle del error ocurrido.
     */
    public DomainException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa original.
     * @param message Detalle del error ocurrido.
     * @param cause Causa original del error.
     */
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.ferreyros.servtech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando el horómetro ingresado es menor al acumulado histórico.
 * Mapeada al código de estado HTTP 400 Bad Request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HorometroInvalidoException extends RuntimeException {
    public HorometroInvalidoException(String mensaje) {
        super(mensaje);
    }
}

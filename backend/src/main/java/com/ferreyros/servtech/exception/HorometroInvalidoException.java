package com.ferreyros.servtech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/* Excepción lanzada cuando el horómetro ingresado es menor al acumulado histórico. Error HTTP 400*/
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HorometroInvalidoException extends RuntimeException {
    public HorometroInvalidoException(String mensaje) {
        super(mensaje);
    }
}

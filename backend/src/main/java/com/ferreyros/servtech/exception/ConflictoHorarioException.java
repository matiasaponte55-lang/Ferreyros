package com.ferreyros.servtech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/* Excepción lanzada cuando ocurre una colisión horaria de cuadrillas, error HTTP 409 Conflict.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictoHorarioException extends RuntimeException {

    private String codigoOtExistente;

    public ConflictoHorarioException(String mensaje) {
        super(mensaje);
    }

    public ConflictoHorarioException(String mensaje, String codigoOtExistente) {
        super(mensaje);
        this.codigoOtExistente = codigoOtExistente;
    }

    public String getCodigoOtExistente() {
        return codigoOtExistente;
    }
}

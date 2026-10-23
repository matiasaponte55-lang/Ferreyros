package com.ferreyros.servtech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para estandarizar las respuestas JSON de error.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConflictoHorarioException.class)
    public ResponseEntity<Map<String, Object>> manejarConflictoHorario(ConflictoHorarioException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("status", HttpStatus.CONFLICT.value());
        cuerpo.put("error", "CONFLICT");
        cuerpo.put("mensaje", ex.getMessage());
        cuerpo.put("codigoOtConflictiva", ex.getCodigoOtExistente());
        cuerpo.put("timestamp", OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(cuerpo);
    }

    @ExceptionHandler(HorometroInvalidoException.class)
    public ResponseEntity<Map<String, Object>> manejarHorometroInvalido(HorometroInvalidoException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("status", HttpStatus.BAD_REQUEST.value());
        cuerpo.put("error", "BAD_REQUEST");
        cuerpo.put("mensaje", ex.getMessage());
        cuerpo.put("timestamp", OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpo);
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("status", HttpStatus.NOT_FOUND.value());
        cuerpo.put("error", "NOT_FOUND");
        cuerpo.put("mensaje", ex.getMessage());
        cuerpo.put("timestamp", OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cuerpo);
    }

    @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> manejarResponseStatus(org.springframework.web.server.ResponseStatusException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("status", ex.getStatusCode().value());
        cuerpo.put("error", ex.getStatusCode().toString());
        cuerpo.put("mensaje", ex.getReason());
        cuerpo.put("message", ex.getReason());
        cuerpo.put("timestamp", OffsetDateTime.now());
        return ResponseEntity.status(ex.getStatusCode()).body(cuerpo);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarExcepcionGeneral(Exception ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        cuerpo.put("error", "INTERNAL_SERVER_ERROR");
        cuerpo.put("mensaje", ex.getMessage());
        cuerpo.put("timestamp", OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cuerpo);
    }
}

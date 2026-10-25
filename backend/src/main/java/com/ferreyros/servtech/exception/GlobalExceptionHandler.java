package com.ferreyros.servtech.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para estandarizar las respuestas JSON de error
 * y garantizar validaciones exhaustivas con mensajes precisos en español.
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
        cuerpo.put("error", "HOROMETRO_INVALIDO");
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("status", HttpStatus.BAD_REQUEST.value());
        cuerpo.put("error", "PARAMETRO_INVALIDO");
        cuerpo.put("mensaje", ex.getMessage());
        cuerpo.put("timestamp", OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpo);
    }

    /**
     * Captura cuando se envían letras en un campo que requiere números puros en el cuerpo JSON.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> manejarMensajeNoLegible(HttpMessageNotReadableException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("status", HttpStatus.BAD_REQUEST.value());
        cuerpo.put("error", "TIPO_DATO_INCOMPATIBLE");

        String detalle = "Formato de datos incompatible: Verifique que los campos numéricos contengan únicamente números y no texto.";
        Throwable causa = ex.getCause();
        if (causa instanceof InvalidFormatException ife) {
            String campo = ife.getPath().stream()
                    .map(ref -> ref.getFieldName())
                    .filter(Objects::nonNull)
                    .findFirst().orElse("desconocido");
            String tipoEsperado = ife.getTargetType() != null ? ife.getTargetType().getSimpleName() : "numérico";
            detalle = "El campo '" + campo + "' requiere un valor numérico válido (tipo " + tipoEsperado + "), pero se recibió texto o un valor incompatible: '" + ife.getValue() + "'.";
        }

        cuerpo.put("mensaje", detalle);
        cuerpo.put("timestamp", OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpo);
    }

    /**
     * Captura cuando se envían letras en un parámetro numérico de la URL (ej: /api/v1/ordenes/abc).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> manejarTipoIncompatible(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("status", HttpStatus.BAD_REQUEST.value());
        cuerpo.put("error", "PARAMETRO_URL_INVALIDO");
        cuerpo.put("mensaje", "El parámetro '" + ex.getName() + "' debe ser un número entero válido. No se permiten letras ni símbolos. Valor recibido: '" + ex.getValue() + "'.");
        cuerpo.put("timestamp", OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpo);
    }

    /**
     * Captura violaciones de validaciones @Valid en DTOs (@NotNull, @NotBlank, @Positive, etc.).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacionBean(MethodArgumentNotValidException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("status", HttpStatus.BAD_REQUEST.value());
        cuerpo.put("error", "VALIDACION_CAMPOS");

        List<String> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> "Campo '" + err.getField() + "': " + err.getDefaultMessage())
                .collect(Collectors.toList());

        cuerpo.put("mensaje", String.join(" | ", errores));
        cuerpo.put("errores", errores);
        cuerpo.put("timestamp", OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpo);
    }

    /**
     * Captura violaciones de restricciones relacionales (CHECK constraints, UNIQUE, FOREIGN KEY) en PostgreSQL.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> manejarViolacionIntegridad(DataIntegrityViolationException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("status", HttpStatus.BAD_REQUEST.value());
        cuerpo.put("error", "RESTRICCION_INTEGRIDAD_BD");

        String msg = "Los datos enviados violan las restricciones de integridad y compatibilidad de la base de datos.";
        String causa = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : "";
        if (causa.contains("violates check constraint")) {
            msg = "Los valores seleccionados o ingresados no cumplen con las reglas permitidas por la especificación del sistema.";
        } else if (causa.contains("violates unique constraint")) {
            msg = "Ya existe un registro con ese mismo código único o identificador en el sistema.";
        } else if (causa.contains("violates foreign key constraint")) {
            msg = "El registro relacionado no existe en la base de datos.";
        }

        cuerpo.put("mensaje", msg);
        cuerpo.put("timestamp", OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpo);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> manejarResponseStatus(ResponseStatusException ex) {
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

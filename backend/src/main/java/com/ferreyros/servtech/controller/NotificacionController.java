package com.ferreyros.servtech.controller;

import com.ferreyros.servtech.dto.NotificacionReporteResponseDto;
import com.ferreyros.servtech.dto.SolicitudReporteDto;
import com.ferreyros.servtech.service.NotificacionEmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para el despacho de alertas y notificaciones por correo electrónico
 * al emitirse reportes de gestión operativa (PDF, CSV, PPTX).
 */
@RestController
@RequestMapping("/api/v1/notificaciones")
@CrossOrigin(originPatterns = "*")
public class NotificacionController {

    @Autowired
    private NotificacionEmailService notificacionEmailService;

    @PostMapping("/reporte")
    public ResponseEntity<NotificacionReporteResponseDto> notificarGeneracionReporte(
            @Valid @RequestBody SolicitudReporteDto dto
    ) {
        NotificacionReporteResponseDto respuesta = notificacionEmailService.procesarNotificacion(dto);
        return ResponseEntity.ok(respuesta);
    }
}

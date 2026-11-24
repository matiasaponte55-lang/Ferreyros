package com.ferreyros.servtech.controller;

import com.ferreyros.servtech.dto.NotificacionReporteResponseDto;
import com.ferreyros.servtech.dto.SolicitudReporteDto;
import com.ferreyros.servtech.service.NotificacionEmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Controlador REST para el despacho de alertas y notificaciones por correo electrónico
 * al emitirse reportes de gestión operativa (PDF, CSV, PPTX).
 * Protegido por RBAC: Solo cuentas ejecutivas (ADMIN, PLANIFICADOR, SUPERVISOR) pueden emitir reportes.
 */
@RestController
@RequestMapping("/api/v1/notificaciones")
@CrossOrigin(originPatterns = "*")
public class NotificacionController {

    private static final Set<String> ROLES_AUTORIZADOS_REPORTES = Set.of(
            "ROLE_ADMIN",
            "ROLE_PLANIFICADOR",
            "ROLE_SUPERVISOR"
    );

    @Autowired
    private NotificacionEmailService notificacionEmailService;

    @PostMapping("/reporte")
    public ResponseEntity<NotificacionReporteResponseDto> notificarGeneracionReporte(
            @Valid @RequestBody SolicitudReporteDto dto
    ) {
        String rol = dto.getRolSolicitante();
        if (rol == null || !ROLES_AUTORIZADOS_REPORTES.contains(rol.trim().toUpperCase())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new NotificacionReporteResponseDto(
                            false,
                            "Acceso denegado (RBAC): El rol '" + (rol != null ? rol : "NO_DEFINIDO") +
                            "' no posee privilegios para emitir reportes ejecutivos ni despachar alertas. Se requiere rol ADMIN, PLANIFICADOR o SUPERVISOR.",
                            dto.getTipoReporte(),
                            null,
                            "ACCESO_DENEGADO_RBAC"
                    )
            );
        }

        NotificacionReporteResponseDto respuesta = notificacionEmailService.procesarNotificacion(dto);
        return ResponseEntity.ok(respuesta);
    }
}

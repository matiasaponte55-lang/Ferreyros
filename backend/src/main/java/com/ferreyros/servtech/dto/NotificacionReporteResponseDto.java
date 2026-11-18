package com.ferreyros.servtech.dto;

import java.time.OffsetDateTime;

/**
 * DTO de respuesta para la confirmación de la notificación de reporte.
 */
public class NotificacionReporteResponseDto {

    private boolean exito;
    private String mensaje;
    private String tipoReporte;
    private String correoDestino;
    private String modo;
    private OffsetDateTime timestamp;

    public NotificacionReporteResponseDto() {}

    public NotificacionReporteResponseDto(boolean exito, String mensaje, String tipoReporte, String correoDestino, String modo) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.tipoReporte = tipoReporte;
        this.correoDestino = correoDestino;
        this.modo = modo;
        this.timestamp = OffsetDateTime.now();
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public String getCorreoDestino() {
        return correoDestino;
    }

    public void setCorreoDestino(String correoDestino) {
        this.correoDestino = correoDestino;
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

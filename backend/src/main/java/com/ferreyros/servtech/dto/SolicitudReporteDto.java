package com.ferreyros.servtech.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para la solicitud de notificación por correo electrónico
 * al generarse un reporte (PDF, CSV o PPTX).
 */
public class SolicitudReporteDto {

    @NotBlank(message = "El tipo de reporte es obligatorio (PDF, CSV o PPTX)")
    private String tipoReporte;

    private String nombreReporte;
    private String solicitadoPor;
    private String rolSolicitante;
    private String correoSolicitante;
    private String correoDestino;
    private String sede;
    private String detalles;

    public SolicitudReporteDto() {}

    public SolicitudReporteDto(String tipoReporte, String nombreReporte, String solicitadoPor, String rolSolicitante, String correoDestino, String sede, String detalles) {
        this.tipoReporte = tipoReporte;
        this.nombreReporte = nombreReporte;
        this.solicitadoPor = solicitadoPor;
        this.rolSolicitante = rolSolicitante;
        this.correoDestino = correoDestino;
        this.sede = sede;
        this.detalles = detalles;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    public String getSolicitadoPor() {
        return solicitadoPor;
    }

    public void setSolicitadoPor(String solicitadoPor) {
        this.solicitadoPor = solicitadoPor;
    }

    public String getRolSolicitante() {
        return rolSolicitante;
    }

    public void setRolSolicitante(String rolSolicitante) {
        this.rolSolicitante = rolSolicitante;
    }

    public String getCorreoSolicitante() {
        return correoSolicitante;
    }

    public void setCorreoSolicitante(String correoSolicitante) {
        this.correoSolicitante = correoSolicitante;
    }

    public String getCorreoDestino() {
        return correoDestino;
    }

    public void setCorreoDestino(String correoDestino) {
        this.correoDestino = correoDestino;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }
}

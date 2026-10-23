package com.ferreyros.servtech.dto;

public class ActualizarEstadoOtRequest {
    private String nuevoEstado;
    private String motivoRetrabajo;
    private String cambiadoPor;

    public ActualizarEstadoOtRequest() {}

    public String getNuevoEstado() { return nuevoEstado; }
    public void setNuevoEstado(String nuevoEstado) { this.nuevoEstado = nuevoEstado; }

    public String getMotivoRetrabajo() { return motivoRetrabajo; }
    public void setMotivoRetrabajo(String motivoRetrabajo) { this.motivoRetrabajo = motivoRetrabajo; }

    public String getCambiadoPor() { return cambiadoPor; }
    public void setCambiadoPor(String cambiadoPor) { this.cambiadoPor = cambiadoPor; }
}

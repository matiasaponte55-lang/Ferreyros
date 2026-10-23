package com.ferreyros.servtech.dto;

import java.math.BigDecimal;

public class NuevaOrdenRequest {
    private String numeroSeriePin;
    private Integer idMaquinaria;
    private BigDecimal horometroRegistro;
    private String tipoMantenimiento;
    private Integer idSede;
    private String prioridad;
    private String fallaReportada;
    private String creadoPorUsername;

    public NuevaOrdenRequest() {}

    public String getNumeroSeriePin() { return numeroSeriePin; }
    public void setNumeroSeriePin(String numeroSeriePin) { this.numeroSeriePin = numeroSeriePin; }

    public Integer getIdMaquinaria() { return idMaquinaria; }
    public void setIdMaquinaria(Integer idMaquinaria) { this.idMaquinaria = idMaquinaria; }

    public BigDecimal getHorometroRegistro() { return horometroRegistro; }
    public void setHorometroRegistro(BigDecimal horometroRegistro) { this.horometroRegistro = horometroRegistro; }

    public String getTipoMantenimiento() { return tipoMantenimiento; }
    public void setTipoMantenimiento(String tipoMantenimiento) { this.tipoMantenimiento = tipoMantenimiento; }

    public Integer getIdSede() { return idSede; }
    public void setIdSede(Integer idSede) { this.idSede = idSede; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getFallaReportada() { return fallaReportada; }
    public void setFallaReportada(String fallaReportada) { this.fallaReportada = fallaReportada; }

    public String getCreadoPorUsername() { return creadoPorUsername; }
    public void setCreadoPorUsername(String creadoPorUsername) { this.creadoPorUsername = creadoPorUsername; }
}

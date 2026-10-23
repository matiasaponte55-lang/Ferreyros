package com.ferreyros.servtech.dto;

import java.math.BigDecimal;

public class InspeccionRequest {
    private Integer idOrden;
    private Integer idTecnicoResponsable;
    private BigDecimal horometroSalida;
    private Boolean atsAprobado = false;
    private Boolean lotoBloqueado = false;
    private Boolean eppCompleto = false;
    private Boolean areaDemarcada = false;
    private String evaluacionMotor;
    private String evaluacionHidraulico;
    private String evaluacionTransmision;
    private String evaluacionTrenRodaje;
    private String observaciones;
    private String firmanteNombre;
    private String firmanteDni;
    private String firmanteCargo;
    private String firmaBase64;

    public InspeccionRequest() {}

    public Integer getIdOrden() { return idOrden; }
    public void setIdOrden(Integer idOrden) { this.idOrden = idOrden; }

    public Integer getIdTecnicoResponsable() { return idTecnicoResponsable; }
    public void setIdTecnicoResponsable(Integer idTecnicoResponsable) { this.idTecnicoResponsable = idTecnicoResponsable; }

    public BigDecimal getHorometroSalida() { return horometroSalida; }
    public void setHorometroSalida(BigDecimal horometroSalida) { this.horometroSalida = horometroSalida; }

    public Boolean getAtsAprobado() { return atsAprobado; }
    public void setAtsAprobado(Boolean atsAprobado) { this.atsAprobado = atsAprobado; }

    public Boolean getLotoBloqueado() { return lotoBloqueado; }
    public void setLotoBloqueado(Boolean lotoBloqueado) { this.lotoBloqueado = lotoBloqueado; }

    public Boolean getEppCompleto() { return eppCompleto; }
    public void setEppCompleto(Boolean eppCompleto) { this.eppCompleto = eppCompleto; }

    public Boolean getAreaDemarcada() { return areaDemarcada; }
    public void setAreaDemarcada(Boolean areaDemarcada) { this.areaDemarcada = areaDemarcada; }

    public String getEvaluacionMotor() { return evaluacionMotor; }
    public void setEvaluacionMotor(String evaluacionMotor) { this.evaluacionMotor = evaluacionMotor; }

    public String getEvaluacionHidraulico() { return evaluacionHidraulico; }
    public void setEvaluacionHidraulico(String evaluacionHidraulico) { this.evaluacionHidraulico = evaluacionHidraulico; }

    public String getEvaluacionTransmision() { return evaluacionTransmision; }
    public void setEvaluacionTransmision(String evaluacionTransmision) { this.evaluacionTransmision = evaluacionTransmision; }

    public String getEvaluacionTrenRodaje() { return evaluacionTrenRodaje; }
    public void setEvaluacionTrenRodaje(String evaluacionTrenRodaje) { this.evaluacionTrenRodaje = evaluacionTrenRodaje; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getFirmanteNombre() { return firmanteNombre; }
    public void setFirmanteNombre(String firmanteNombre) { this.firmanteNombre = firmanteNombre; }

    public String getFirmanteDni() { return firmanteDni; }
    public void setFirmanteDni(String firmanteDni) { this.firmanteDni = firmanteDni; }

    public String getFirmanteCargo() { return firmanteCargo; }
    public void setFirmanteCargo(String firmanteCargo) { this.firmanteCargo = firmanteCargo; }

    public String getFirmaBase64() { return firmaBase64; }
    public void setFirmaBase64(String firmaBase64) { this.firmaBase64 = firmaBase64; }
}

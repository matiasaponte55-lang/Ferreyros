package com.ferreyros.servtech.dto;

public class AsignacionRequest {
    private Integer idOrden;
    private Integer idTecnico;
    private String horaInicio;
    private String horaFin;
    private String fechaHoraInicio;
    private String fechaHoraFin;
    private Boolean esLider = false;

    public AsignacionRequest() {}

    public Integer getIdOrden() { return idOrden; }
    public void setIdOrden(Integer idOrden) { this.idOrden = idOrden; }

    public Integer getIdTecnico() { return idTecnico; }
    public void setIdTecnico(Integer idTecnico) { this.idTecnico = idTecnico; }

    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }

    public String getFechaHoraInicio() { return fechaHoraInicio; }
    public void setFechaHoraInicio(String fechaHoraInicio) { this.fechaHoraInicio = fechaHoraInicio; }

    public String getFechaHoraFin() { return fechaHoraFin; }
    public void setFechaHoraFin(String fechaHoraFin) { this.fechaHoraFin = fechaHoraFin; }

    public Boolean getEsLider() { return esLider; }
    public void setEsLider(Boolean esLider) { this.esLider = esLider; }
}

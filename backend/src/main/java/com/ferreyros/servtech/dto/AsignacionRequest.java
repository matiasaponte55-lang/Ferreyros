package com.ferreyros.servtech.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AsignacionRequest {

    @NotNull(message = "El ID de la orden de trabajo es obligatorio.")
    @Positive(message = "El ID de la orden debe ser un número positivo.")
    private Integer idOrden;

    @NotNull(message = "El ID del técnico responsable es obligatorio.")
    @Positive(message = "El ID del técnico debe ser un número positivo.")
    private Integer idTecnico;

    private String horaInicio;
    private String horaFin;
    private String fechaHoraInicio;
    private String fechaHoraFin;
    private Boolean esLider;

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

package com.ferreyros.servtech.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class NuevaOrdenRequest {

    @NotBlank(message = "El número de serie PIN de la maquinaria es obligatorio.")
    private String numeroSeriePin;

    @NotNull(message = "El ID de la maquinaria es obligatorio.")
    @Positive(message = "El ID de maquinaria debe ser un número entero positivo.")
    private Integer idMaquinaria;

    @NotNull(message = "El horómetro de entrada es obligatorio.")
    @DecimalMin(value = "0.1", message = "El horómetro debe ser un número positivo mayor a 0.")
    @Digits(integer = 8, fraction = 2, message = "El horómetro no puede tener más de 8 enteros y 2 decimales.")
    private BigDecimal horometroRegistro;

    @NotBlank(message = "El tipo de mantenimiento estandarizado es obligatorio.")
    private String tipoMantenimiento;

    private Integer idSede;

    @NotBlank(message = "La prioridad operativa del servicio es obligatoria.")
    private String prioridad;

    @NotBlank(message = "La descripción de la falla o requerimiento es obligatoria.")
    @Size(min = 5, message = "La descripción de la falla debe tener un mínimo de 5 caracteres.")
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

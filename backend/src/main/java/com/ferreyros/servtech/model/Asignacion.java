package com.ferreyros.servtech.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

/**
 * Entidad Asignacion:
 * Despacho temporal de técnicos a órdenes de trabajo con protección
 * contra solapamientos (Regla Anti-Solapamiento GiST).
 */
@Entity
@Table(name = "asignaciones")
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion")
    private Integer idAsignacion;

    @ManyToOne
    @JoinColumn(name = "id_orden", referencedColumnName = "id_orden", nullable = false)
    private OrdenServicio orden;

    @ManyToOne
    @JoinColumn(name = "id_tecnico", referencedColumnName = "id_tecnico", nullable = false)
    private Tecnico tecnico;

    @Column(name = "fecha_inicio", nullable = false)
    private OffsetDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private OffsetDateTime fechaFin;

    @Column(name = "es_lider", nullable = false)
    private Boolean esLider = false;

    @Column(name = "estado_asignacion", nullable = false, length = 20)
    private String estadoAsignacion = "PROGRAMADA"; // PROGRAMADA, EN_EJECUCION, COMPLETADA, CANCELADA

    @Column(name = "creado_en")
    private OffsetDateTime creadoEn = OffsetDateTime.now();

    public Asignacion() {}

    public Asignacion(OrdenServicio orden, Tecnico tecnico, OffsetDateTime fechaInicio, OffsetDateTime fechaFin, Boolean esLider) {
        this.orden = orden;
        this.tecnico = tecnico;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.esLider = esLider;
        this.estadoAsignacion = "PROGRAMADA";
        this.creadoEn = OffsetDateTime.now();
    }

    public Integer getIdAsignacion() { return idAsignacion; }
    public void setIdAsignacion(Integer idAsignacion) { this.idAsignacion = idAsignacion; }

    public OrdenServicio getOrden() { return orden; }
    public void setOrden(OrdenServicio orden) { this.orden = orden; }

    public Tecnico getTecnico() { return tecnico; }
    public void setTecnico(Tecnico tecnico) { this.tecnico = tecnico; }

    public OffsetDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(OffsetDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public OffsetDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(OffsetDateTime fechaFin) { this.fechaFin = fechaFin; }

    public Boolean getEsLider() { return esLider; }
    public void setEsLider(Boolean esLider) { this.esLider = esLider; }

    public String getEstadoAsignacion() { return estadoAsignacion; }
    public void setEstadoAsignacion(String estadoAsignacion) { this.estadoAsignacion = estadoAsignacion; }

    public OffsetDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(OffsetDateTime creadoEn) { this.creadoEn = creadoEn; }
}

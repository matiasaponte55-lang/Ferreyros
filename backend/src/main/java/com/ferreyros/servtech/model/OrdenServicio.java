package com.ferreyros.servtech.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Entidad central transaccional que gestiona el ciclo de vida
 * de los servicios preventivos y correctivos de soporte técnico Caterpillar.
 */
@Entity
@Table(name = "ordenes_servicio")
public class OrdenServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden")
    private Integer idOrden;

    @Column(name = "codigo_ot", nullable = false, unique = true, length = 50)
    private String codigoOt;

    @ManyToOne
    @JoinColumn(name = "id_maquinaria", referencedColumnName = "id_maquinaria", nullable = false)
    private Maquinaria maquinaria;

    @ManyToOne
    @JoinColumn(name = "id_sede", referencedColumnName = "id_sede", nullable = false)
    private Sede sede;

    @Column(name = "tipo_mantenimiento", nullable = false, length = 30)
    private String tipoMantenimiento; // PM1_250H, PM2_500H, PM3_1000H, PM4_2000H, CORRECTIVO, DIAGNOSTICO_FALLA

    @Column(name = "estado", nullable = false, length = 30)
    private String estado = "SOLICITADA"; // SOLICITADA, ASIGNADA, EN_DIAGNOSTICO, EN_REPARACION, CONTROL_CALIDAD, CERRADA, CANCELADA

    @Column(name = "prioridad", nullable = false, length = 30)
    private String prioridad = "MEDIA"; // BAJA, MEDIA, ALTA, CRITICA_DOWN_MACHINE

    @Column(name = "falla_reportada", columnDefinition = "TEXT")
    private String fallaReportada;

    @Column(name = "descripcion_falla", columnDefinition = "TEXT")
    private String descripcionFalla;

    @Column(name = "horometro_registro", nullable = false, precision = 10, scale = 2)
    private BigDecimal horometroRegistro;

    @Column(name = "fecha_programada_inicio")
    private OffsetDateTime fechaProgramadaInicio;

    @Column(name = "fecha_programada_fin")
    private OffsetDateTime fechaProgramadaFin;

    @ManyToOne
    @JoinColumn(name = "creado_por", referencedColumnName = "id_usuario")
    private Usuario creadoPor;

    @Column(name = "creado_en")
    private OffsetDateTime creadoEn = OffsetDateTime.now();

    @Column(name = "actualizado_en")
    private OffsetDateTime actualizadoEn = OffsetDateTime.now();

    @Column(name = "cerrado_en")
    private OffsetDateTime cerradoEn;

    public OrdenServicio() {}

    public Integer getIdOrden() { return idOrden; }
    public void setIdOrden(Integer idOrden) { this.idOrden = idOrden; }

    public String getCodigoOt() { return codigoOt; }
    public void setCodigoOt(String codigoOt) { this.codigoOt = codigoOt; }

    public Maquinaria getMaquinaria() { return maquinaria; }
    public void setMaquinaria(Maquinaria maquinaria) { this.maquinaria = maquinaria; }

    public Sede getSede() { return sede; }
    public void setSede(Sede sede) { this.sede = sede; }

    public String getTipoMantenimiento() { return tipoMantenimiento; }
    public void setTipoMantenimiento(String tipoMantenimiento) { this.tipoMantenimiento = tipoMantenimiento; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getFallaReportada() { return fallaReportada != null ? fallaReportada : descripcionFalla; }
    public void setFallaReportada(String fallaReportada) {
        this.fallaReportada = fallaReportada;
        this.descripcionFalla = fallaReportada;
    }

    public String getDescripcionFalla() { return descripcionFalla != null ? descripcionFalla : fallaReportada; }
    public void setDescripcionFalla(String descripcionFalla) {
        this.descripcionFalla = descripcionFalla;
        this.fallaReportada = descripcionFalla;
    }

    public BigDecimal getHorometroRegistro() { return horometroRegistro; }
    public void setHorometroRegistro(BigDecimal horometroRegistro) { this.horometroRegistro = horometroRegistro; }

    public OffsetDateTime getFechaProgramadaInicio() { return fechaProgramadaInicio; }
    public void setFechaProgramadaInicio(OffsetDateTime fechaProgramadaInicio) { this.fechaProgramadaInicio = fechaProgramadaInicio; }

    public OffsetDateTime getFechaProgramadaFin() { return fechaProgramadaFin; }
    public void setFechaProgramadaFin(OffsetDateTime fechaProgramadaFin) { this.fechaProgramadaFin = fechaProgramadaFin; }

    public Usuario getCreadoPor() { return creadoPor; }
    public void setCreadoPor(Usuario creadoPor) { this.creadoPor = creadoPor; }

    public OffsetDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(OffsetDateTime creadoEn) { this.creadoEn = creadoEn; }

    public OffsetDateTime getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(OffsetDateTime actualizadoEn) { this.actualizadoEn = actualizadoEn; }

    public OffsetDateTime getCerradoEn() { return cerradoEn; }
    public void setCerradoEn(OffsetDateTime cerradoEn) { this.cerradoEn = cerradoEn; }
}

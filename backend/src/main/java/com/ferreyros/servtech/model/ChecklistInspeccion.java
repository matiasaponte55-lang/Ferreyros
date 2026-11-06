package com.ferreyros.servtech.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

/* Entidad ChecklistInspeccion: Análisis de Trabajo Seguro (ATS) */
@Entity
@Table(name = "checklists_inspeccion")
public class ChecklistInspeccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inspeccion")
    private Integer idInspeccion;

    @OneToOne
    @JoinColumn(name = "id_orden", referencedColumnName = "id_orden", nullable = false, unique = true)
    private OrdenServicio orden;

    @ManyToOne
    @JoinColumn(name = "id_tecnico_responsable", referencedColumnName = "id_tecnico", nullable = false)
    private Tecnico tecnicoResponsable;

    @Column(name = "horometro_salida", precision = 10, scale = 2)
    private BigDecimal horometroSalida;

    @Column(name = "ats_aprobado", nullable = false)
    private Boolean atsAprobado = false;

    @Column(name = "loto_bloqueado", nullable = false)
    private Boolean lotoBloqueado = false;

    @Column(name = "epp_completo", nullable = false)
    private Boolean eppCompleto = false;

    @Column(name = "area_demarcada", nullable = false)
    private Boolean areaDemarcada = false;

    @Column(name = "evaluacion_motor", length = 20)
    private String evaluacionMotor = "CONFORME"; // CONFORME, OBSERVADO, CRITICO, NO_APLICA

    @Column(name = "evaluacion_hidraulico", length = 20)
    private String evaluacionHidraulico = "CONFORME";

    @Column(name = "evaluacion_transmision", length = 20)
    private String evaluacionTransmision = "CONFORME";

    @Column(name = "evaluacion_tren_rodaje", length = 20)
    private String evaluacionTrenRodaje = "CONFORME";

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "firmante_nombre", length = 150)
    private String firmanteNombre;

    @Column(name = "firmante_dni", length = 15)
    private String firmanteDni;

    @Column(name = "firmante_cargo", length = 100)
    private String firmanteCargo;

    @Column(name = "firma_base64", columnDefinition = "TEXT")
    private String firmaBase64;

    @Column(name = "hash_criptografico", length = 64)
    private String hashCriptografico;

    @Column(name = "fecha_cierre")
    private OffsetDateTime fechaCierre;

    public ChecklistInspeccion() {}

    public Integer getIdInspeccion() { return idInspeccion; }
    public void setIdInspeccion(Integer idInspeccion) { this.idInspeccion = idInspeccion; }

    public OrdenServicio getOrden() { return orden; }
    public void setOrden(OrdenServicio orden) { this.orden = orden; }

    public Tecnico getTecnicoResponsable() { return tecnicoResponsable; }
    public void setTecnicoResponsable(Tecnico tecnicoResponsable) { this.tecnicoResponsable = tecnicoResponsable; }

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

    public String getHashCriptografico() { return hashCriptografico; }
    public void setHashCriptografico(String hashCriptografico) { this.hashCriptografico = hashCriptografico; }

    public OffsetDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(OffsetDateTime fechaCierre) { this.fechaCierre = fechaCierre; }
}

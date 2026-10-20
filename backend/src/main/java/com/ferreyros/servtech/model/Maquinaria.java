package com.ferreyros.servtech.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Entidad Maquinaria:
 * Catálogo maestro de equipos pesados Caterpillar (797F, 6060 FS, D11T, 994K)
 * asignados a faenas mineras bajo contratos MARC.
 */
@Entity
@Table(name = "maquinarias")
public class Maquinaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_maquinaria")
    private Integer idMaquinaria;

    @Column(name = "numero_serie_pin", nullable = false, unique = true, length = 50)
    private String numeroSeriePin;

    @Column(name = "codigo_flota", nullable = false, length = 30)
    private String codigoFlota; // CAM-041, PAL-002, etc.

    @Column(name = "modelo_cat", length = 50)
    private String modeloCat;

    @Column(name = "familia", length = 50)
    private String familia; // 'CAMION_MINERO', 'PALA_HIDRAULICA', 'TRACTOR_ORUGA', 'EXCAVADORA', 'CARGADOR_FRONTAL', 'PERFORADORA'

    @ManyToOne
    @JoinColumn(name = "id_modelo", referencedColumnName = "id_modelo")
    private ModeloCatMaestro modelo;

    @ManyToOne
    @JoinColumn(name = "id_sede_actual", referencedColumnName = "id_sede", nullable = false)
    private Sede sedeActual;

    @ManyToOne
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    private Cliente cliente;

    @Column(name = "horometro_acumulado", nullable = false, precision = 10, scale = 2)
    private BigDecimal horometroAcumulado = BigDecimal.ZERO;

    @Column(name = "estado_operativo", nullable = false, length = 30)
    private String estadoOperativo = "OPERATIVO"; // 'OPERATIVO', 'EN_MANTENIMIENTO', 'PARADA_CRITICA_DOWN', 'STAND_BY'

    @Column(name = "contrato_marc", length = 50)
    private String contratoMarc = "MARC Oro (SLA 92%)";

    @Column(name = "sla_garantizado", precision = 5, scale = 2)
    private BigDecimal slaGarantizado = new BigDecimal("92.00");

    @Column(name = "telemetria_activa")
    private Boolean telemetriaActiva = true;

    @Column(name = "creado_en")
    private OffsetDateTime creadoEn = OffsetDateTime.now();

    @Column(name = "actualizado_en")
    private OffsetDateTime actualizadoEn = OffsetDateTime.now();

    public Maquinaria() {}

    public Integer getIdMaquinaria() { return idMaquinaria; }
    public void setIdMaquinaria(Integer idMaquinaria) { this.idMaquinaria = idMaquinaria; }

    public String getNumeroSeriePin() { return numeroSeriePin; }
    public void setNumeroSeriePin(String numeroSeriePin) { this.numeroSeriePin = numeroSeriePin; }

    public String getCodigoFlota() { return codigoFlota; }
    public void setCodigoFlota(String codigoFlota) { this.codigoFlota = codigoFlota; }

    public String getModeloCat() { return modeloCat; }
    public void setModeloCat(String modeloCat) { this.modeloCat = modeloCat; }

    public String getFamilia() { return familia; }
    public void setFamilia(String familia) { this.familia = familia; }

    public ModeloCatMaestro getModelo() { return modelo; }
    public void setModelo(ModeloCatMaestro modelo) {
        this.modelo = modelo;
        if (modelo != null) {
            this.modeloCat = modelo.getNombreComercial();
            this.familia = modelo.getFamilia();
        }
    }

    public Sede getSedeActual() { return sedeActual; }
    public void setSedeActual(Sede sedeActual) { this.sedeActual = sedeActual; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public BigDecimal getHorometroAcumulado() { return horometroAcumulado; }
    public void setHorometroAcumulado(BigDecimal horometroAcumulado) { this.horometroAcumulado = horometroAcumulado; }

    public String getEstadoOperativo() { return estadoOperativo; }
    public void setEstadoOperativo(String estadoOperativo) { this.estadoOperativo = estadoOperativo; }

    public String getContratoMarc() { return contratoMarc; }
    public void setContratoMarc(String contratoMarc) { this.contratoMarc = contratoMarc; }

    public BigDecimal getSlaGarantizado() { return slaGarantizado; }
    public void setSlaGarantizado(BigDecimal slaGarantizado) { this.slaGarantizado = slaGarantizado; }

    public Boolean getTelemetriaActiva() { return telemetriaActiva; }
    public void setTelemetriaActiva(Boolean telemetriaActiva) { this.telemetriaActiva = telemetriaActiva; }

    public OffsetDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(OffsetDateTime creadoEn) { this.creadoEn = creadoEn; }

    public OffsetDateTime getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(OffsetDateTime actualizadoEn) { this.actualizadoEn = actualizadoEn; }
}

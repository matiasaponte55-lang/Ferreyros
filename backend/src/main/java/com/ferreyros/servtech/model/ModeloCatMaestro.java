package com.ferreyros.servtech.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entidad ModeloCatMaestro:
 * Catálogo técnico de modelos Caterpillar (ej: 797F, 6060 FS, D11T, 994K).
 */
@Entity
@Table(name = "modelo_cat_maestro")
public class ModeloCatMaestro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo")
    private Integer idModelo;

    @Column(name = "codigo_modelo", nullable = false, unique = true, length = 30)
    private String codigoModelo;

    @Column(name = "familia", nullable = false, length = 50)
    private String familia; // CAMION_MINERO, PALA_HIDRAULICA, CARGADOR_RUEDAS, TRACTOR_ORUGAS, MOTONIVELADORA

    @Column(name = "nombre_comercial", nullable = false, length = 100)
    private String nombreComercial;

    @Column(name = "potencia_hp", nullable = false)
    private Integer potenciaHp;

    @Column(name = "capacidad_carga_ton", nullable = false, precision = 8, scale = 2)
    private BigDecimal capacidadCargaTon;

    @Column(name = "intervalo_pm_horas", nullable = false)
    private Integer intervaloPmHoras = 250;

    public ModeloCatMaestro() {}

    public ModeloCatMaestro(String codigoModelo, String familia, String nombreComercial, Integer potenciaHp, BigDecimal capacidadCargaTon, Integer intervaloPmHoras) {
        this.codigoModelo = codigoModelo;
        this.familia = familia;
        this.nombreComercial = nombreComercial;
        this.potenciaHp = potenciaHp;
        this.capacidadCargaTon = capacidadCargaTon;
        this.intervaloPmHoras = intervaloPmHoras;
    }

    public Integer getIdModelo() { return idModelo; }
    public void setIdModelo(Integer idModelo) { this.idModelo = idModelo; }

    public String getCodigoModelo() { return codigoModelo; }
    public void setCodigoModelo(String codigoModelo) { this.codigoModelo = codigoModelo; }

    public String getFamilia() { return familia; }
    public void setFamilia(String familia) { this.familia = familia; }

    public String getNombreComercial() { return nombreComercial; }
    public void setNombreComercial(String nombreComercial) { this.nombreComercial = nombreComercial; }

    public Integer getPotenciaHp() { return potenciaHp; }
    public void setPotenciaHp(Integer potenciaHp) { this.potenciaHp = potenciaHp; }

    public BigDecimal getCapacidadCargaTon() { return capacidadCargaTon; }
    public void setCapacidadCargaTon(BigDecimal capacidadCargaTon) { this.capacidadCargaTon = capacidadCargaTon; }

    public Integer getIntervaloPmHoras() { return intervaloPmHoras; }
    public void setIntervaloPmHoras(Integer intervaloPmHoras) { this.intervaloPmHoras = intervaloPmHoras; }
}

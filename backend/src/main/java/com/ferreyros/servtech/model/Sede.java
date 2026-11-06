package com.ferreyros.servtech.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

/* Entidad Sede: Centros de Reparación de Componentes y campamentos mineros */
@Entity
@Table(name = "sedes")
public class Sede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sede")
    private Integer idSede;

    @Column(name = "codigo_sede", nullable = false, unique = true, length = 20)
    private String codigoSede;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo; // TALLER_CRC, FAENA_MINERA, SUCURSAL_ZONAL

    @Column(name = "region", nullable = false, length = 50)
    private String region;

    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "creado_en")
    private OffsetDateTime creadoEn = OffsetDateTime.now();

    public Sede() {}

    public Sede(String codigoSede, String nombre, String tipo, String region, String direccion) {
        this.codigoSede = codigoSede;
        this.nombre = nombre;
        this.tipo = tipo;
        this.region = region;
        this.direccion = direccion;
        this.activo = true;
        this.creadoEn = OffsetDateTime.now();
    }

    public Integer getIdSede() { return idSede; }
    public void setIdSede(Integer idSede) { this.idSede = idSede; }

    public String getCodigoSede() { return codigoSede; }
    public void setCodigoSede(String codigoSede) { this.codigoSede = codigoSede; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public OffsetDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(OffsetDateTime creadoEn) { this.creadoEn = creadoEn; }
}

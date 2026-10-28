package com.ferreyros.servtech.model;

import jakarta.persistence.*;

/* Entidad Competencia: Catálogo de habilidades y certificaciones técnicas oficiales Caterpillar */
@Entity
@Table(name = "competencias")
public class Competencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_competencia")
    private Integer idCompetencia;

    @Column(name = "codigo_cat", nullable = false, unique = true, length = 50)
    private String codigoCat;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "categoria", nullable = false, length = 50)
    private String categoria; // MOTOR, SISTEMA_HIDRAULICO, TRANSMISION, TREN_RODAJE, ELECTRONICA_CAT

    public Competencia() {}

    public Competencia(String codigoCat, String nombre, String categoria) {
        this.codigoCat = codigoCat;
        this.nombre = nombre;
        this.categoria = categoria;
    }

    public Integer getIdCompetencia() { return idCompetencia; }
    public void setIdCompetencia(Integer idCompetencia) { this.idCompetencia = idCompetencia; }

    public String getCodigoCat() { return codigoCat; }
    public void setCodigoCat(String codigoCat) { this.codigoCat = codigoCat; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}

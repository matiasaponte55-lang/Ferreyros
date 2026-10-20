package com.ferreyros.servtech.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Tabla asociativa que acredita qué certificaciones oficiales Caterpillar posee cada técnico y su vigencia.
 */
@Entity
@Table(name = "tecnico_competencias")
public class TecnicoCompetencia {

    @EmbeddedId
    private TecnicoCompetenciaId id = new TecnicoCompetenciaId();

    @ManyToOne
    @MapsId("idTecnico")
    @JoinColumn(name = "id_tecnico")
    private Tecnico tecnico;

    @ManyToOne
    @MapsId("idCompetencia")
    @JoinColumn(name = "id_competencia")
    private Competencia competencia;

    @Column(name = "fecha_certificacion", nullable = false)
    private LocalDate fechaCertificacion;

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDate fechaExpiracion;

    @Column(name = "vigente", nullable = false)
    private Boolean vigente = true;

    public TecnicoCompetencia() {}

    public TecnicoCompetencia(Tecnico tecnico, Competencia competencia, LocalDate fechaCertificacion, LocalDate fechaExpiracion) {
        this.tecnico = tecnico;
        this.competencia = competencia;
        this.fechaCertificacion = fechaCertificacion;
        this.fechaExpiracion = fechaExpiracion;
        this.vigente = true;
        this.id = new TecnicoCompetenciaId(tecnico.getIdTecnico(), competencia.getIdCompetencia());
    }

    public TecnicoCompetenciaId getId() { return id; }
    public void setId(TecnicoCompetenciaId id) { this.id = id; }

    public Tecnico getTecnico() { return tecnico; }
    public void setTecnico(Tecnico tecnico) { this.tecnico = tecnico; }

    public Competencia getCompetencia() { return competencia; }
    public void setCompetencia(Competencia competencia) { this.competencia = competencia; }

    public LocalDate getFechaCertificacion() { return fechaCertificacion; }
    public void setFechaCertificacion(LocalDate fechaCertificacion) { this.fechaCertificacion = fechaCertificacion; }

    public LocalDate getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(LocalDate fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }

    public Boolean getVigente() { return vigente; }
    public void setVigente(Boolean vigente) { this.vigente = vigente; }

    @Embeddable
    public static class TecnicoCompetenciaId implements Serializable {
        private Integer idTecnico;
        private Integer idCompetencia;

        public TecnicoCompetenciaId() {}
        public TecnicoCompetenciaId(Integer idTecnico, Integer idCompetencia) {
            this.idTecnico = idTecnico;
            this.idCompetencia = idCompetencia;
        }

        public Integer getIdTecnico() { return idTecnico; }
        public void setIdTecnico(Integer idTecnico) { this.idTecnico = idTecnico; }

        public Integer getIdCompetencia() { return idCompetencia; }
        public void setIdCompetencia(Integer idCompetencia) { this.idCompetencia = idCompetencia; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TecnicoCompetenciaId that = (TecnicoCompetenciaId) o;
            return Objects.equals(idTecnico, that.idTecnico) && Objects.equals(idCompetencia, that.idCompetencia);
        }

        @Override
        public int hashCode() {
            return Objects.hash(idTecnico, idCompetencia);
        }
    }
}

package com.ferreyros.servtech.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entidad Tecnico:
 * Personal técnico especializado bajo régimen minero atípico (14x7)
 * y acreditación oficial Caterpillar Service Pro.
 */
@Entity
@Table(name = "tecnicos")
public class Tecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tecnico")
    private Integer idTecnico;

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "codigo_empleado", nullable = false, unique = true, length = 20)
    private String codigoEmpleado;

    @Column(name = "fotocheck", length = 20)
    private String fotocheck;

    @Column(name = "especialidad_principal", length = 100)
    private String especialidadPrincipal;

    @ManyToOne
    @JoinColumn(name = "id_sede_base", referencedColumnName = "id_sede", nullable = false)
    private Sede sedeBase;

    @Column(name = "nivel_certificacion", nullable = false, length = 40)
    private String nivelCertificacion = "Level 2"; // 'Foundational', 'Level 1', 'Level 2', 'Specialist', 'Master CAT'

    @Column(name = "regimen_guardia", length = 30)
    private String regimenGuardia = "14x7_DIURNO"; // '14x7_DIURNO', '14x7_NOCTURNO', '10x10', '5x2_TALLER'

    @Column(name = "turno_asignado", length = 30)
    private String turnoAsignado = "14X7_DIURNO";

    @Column(name = "estado_disponibilidad", length = 30)
    private String estadoDisponibilidad = "DISPONIBLE"; // 'DISPONIBLE', 'EN_SERVICIO', 'DESCANSO_LEGAL', 'CAPACITACION', 'LICENCIA_MEDICA'

    @Column(name = "estado_operativo", length = 30)
    private String estadoOperativo = "DISPONIBLE";

    @Column(name = "horas_acumuladas_mes", precision = 6, scale = 2)
    private BigDecimal horasAcumuladasMes = BigDecimal.ZERO;

    public Tecnico() {}

    public Integer getIdTecnico() { return idTecnico; }
    public void setIdTecnico(Integer idTecnico) { this.idTecnico = idTecnico; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getCodigoEmpleado() { return codigoEmpleado; }
    public void setCodigoEmpleado(String codigoEmpleado) { this.codigoEmpleado = codigoEmpleado; }

    public String getFotocheck() { return fotocheck; }
    public void setFotocheck(String fotocheck) { this.fotocheck = fotocheck; }

    public String getEspecialidadPrincipal() { return especialidadPrincipal; }
    public void setEspecialidadPrincipal(String especialidadPrincipal) { this.especialidadPrincipal = especialidadPrincipal; }

    public Sede getSedeBase() { return sedeBase; }
    public void setSedeBase(Sede sedeBase) { this.sedeBase = sedeBase; }

    public String getNivelCertificacion() { return nivelCertificacion; }
    public void setNivelCertificacion(String nivelCertificacion) { this.nivelCertificacion = nivelCertificacion; }

    public String getRegimenGuardia() { return regimenGuardia; }
    public void setRegimenGuardia(String regimenGuardia) {
        this.regimenGuardia = regimenGuardia;
        this.turnoAsignado = regimenGuardia;
    }

    public String getTurnoAsignado() { return turnoAsignado; }
    public void setTurnoAsignado(String turnoAsignado) {
        this.turnoAsignado = turnoAsignado;
        this.regimenGuardia = turnoAsignado;
    }

    public String getEstadoDisponibilidad() { return estadoDisponibilidad; }
    public void setEstadoDisponibilidad(String estadoDisponibilidad) {
        this.estadoDisponibilidad = estadoDisponibilidad;
        this.estadoOperativo = estadoDisponibilidad;
    }

    public String getEstadoOperativo() { return estadoOperativo; }
    public void setEstadoOperativo(String estadoOperativo) {
        this.estadoOperativo = estadoOperativo;
        this.estadoDisponibilidad = estadoOperativo;
    }

    public BigDecimal getHorasAcumuladasMes() { return horasAcumuladasMes; }
    public void setHorasAcumuladasMes(BigDecimal horasAcumuladasMes) { this.horasAcumuladasMes = horasAcumuladasMes; }
}

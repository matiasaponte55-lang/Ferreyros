package com.ferreyros.servtech.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

/**
 * Entidad AuditoriaEstadoOt:
 * Bitácora inmutable Append-Only de transiciones de estado de órdenes de trabajo (RF-14).
 * Registra cada cambio con marca temporal inalterable, usuario responsable (UUID)
 * y motivo obligatorio de retrabajo si fue rechazada en Control de Calidad.
 */
@Entity
@Table(name = "auditoria_estados_ot")
public class AuditoriaEstadoOt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Long idAuditoria;

    @ManyToOne
    @JoinColumn(name = "id_orden", referencedColumnName = "id_orden", nullable = false)
    private OrdenServicio orden;

    @Column(name = "estado_anterior", nullable = false, length = 30)
    private String estadoAnterior;

    @Column(name = "estado_nuevo", nullable = false, length = 30)
    private String estadoNuevo;

    @ManyToOne
    @JoinColumn(name = "cambiado_por", referencedColumnName = "id_usuario")
    private Usuario cambiadoPor;

    @Column(name = "motivo_retrabajo", columnDefinition = "TEXT")
    private String motivoRetrabajo;

    @Column(name = "fecha_cambio", nullable = false)
    private OffsetDateTime fechaCambio = OffsetDateTime.now();

    public AuditoriaEstadoOt() {}

    public AuditoriaEstadoOt(OrdenServicio orden, String estadoAnterior, String estadoNuevo, Usuario cambiadoPor, String motivoRetrabajo) {
        this.orden = orden;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.cambiadoPor = cambiadoPor;
        this.motivoRetrabajo = motivoRetrabajo;
        this.fechaCambio = OffsetDateTime.now();
    }

    public Long getIdAuditoria() { return idAuditoria; }
    public void setIdAuditoria(Long idAuditoria) { this.idAuditoria = idAuditoria; }

    public OrdenServicio getOrden() { return orden; }
    public void setOrden(OrdenServicio orden) { this.orden = orden; }

    public String getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(String estadoAnterior) { this.estadoAnterior = estadoAnterior; }

    public String getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(String estadoNuevo) { this.estadoNuevo = estadoNuevo; }

    public Usuario getCambiadoPor() { return cambiadoPor; }
    public void setCambiadoPor(Usuario cambiadoPor) { this.cambiadoPor = cambiadoPor; }

    public String getMotivoRetrabajo() { return motivoRetrabajo; }
    public void setMotivoRetrabajo(String motivoRetrabajo) { this.motivoRetrabajo = motivoRetrabajo; }

    public OffsetDateTime getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(OffsetDateTime fechaCambio) { this.fechaCambio = fechaCambio; }
}

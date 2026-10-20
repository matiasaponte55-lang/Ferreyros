package com.ferreyros.servtech.model;

import jakarta.persistence.*;

/**
 * Entidad RepuestoInstalado:
 * Registro de repuestos e insumos genuinos Caterpillar (1R-0716, filtros, aceites)
 * consumidos e instalados en la maquinaria durante la orden de servicio.
 */
@Entity
@Table(name = "repuestos_instalados")
public class RepuestoInstalado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_repuesto")
    private Integer idRepuesto;

    @ManyToOne
    @JoinColumn(name = "id_orden", referencedColumnName = "id_orden", nullable = false)
    private OrdenServicio orden;

    @Column(name = "numero_parte_cat", nullable = false, length = 80)
    private String numeroParteCat;

    @Column(name = "descripcion", nullable = false, length = 150)
    private String descripcion;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "instalado_por", referencedColumnName = "id_tecnico")
    private Tecnico instaladoPor;

    public RepuestoInstalado() {}

    public RepuestoInstalado(OrdenServicio orden, String numeroParteCat, String descripcion, Integer cantidad, Tecnico instaladoPor) {
        this.orden = orden;
        this.numeroParteCat = numeroParteCat;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.instaladoPor = instaladoPor;
    }

    public Integer getIdRepuesto() { return idRepuesto; }
    public void setIdRepuesto(Integer idRepuesto) { this.idRepuesto = idRepuesto; }

    public OrdenServicio getOrden() { return orden; }
    public void setOrden(OrdenServicio orden) { this.orden = orden; }

    public String getNumeroParteCat() { return numeroParteCat; }
    public void setNumeroParteCat(String numeroParteCat) { this.numeroParteCat = numeroParteCat; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Tecnico getInstaladoPor() { return instaladoPor; }
    public void setInstaladoPor(Tecnico instaladoPor) { this.instaladoPor = instaladoPor; }
}

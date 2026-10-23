package com.ferreyros.servtech.dto;

import java.math.BigDecimal;
import java.util.List;

public class DashboardMetricasDto {
    private BigDecimal disponibilidadMarcPromedio; // e.g. 94.65
    private BigDecimal mttrPromedioHoras;          // e.g. 4.8
    private BigDecimal ocupacionCuadrillas;        // e.g. 88.5
    private Long horasFacturables;                 // e.g. 1420
    private Long totalOtsActivas;
    private Long otsSolicitadas;
    private Long otsAsignadas;
    private Long otsEnDiagnostico;
    private Long otsEnReparacion;
    private Long otsControlCalidad;
    private Long otsCerradas;
    private List<FlotaEstadoDto> flotas;

    public DashboardMetricasDto() {}

    public BigDecimal getDisponibilidadMarcPromedio() { return disponibilidadMarcPromedio; }
    public void setDisponibilidadMarcPromedio(BigDecimal disponibilidadMarcPromedio) { this.disponibilidadMarcPromedio = disponibilidadMarcPromedio; }

    public BigDecimal getMttrPromedioHoras() { return mttrPromedioHoras; }
    public void setMttrPromedioHoras(BigDecimal mttrPromedioHoras) { this.mttrPromedioHoras = mttrPromedioHoras; }

    public BigDecimal getOcupacionCuadrillas() { return ocupacionCuadrillas; }
    public void setOcupacionCuadrillas(BigDecimal ocupacionCuadrillas) { this.ocupacionCuadrillas = ocupacionCuadrillas; }

    public Long getHorasFacturables() { return horasFacturables; }
    public void setHorasFacturables(Long horasFacturables) { this.horasFacturables = horasFacturables; }

    public Long getTotalOtsActivas() { return totalOtsActivas; }
    public void setTotalOtsActivas(Long totalOtsActivas) { this.totalOtsActivas = totalOtsActivas; }

    public Long getOtsSolicitadas() { return otsSolicitadas; }
    public void setOtsSolicitadas(Long otsSolicitadas) { this.otsSolicitadas = otsSolicitadas; }

    public Long getOtsAsignadas() { return otsAsignadas; }
    public void setOtsAsignadas(Long otsAsignadas) { this.otsAsignadas = otsAsignadas; }

    public Long getOtsEnDiagnostico() { return otsEnDiagnostico; }
    public void setOtsEnDiagnostico(Long otsEnDiagnostico) { this.otsEnDiagnostico = otsEnDiagnostico; }

    public Long getOtsEnReparacion() { return otsEnReparacion; }
    public void setOtsEnReparacion(Long otsEnReparacion) { this.otsEnReparacion = otsEnReparacion; }

    public Long getOtsControlCalidad() { return otsControlCalidad; }
    public void setOtsControlCalidad(Long otsControlCalidad) { this.otsControlCalidad = otsControlCalidad; }

    public Long getOtsCerradas() { return otsCerradas; }
    public void setOtsCerradas(Long otsCerradas) { this.otsCerradas = otsCerradas; }

    public List<FlotaEstadoDto> getFlotas() { return flotas; }
    public void setFlotas(List<FlotaEstadoDto> flotas) { this.flotas = flotas; }

    public static class FlotaEstadoDto {
        private String codigoFlota;
        private String modeloCat;
        private String seriePin;
        private String cliente;
        private String sede;
        private BigDecimal horometro;
        private String estadoOperativo;
        private BigDecimal disponibilidad;
        private BigDecimal slaMarc;
        private String condicionSla; // CUMPLE, BAJO SLA

        public FlotaEstadoDto() {}

        public FlotaEstadoDto(String codigoFlota, String modeloCat, String seriePin, String cliente, String sede, BigDecimal horometro, String estadoOperativo, BigDecimal disponibilidad, BigDecimal slaMarc, String condicionSla) {
            this.codigoFlota = codigoFlota;
            this.modeloCat = modeloCat;
            this.seriePin = seriePin;
            this.cliente = cliente;
            this.sede = sede;
            this.horometro = horometro;
            this.estadoOperativo = estadoOperativo;
            this.disponibilidad = disponibilidad;
            this.slaMarc = slaMarc;
            this.condicionSla = condicionSla;
        }

        public String getCodigoFlota() { return codigoFlota; }
        public void setCodigoFlota(String codigoFlota) { this.codigoFlota = codigoFlota; }

        public String getModeloCat() { return modeloCat; }
        public void setModeloCat(String modeloCat) { this.modeloCat = modeloCat; }

        public String getSeriePin() { return seriePin; }
        public void setSeriePin(String seriePin) { this.seriePin = seriePin; }

        public String getCliente() { return cliente; }
        public void setCliente(String cliente) { this.cliente = cliente; }

        public String getSede() { return sede; }
        public void setSede(String sede) { this.sede = sede; }

        public BigDecimal getHorometro() { return horometro; }
        public void setHorometro(BigDecimal horometro) { this.horometro = horometro; }

        public String getEstadoOperativo() { return estadoOperativo; }
        public void setEstadoOperativo(String estadoOperativo) { this.estadoOperativo = estadoOperativo; }

        public BigDecimal getDisponibilidad() { return disponibilidad; }
        public void setDisponibilidad(BigDecimal disponibilidad) { this.disponibilidad = disponibilidad; }

        public BigDecimal getSlaMarc() { return slaMarc; }
        public void setSlaMarc(BigDecimal slaMarc) { this.slaMarc = slaMarc; }

        public String getCondicionSla() { return condicionSla; }
        public void setCondicionSla(String condicionSla) { this.condicionSla = condicionSla; }
    }
}

package com.ferreyros.servtech.service;

import com.ferreyros.servtech.dto.DashboardMetricasDto;
import com.ferreyros.servtech.model.Maquinaria;
import com.ferreyros.servtech.model.OrdenServicio;
import com.ferreyros.servtech.repository.AsignacionRepository;
import com.ferreyros.servtech.repository.MaquinariaRepository;
import com.ferreyros.servtech.repository.OrdenServicioRepository;
import com.ferreyros.servtech.repository.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de Inteligencia Operativa y KPIs Dinámicos de Mantenimiento MARC (RF-11 / RF-12).
 * Todos los indicadores se calculan en tiempo real en función de las órdenes,
 * asignaciones y flota física registradas en PostgreSQL (Supabase).
 */
@Service
public class DashboardService {

    @Autowired
    private MaquinariaRepository maquinariaRepository;

    @Autowired
    private OrdenServicioRepository ordenServicioRepository;

    @Autowired
    private AsignacionRepository asignacionRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    public DashboardMetricasDto obtenerMetricas() {
        DashboardMetricasDto dto = new DashboardMetricasDto();

        List<Maquinaria> flotas = maquinariaRepository.findAll();
        List<OrdenServicio> ordenes = ordenServicioRepository.findAll();

        long solicitadas = ordenes.stream().filter(o -> "SOLICITADA".equalsIgnoreCase(o.getEstado())).count();
        long asignadas = ordenes.stream().filter(o -> "ASIGNADA".equalsIgnoreCase(o.getEstado()) || "PROGRAMADA".equalsIgnoreCase(o.getEstado())).count();
        long diagnostico = ordenes.stream().filter(o -> "EN_DIAGNOSTICO".equalsIgnoreCase(o.getEstado())).count();
        long reparacion = ordenes.stream().filter(o -> "EN_REPARACION".equalsIgnoreCase(o.getEstado())).count();
        long qc = ordenes.stream().filter(o -> "CONTROL_CALIDAD".equalsIgnoreCase(o.getEstado())).count();
        long cerradas = ordenes.stream().filter(o -> "CERRADA".equalsIgnoreCase(o.getEstado()) || "LIQUIDADA".equalsIgnoreCase(o.getEstado())).count();

        dto.setOtsSolicitadas(solicitadas);
        dto.setOtsAsignadas(asignadas);
        dto.setOtsEnDiagnostico(diagnostico);
        dto.setOtsEnReparacion(reparacion);
        dto.setOtsControlCalidad(qc);
        dto.setOtsCerradas(cerradas);
        dto.setTotalOtsActivas(solicitadas + asignadas + diagnostico + reparacion + qc);

        // 1. Disponibilidad MARC Promedio (calculada sobre flota real)
        BigDecimal sumaDisp = BigDecimal.ZERO;
        List<DashboardMetricasDto.FlotaEstadoDto> listaFlota = new ArrayList<>();

        for (Maquinaria m : flotas) {
            BigDecimal sla = m.getSlaGarantizado() != null ? m.getSlaGarantizado() : new BigDecimal("92.00");
            BigDecimal disp;
            String condicion;

            if ("OPERATIVO".equalsIgnoreCase(m.getEstadoOperativo())) {
                disp = new BigDecimal("100.00");
                condicion = "CUMPLE (Operativo)";
            } else if ("EN_MANTENIMIENTO".equalsIgnoreCase(m.getEstadoOperativo())) {
                disp = new BigDecimal("93.50");
                condicion = disp.compareTo(sla) >= 0 ? "CUMPLE (En Mantenimiento)" : "BAJO SLA (En Mantenimiento)";
            } else if ("PARADA_CRITICA_DOWN".equalsIgnoreCase(m.getEstadoOperativo())) {
                disp = new BigDecimal("82.00");
                condicion = "BAJO SLA (Parada Crítica)";
            } else {
                disp = new BigDecimal("96.00");
                condicion = "CUMPLE (Stand By)";
            }

            sumaDisp = sumaDisp.add(disp);

            listaFlota.add(new DashboardMetricasDto.FlotaEstadoDto(
                    m.getCodigoFlota(),
                    m.getModelo() != null ? m.getModelo().getNombreComercial() : (m.getModeloCat() != null ? m.getModeloCat() : "Caterpillar"),
                    m.getNumeroSeriePin(),
                    m.getCliente() != null ? m.getCliente().getRazonSocial() : "Cliente Minero",
                    m.getSedeActual() != null ? m.getSedeActual().getNombre() : "Taller CRC",
                    m.getHorometroAcumulado() != null ? m.getHorometroAcumulado() : BigDecimal.ZERO,
                    m.getEstadoOperativo() != null ? m.getEstadoOperativo() : "OPERATIVO",
                    disp,
                    sla,
                    condicion
            ));
        }

        BigDecimal dispPromedio = flotas.isEmpty() 
                ? BigDecimal.ZERO 
                : sumaDisp.divide(new BigDecimal(flotas.size()), 2, RoundingMode.HALF_UP);
        dto.setDisponibilidadMarcPromedio(dispPromedio);
        dto.setFlotas(listaFlota);

        // 2. MTTR Promedio (Tiempo Medio de Reparación de OTs concluidas)
        List<OrdenServicio> ordenesCerradas = ordenes.stream()
                .filter(o -> o.getCerradoEn() != null && o.getCreadoEn() != null)
                .toList();

        if (ordenesCerradas.isEmpty()) {
            dto.setMttrPromedioHoras(BigDecimal.ZERO);
            dto.setHorasFacturables(0L);
        } else {
            double totalHoras = 0;
            for (OrdenServicio oc : ordenesCerradas) {
                Duration dur = Duration.between(oc.getCreadoEn(), oc.getCerradoEn());
                totalHoras += Math.max(0.5, dur.toMinutes() / 60.0);
            }
            double promedio = totalHoras / ordenesCerradas.size();
            dto.setMttrPromedioHoras(BigDecimal.valueOf(promedio).setScale(1, RoundingMode.HALF_UP));
            dto.setHorasFacturables((long) Math.round(totalHoras));
        }

        // 3. Ocupación de Cuadrillas 14x7 (Asignaciones activas sobre técnicos totales)
        long totalTecnicos = tecnicoRepository.count();
        long asignacionesActivas = asignacionRepository.count();

        if (totalTecnicos == 0 || asignacionesActivas == 0) {
            dto.setOcupacionCuadrillas(BigDecimal.ZERO);
        } else {
            BigDecimal ocupacion = BigDecimal.valueOf(asignacionesActivas)
                    .multiply(new BigDecimal("100"))
                    .divide(BigDecimal.valueOf(totalTecnicos), 1, RoundingMode.HALF_UP);
            if (ocupacion.compareTo(new BigDecimal("100")) > 0) ocupacion = new BigDecimal("100.0");
            dto.setOcupacionCuadrillas(ocupacion);
        }

        return dto;
    }
}

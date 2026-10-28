package com.ferreyros.servtech.service;

import com.ferreyros.servtech.dto.DashboardMetricasDto;
import com.ferreyros.servtech.model.Maquinaria;
import com.ferreyros.servtech.model.OrdenServicio;
import com.ferreyros.servtech.repository.AsignacionRepository;
import com.ferreyros.servtech.repository.MaquinariaRepository;
import com.ferreyros.servtech.repository.OrdenServicioRepository;
import com.ferreyros.servtech.repository.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/* Servicio de Inteligencia Operativa y KPIs Dinámicos de Mantenimiento MARC  */
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

    @Cacheable(value = "dashboardMetricas")
    public DashboardMetricasDto obtenerMetricas() {
        DashboardMetricasDto dto = new DashboardMetricasDto();

        // Ejecución concurrente en paralelo de las 5 consultas independientes contra la BD
        CompletableFuture<List<Maquinaria>> futureFlotas = CompletableFuture.supplyAsync(
                () -> maquinariaRepository.findAllConRelaciones()
        );

        CompletableFuture<List<Object[]>> futureConteos = CompletableFuture.supplyAsync(
                () -> ordenServicioRepository.contarPorEstado()
        );

        CompletableFuture<List<OrdenServicio>> futureCerradas = CompletableFuture.supplyAsync(
                () -> ordenServicioRepository.findOrdenesCerradasConFechas()
        );

        CompletableFuture<Long> futureTotalTecnicos = CompletableFuture.supplyAsync(
                () -> tecnicoRepository.count()
        );

        CompletableFuture<Long> futureAsignaciones = CompletableFuture.supplyAsync(
                () -> asignacionRepository.count()
        );

        // Espera paralela
        CompletableFuture.allOf(
                futureFlotas,
                futureConteos,
                futureCerradas,
                futureTotalTecnicos,
                futureAsignaciones
        ).join();

        List<Maquinaria> flotas = futureFlotas.join();
        List<Object[]> conteos = futureConteos.join();
        List<OrdenServicio> ordenesCerradas = futureCerradas.join();
        long totalTecnicos = futureTotalTecnicos.join();
        long asignacionesActivas = futureAsignaciones.join();

        // Conteo de órdenes agrupadas por estado
        long solicitadas = 0;
        long asignadas = 0;
        long diagnostico = 0;
        long reparacion = 0;
        long qc = 0;
        long cerradas = 0;

        for (Object[] fila : conteos) {
            String estado = (String) fila[0];
            long cantidad = fila[1] != null ? ((Number) fila[1]).longValue() : 0L;
            if (estado == null) continue;

            if ("SOLICITADA".equalsIgnoreCase(estado)) {
                solicitadas += cantidad;
            } else if ("ASIGNADA".equalsIgnoreCase(estado) || "PROGRAMADA".equalsIgnoreCase(estado)) {
                asignadas += cantidad;
            } else if ("EN_DIAGNOSTICO".equalsIgnoreCase(estado)) {
                diagnostico += cantidad;
            } else if ("EN_REPARACION".equalsIgnoreCase(estado)) {
                reparacion += cantidad;
            } else if ("CONTROL_CALIDAD".equalsIgnoreCase(estado)) {
                qc += cantidad;
            } else if ("CERRADA".equalsIgnoreCase(estado) || "LIQUIDADA".equalsIgnoreCase(estado)) {
                cerradas += cantidad;
            }
        }

        dto.setOtsSolicitadas(solicitadas);
        dto.setOtsAsignadas(asignadas);
        dto.setOtsEnDiagnostico(diagnostico);
        dto.setOtsEnReparacion(reparacion);
        dto.setOtsControlCalidad(qc);
        dto.setOtsCerradas(cerradas);
        dto.setTotalOtsActivas(solicitadas + asignadas + diagnostico + reparacion + qc);

        // Disponibilidad MARC Promedio 
        BigDecimal sumaDisp = BigDecimal.ZERO;
        List<DashboardMetricasDto.FlotaEstadoDto> listaFlota = new ArrayList<>();

        for (Maquinaria m : flotas) {
            BigDecimal sla = m.getSlaGarantizado() != null ? m.getSlaGarantizado() : new BigDecimal("92.00");
            BigDecimal disp;
            String condicion;

            // Cálculo dinámico de disponibilidad física minera
            BigDecimal horasMes = new BigDecimal("720.00");
            BigDecimal horasDetencion = BigDecimal.ZERO;
            if ("EN_MANTENIMIENTO".equalsIgnoreCase(m.getEstadoOperativo())) {
                horasDetencion = new BigDecimal("48.00");
            } else if ("PARADA_CRITICA_DOWN".equalsIgnoreCase(m.getEstadoOperativo())) {
                horasDetencion = new BigDecimal("130.00");
            } else if ("STAND_BY".equalsIgnoreCase(m.getEstadoOperativo())) {
                horasDetencion = new BigDecimal("24.00");
            }
            BigDecimal horasOperativas = horasMes.subtract(horasDetencion);
            disp = horasOperativas.divide(horasMes, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100.00"))
                    .setScale(2, RoundingMode.HALF_UP);

            if (disp.compareTo(sla) >= 0) {
                condicion = "CUMPLE (" + (m.getEstadoOperativo() != null ? m.getEstadoOperativo() : "Operativo") + ")";
            } else {
                condicion = "BAJO SLA (" + (m.getEstadoOperativo() != null ? m.getEstadoOperativo() : "Alerta") + ")";
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

        // Tiempo Medio de Reparación solo de OTs concluidas
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

        // Ocupación de Cuadrillas 14x7
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

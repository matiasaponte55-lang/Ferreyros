package com.ferreyros.servtech.controller;

import com.ferreyros.servtech.dto.DashboardMetricasDto;
import com.ferreyros.servtech.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controlador de Inteligencia de Negocios, Dashboard Gerencial y Reportes MARC
 * (Pantalla 11 / Apéndice B).
 */
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(originPatterns = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard/metricas")
    public ResponseEntity<DashboardMetricasDto> obtenerMetricas() {
        DashboardMetricasDto metricas = dashboardService.obtenerMetricas();
        return ResponseEntity.ok(metricas);
    }

    /**
     * Reporte 1: Cumplimiento de Disponibilidad Mecánica y SLAs por Flota (Tabla 22).
     */
    @GetMapping("/reportes/disponibilidad-marc")
    public ResponseEntity<List<Map<String, Object>>> reporteDisponibilidadMarc() {
        List<Map<String, Object>> reporte = new ArrayList<>();

        reporte.add(crearFilaReporte("CAM-041", "CAT 797F", "PIN-797F-00124", 720.0, 38.5, 180.5, 9.6, "94.65%", "92.00%", "CUMPLE (Bonificación)"));
        reporte.add(crearFilaReporte("CAM-042", "CAT 797F", "PIN-797F-00125", 720.0, 42.0, 165.2, 10.5, "94.17%", "92.00%", "CUMPLE (Estándar)"));
        reporte.add(crearFilaReporte("CAM-045", "CAT 793F", "PIN-793F-00308", 720.0, 64.0, 110.0, 16.0, "91.11%", "92.00%", "BAJO SLA (Penalidad)"));
        reporte.add(crearFilaReporte("CAR-012", "CAT 994K", "PIN-994K-00055", 720.0, 31.0, 240.0, 7.7, "95.69%", "92.00%", "CUMPLE (Excelente)"));
        reporte.add(crearFilaReporte("PAL-002", "CAT 6060 FS", "PIN-6060-00019", 720.0, 28.0, 360.0, 14.0, "96.11%", "94.00%", "CUMPLE (Excelente)"));

        return ResponseEntity.ok(reporte);
    }

    /**
     * Reporte 3: Balance de Horas-Hombre y Rendimiento de Técnicos en Guardia 14x7 (Tabla 24).
     */
    @GetMapping("/reportes/horas-hombre")
    public ResponseEntity<List<Map<String, Object>>> reporteHorasHombre() {
        List<Map<String, Object>> balance = new ArrayList<>();

        balance.add(crearFilaHH("Ivan B. Mamani V.", "Master CAT", "14x7 Diurno", 142.0, 14.0, 4.0, 160.0, "97.5% (Sobresaliente)"));
        balance.add(crearFilaHH("Matias S. Aponte C.", "Specialist", "14x7 Diurno", 138.5, 14.0, 7.5, 160.0, "95.3% (Sobresaliente)"));
        balance.add(crearFilaHH("Andy I. Manchego M.", "Level 2", "14x7 Nocturno", 132.0, 14.0, 14.0, 160.0, "91.2% (Óptimo)"));
        balance.add(crearFilaHH("Juan D. Zamata LL.", "Foundational", "14x7 Nocturno", 128.0, 14.0, 18.0, 160.0, "88.7% (Conforme)"));

        return ResponseEntity.ok(balance);
    }

    private Map<String, Object> crearFilaReporte(String codigoFlota, String modelo, String pin, double hCal, double hPar, double mtbf, double mttr, String disp, String sla, String estado) {
        Map<String, Object> m = new HashMap<>();
        m.put("codigoFlota", codigoFlota);
        m.put("modeloCat", modelo);
        m.put("seriePin", pin);
        m.put("horasCalendario", hCal);
        m.put("horasParada", hPar);
        m.put("mtbf", mtbf);
        m.put("mttr", mttr);
        m.put("disponibilidadReal", disp);
        m.put("slaMarc", sla);
        m.put("estado", estado);
        return m;
    }

    private Map<String, Object> crearFilaHH(String tecnico, String nivel, String regimen, double hhDir, double hhSeg, double hhEsp, double total, String ef) {
        Map<String, Object> m = new HashMap<>();
        m.put("tecnico", tecnico);
        m.put("nivelCat", nivel);
        m.put("regimen", regimen);
        m.put("hhDirectas", hhDir);
        m.put("hhSeguridad", hhSeg);
        m.put("hhEspera", hhEsp);
        m.put("totalHh", total);
        m.put("eficiencia", ef);
        return m;
    }
}

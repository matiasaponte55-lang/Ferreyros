package com.ferreyros.servtech.controller;

import com.ferreyros.servtech.dto.DashboardMetricasDto;
import com.ferreyros.servtech.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de Inteligencia de Negocios y Dashboard Gerencial (Pantalla 11 / Apéndice B).
 * Todos los indicadores y métricas se obtienen de forma 100% dinámica desde la base de datos
 * en Supabase PostgreSQL, reflejando exclusivamente los registros ingresados a través de la web.
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
}

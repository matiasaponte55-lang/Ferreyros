package com.ferreyros.servtech.controller;

import com.ferreyros.servtech.dto.DashboardMetricasDto;
import com.ferreyros.servtech.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/* Ayuda a obtener métricas y KPIs de la operación de forma dinámica */
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

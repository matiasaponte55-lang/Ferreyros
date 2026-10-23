package com.ferreyros.servtech.controller;

import com.ferreyros.servtech.dto.AsignacionRequest;
import com.ferreyros.servtech.model.Asignacion;
import com.ferreyros.servtech.service.AsignacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para la Matriz Dispatcher View y Asignación de Cuadrillas (Pantalla 7 / CU-03 / RF-04).
 * Si existe solapamiento temporal responde con HTTP 409 Conflict (Regla GiST).
 */
@RestController
@RequestMapping("/api/v1/asignaciones")
@CrossOrigin(originPatterns = "*")
public class AsignacionController {

    @Autowired
    private AsignacionService asignacionService;

    @GetMapping
    public ResponseEntity<List<Asignacion>> listarTodas() {
        List<Asignacion> lista = asignacionService.listarTodas();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/orden/{idOrden}")
    public ResponseEntity<List<Asignacion>> listarPorOrden(@PathVariable("idOrden") Integer idOrden) {
        List<Asignacion> lista = asignacionService.listarPorOrden(idOrden);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/tecnico/{idTecnico}")
    public ResponseEntity<List<Asignacion>> listarPorTecnico(@PathVariable("idTecnico") Integer idTecnico) {
        List<Asignacion> lista = asignacionService.listarPorTecnico(idTecnico);
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<Asignacion> asignar(@RequestBody AsignacionRequest request) {
        Asignacion asignacion = asignacionService.asignarCuadrilla(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(asignacion);
    }
}

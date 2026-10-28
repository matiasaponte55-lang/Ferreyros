package com.ferreyros.servtech.controller;

import com.ferreyros.servtech.dto.AsignacionRequest;
import com.ferreyros.servtech.model.Asignacion;
import com.ferreyros.servtech.service.AsignacionService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* Controla la asignacion de cuadrillas, y el error HTTP 409 */
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
    public ResponseEntity<Asignacion> asignar(@Valid @RequestBody AsignacionRequest request) {
        Asignacion asignacion = asignacionService.asignarCuadrilla(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(asignacion);
    }
}

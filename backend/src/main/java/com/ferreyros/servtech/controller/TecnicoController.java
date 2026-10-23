package com.ferreyros.servtech.controller;

import com.ferreyros.servtech.model.Tecnico;
import com.ferreyros.servtech.service.TecnicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador de cuadrillas técnicas, turnos mineros 14x7 y competencias CAT (Pantalla 4 / RF-13).
 */
@RestController
@RequestMapping("/api/v1/tecnicos")
@CrossOrigin(originPatterns = "*")
public class TecnicoController {

    @Autowired
    private TecnicoService tecnicoService;

    @GetMapping
    public ResponseEntity<List<Tecnico>> listarTodos(
            @RequestParam(value = "certificacion", required = false) String certificacion,
            @RequestParam(value = "turno", required = false) String turno
    ) {
        List<Tecnico> lista = tecnicoService.listarTodos(certificacion, turno);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Tecnico>> listarDisponibles() {
        List<Tecnico> disponibles = tecnicoService.listarDisponibles();
        return ResponseEntity.ok(disponibles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tecnico> obtenerPorId(@PathVariable("id") Integer id) {
        Tecnico t = tecnicoService.obtenerPorId(id);
        return ResponseEntity.ok(t);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Tecnico> cambiarEstado(
            @PathVariable("id") Integer id,
            @RequestParam(value = "nuevoEstado", required = false) String nuevoEstado,
            @RequestBody(required = false) Map<String, String> body
    ) {
        String estado = (nuevoEstado != null && !nuevoEstado.isEmpty()) 
                ? nuevoEstado 
                : (body != null ? body.get("nuevoEstado") : null);
        Tecnico actualizado = tecnicoService.cambiarEstado(id, estado);
        return ResponseEntity.ok(actualizado);
    }
}

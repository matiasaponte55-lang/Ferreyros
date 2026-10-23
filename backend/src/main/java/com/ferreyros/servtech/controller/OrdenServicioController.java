package com.ferreyros.servtech.controller;

import com.ferreyros.servtech.dto.ActualizarEstadoOtRequest;
import com.ferreyros.servtech.dto.NuevaOrdenRequest;
import com.ferreyros.servtech.model.OrdenServicio;
import com.ferreyros.servtech.service.OrdenServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para Órdenes de Servicio (Pantallas 5, 6, 8 / RF-02, RF-03, RF-05, RF-06).
 */
@RestController
@RequestMapping("/api/v1/ordenes-servicio")
@CrossOrigin(originPatterns = "*")
public class OrdenServicioController {

    @Autowired
    private OrdenServicioService ordenServicioService;

    @GetMapping
    public ResponseEntity<List<OrdenServicio>> listar(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer idSede,
            @RequestParam(required = false) String prioridad,
            @RequestParam(required = false) String buscar
    ) {
        List<OrdenServicio> ordenes = ordenServicioService.listarOrdenes(estado, idSede, prioridad, buscar);
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenServicio> obtenerPorId(@PathVariable Integer id) {
        OrdenServicio orden = ordenServicioService.obtenerPorId(id);
        return ResponseEntity.ok(orden);
    }

    @GetMapping("/maquinaria/{idMaquinaria}")
    public ResponseEntity<List<OrdenServicio>> listarPorMaquinaria(@PathVariable Integer idMaquinaria) {
        List<OrdenServicio> ordenes = ordenServicioService.listarPorMaquinaria(idMaquinaria);
        return ResponseEntity.ok(ordenes);
    }

    @PostMapping
    public ResponseEntity<OrdenServicio> crearOrden(@RequestBody NuevaOrdenRequest request) {
        OrdenServicio creada = ordenServicioService.crearOrden(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<OrdenServicio> actualizarEstado(
            @PathVariable Integer id,
            @RequestBody(required = false) ActualizarEstadoOtRequest request,
            @RequestParam(required = false) String nuevoEstado,
            @RequestParam(required = false) String comentario,
            @RequestParam(required = false) String motivoRetrabajo
    ) {
        if (request == null) {
            request = new ActualizarEstadoOtRequest();
        }
        if (request.getNuevoEstado() == null && nuevoEstado != null) {
            request.setNuevoEstado(nuevoEstado);
        }
        if (request.getMotivoRetrabajo() == null) {
            if (motivoRetrabajo != null) {
                request.setMotivoRetrabajo(motivoRetrabajo);
            } else if (comentario != null) {
                request.setMotivoRetrabajo(comentario);
            }
        }
        OrdenServicio actualizada = ordenServicioService.actualizarEstado(id, request);
        return ResponseEntity.ok(actualizada);
    }
}

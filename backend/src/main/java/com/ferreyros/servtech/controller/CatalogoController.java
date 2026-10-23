package com.ferreyros.servtech.controller;

import com.ferreyros.servtech.model.*;
import com.ferreyros.servtech.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador auxiliar de catálogos maestros y bitácora de auditoría inmutable (RF-14).
 */
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(originPatterns = "*")
public class CatalogoController {

    @Autowired
    private SedeRepository sedeRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ModeloCatMaestroRepository modeloRepository;

    @Autowired
    private CompetenciaRepository competenciaRepository;

    @Autowired
    private AuditoriaEstadoOtRepository auditoriaRepository;

    @GetMapping("/sedes")
    public ResponseEntity<List<Sede>> listarSedes() {
        return ResponseEntity.ok(sedeRepository.findAll());
    }

    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> listarClientes() {
        return ResponseEntity.ok(clienteRepository.findAll());
    }

    @GetMapping("/modelos")
    public ResponseEntity<List<ModeloCatMaestro>> listarModelos() {
        return ResponseEntity.ok(modeloRepository.findAll());
    }

    @GetMapping("/competencias")
    public ResponseEntity<List<Competencia>> listarCompetencias() {
        return ResponseEntity.ok(competenciaRepository.findAll());
    }

    @GetMapping("/auditoria/{idOrden}")
    public ResponseEntity<List<AuditoriaEstadoOt>> listarAuditoriaPorOrden(@PathVariable("idOrden") Integer idOrden) {
        return ResponseEntity.ok(auditoriaRepository.findByOrden_IdOrdenOrderByFechaCambioDesc(idOrden));
    }

    @GetMapping("/auditoria")
    public ResponseEntity<List<AuditoriaEstadoOt>> listarTodaAuditoria() {
        return ResponseEntity.ok(auditoriaRepository.findAllByOrderByFechaCambioDesc());
    }
}

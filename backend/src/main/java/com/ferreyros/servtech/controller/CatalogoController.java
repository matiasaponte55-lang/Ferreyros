package com.ferreyros.servtech.controller;

import com.ferreyros.servtech.model.*;
import com.ferreyros.servtech.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Controlador auxiliar de catálogos maestros y bitácora de auditoría inmutable (RF-14).
 * Optimizado con Spring Cache y cabeceras HTTP Cache-Control (5 minutos).
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
    @Cacheable("sedes")
    public ResponseEntity<List<Sede>> listarSedes() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(300, TimeUnit.SECONDS).cachePublic())
                .body(sedeRepository.findAll());
    }

    @GetMapping("/clientes")
    @Cacheable("clientes")
    public ResponseEntity<List<Cliente>> listarClientes() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(300, TimeUnit.SECONDS).cachePublic())
                .body(clienteRepository.findAll());
    }

    @GetMapping("/modelos")
    @Cacheable("modelos")
    public ResponseEntity<List<ModeloCatMaestro>> listarModelos() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(300, TimeUnit.SECONDS).cachePublic())
                .body(modeloRepository.findAll());
    }

    @GetMapping("/competencias")
    @Cacheable("competencias")
    public ResponseEntity<List<Competencia>> listarCompetencias() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(300, TimeUnit.SECONDS).cachePublic())
                .body(competenciaRepository.findAll());
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

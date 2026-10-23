package com.ferreyros.servtech.controller;

import com.ferreyros.servtech.dto.FirmaRequest;
import com.ferreyros.servtech.dto.InspeccionRequest;
import com.ferreyros.servtech.model.ChecklistInspeccion;
import com.ferreyros.servtech.service.InspeccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para Inspección Técnica Digital ATS (DS-024) y Firma Digital con Hash SHA-256
 * (Pantallas 9 y 10 / CU-06 / RF-07, RF-08, RF-09, RF-10).
 */
@RestController
@RequestMapping("/api/v1/inspecciones")
@CrossOrigin(originPatterns = "*")
public class InspeccionController {

    @Autowired
    private InspeccionService inspeccionService;

    @GetMapping("/orden/{idOrden}")
    public ResponseEntity<ChecklistInspeccion> obtenerPorOrden(@PathVariable("idOrden") Integer idOrden) {
        ChecklistInspeccion ch = inspeccionService.obtenerPorOrden(idOrden);
        return ResponseEntity.ok(ch);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChecklistInspeccion> obtenerPorId(@PathVariable("id") Integer id) {
        ChecklistInspeccion ch = inspeccionService.obtenerPorId(id);
        return ResponseEntity.ok(ch);
    }

    @PostMapping
    public ResponseEntity<ChecklistInspeccion> guardarInspeccion(@RequestBody InspeccionRequest request) {
        ChecklistInspeccion guardada = inspeccionService.guardarInspeccion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    @PostMapping("/{idOrden}/firmar")
    public ResponseEntity<ChecklistInspeccion> firmarInspeccion(
            @PathVariable("idOrden") Integer idOrden,
            @RequestBody FirmaRequest request
    ) {
        ChecklistInspeccion firmada = inspeccionService.firmarInspeccion(idOrden, request);
        return ResponseEntity.ok(firmada);
    }

    @PostMapping("/{id}/evidencias")
    public ResponseEntity<Map<String, Object>> subirEvidencia(
            @PathVariable("id") Integer id,
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam(value = "descripcion", required = false) String descripcion
    ) {
        Map<String, Object> resp = new HashMap<>();
        try {
            String uploadDir = "uploads/evidencias";
            java.nio.file.Path rutaDirectorio = java.nio.file.Paths.get(uploadDir);
            if (!java.nio.file.Files.exists(rutaDirectorio)) {
                java.nio.file.Files.createDirectories(rutaDirectorio);
            }

            String nombreOriginal = archivo.getOriginalFilename() != null ? archivo.getOriginalFilename() : "evidencia.jpg";
            String nombreFinal = System.currentTimeMillis() + "_" + nombreOriginal.replaceAll("[^a-zA-Z0-9.-]", "_");
            java.nio.file.Path rutaArchivo = rutaDirectorio.resolve(nombreFinal);
            java.nio.file.Files.copy(archivo.getInputStream(), rutaArchivo, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            resp.put("idEvidencia", System.currentTimeMillis());
            resp.put("nombreArchivo", nombreFinal);
            resp.put("rutaAlmacenamiento", rutaArchivo.toAbsolutePath().toString());
            resp.put("pesoMb", (double) archivo.getSize() / (1024 * 1024));
            resp.put("urlStorage", "/api/v1/evidencias/" + nombreFinal);
            resp.put("mensaje", "Evidencia fotográfica persistida satisfactoriamente");
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (Exception e) {
            resp.put("error", "Error al persistir evidencia fotográfica: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }
}

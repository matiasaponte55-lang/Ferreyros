package com.ferreyros.servtech.controller;

import com.ferreyros.servtech.model.Maquinaria;
import com.ferreyros.servtech.service.MaquinariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para la gestión del catálogo de maquinaria pesada CAT (Pantalla 2 y 3 / RF-12).
 */
@RestController
@RequestMapping("/api/v1/maquinarias")
@CrossOrigin(originPatterns = "*")
public class MaquinariaController {

    @Autowired
    private MaquinariaService maquinariaService;

    @GetMapping
    public ResponseEntity<List<Maquinaria>> listarTodas(
            @RequestParam(value = "familia", required = false) String familia,
            @RequestParam(value = "estado", required = false) String estado,
            @RequestParam(value = "buscar", required = false) String buscar
    ) {
        List<Maquinaria> lista = maquinariaService.listarTodas(familia, estado, buscar);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Maquinaria> obtenerPorId(@PathVariable("id") Integer id) {
        Maquinaria m = maquinariaService.obtenerPorId(id);
        return ResponseEntity.ok(m);
    }

    @PostMapping
    public ResponseEntity<Maquinaria> registrar(@RequestBody Maquinaria maquinaria) {
        Maquinaria guardada = maquinariaService.guardar(maquinaria);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }
}

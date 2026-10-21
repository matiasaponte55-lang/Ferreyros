package com.ferreyros.servtech.controller;

import com.ferreyros.servtech.dto.LoginRequest;
import com.ferreyros.servtech.dto.LoginResponse;
import com.ferreyros.servtech.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de Autenticación y Perímetro de Seguridad (Pantalla 1 / RF-01).
 * Expone el endpoint POST /api/v1/auth/login.
 */
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(originPatterns = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}

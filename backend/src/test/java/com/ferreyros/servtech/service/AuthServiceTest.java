package com.ferreyros.servtech.service;

import com.ferreyros.servtech.dto.LoginRequest;
import com.ferreyros.servtech.dto.LoginResponse;
import com.ferreyros.servtech.model.Usuario;
import com.ferreyros.servtech.repository.UsuarioRepository;
import com.ferreyros.servtech.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/* Pruebas unitarias de autenticación RBAC y validación BCrypt */
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("Rechazar autenticación si el usuario no existe en la base de datos")
    void testLogin_UsuarioNoExiste_Lanza401() {
        LoginRequest req = new LoginRequest();
        req.setUsername("noexiste@ferreyros.com.pe");
        req.setPassword("Clave123!");

        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            authService.login(req);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }

    @Test
    @DisplayName("Autenticación exitosa genera Token JWT con perfil RBAC verificado")
    void testLogin_CredencialesCorrectas_Exito() {
        Usuario user = new Usuario();
        user.setUsername("supervisor@ferreyros.com.pe");
        user.setEmail("supervisor@ferreyros.com.pe");
        user.setPasswordHash(encoder.encode("ClaveCorrecta2026"));
        user.setRol("ROLE_SUPERVISOR");
        user.setNombres("Juan");
        user.setApellidos("Zamata");

        when(usuarioRepository.findByUsername("supervisor@ferreyros.com.pe")).thenReturn(Optional.of(user));
        when(jwtUtil.generarToken(anyString(), anyString(), anyString())).thenReturn("fake.jwt.token");

        LoginRequest req = new LoginRequest();
        req.setUsername("supervisor@ferreyros.com.pe");
        req.setPassword("ClaveCorrecta2026");

        LoginResponse resp = authService.login(req);

        assertNotNull(resp);
        assertEquals("fake.jwt.token", resp.getToken());
        assertEquals("ROLE_SUPERVISOR", resp.getRol());
        assertEquals("supervisor@ferreyros.com.pe", resp.getUser().getUsername());
    }
}

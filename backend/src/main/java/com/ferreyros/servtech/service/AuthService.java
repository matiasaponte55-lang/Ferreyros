package com.ferreyros.servtech.service;

import com.ferreyros.servtech.dto.LoginRequest;
import com.ferreyros.servtech.dto.LoginResponse;
import com.ferreyros.servtech.dto.UsuarioDto;
import com.ferreyros.servtech.model.Usuario;
import com.ferreyros.servtech.repository.UsuarioRepository;
import com.ferreyros.servtech.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/* Valida la credenciales para poder entrar a la pagina */
@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginResponse login(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        if (username == null || username.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de usuario o correo corporativo es obligatorio");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña es obligatoria");
        }

        String search = username.trim();
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(search);
        if (usuarioOpt.isEmpty()) {
            usuarioOpt = usuarioRepository.findByEmail(search);
        }

        if (usuarioOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas: usuario no registrado en el sistema");
        }

        Usuario usuario = usuarioOpt.get();

        if (Boolean.FALSE.equals(usuario.getActivo())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado: cuenta de colaborador inactiva");
        }

        // Validación estricta con hash BCrypt de la base de datos
        boolean passwordValido = false;
        String hashEnBd = usuario.getPasswordHash();

        if (hashEnBd != null && hashEnBd.startsWith("$2a$")) {
            passwordValido = passwordEncoder.matches(password, hashEnBd);
        } else if (hashEnBd != null) {
            // Compatibilidad si la contraseña estuviese en texto plano
            passwordValido = hashEnBd.equals(password);
            if (passwordValido) {
                // Auto-actualizar a BCrypt seguro
                usuario.setPasswordHash(passwordEncoder.encode(password));
                usuarioRepository.save(usuario);
            }
        }

        if (!passwordValido) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas: contraseña no válida");
        }

        // Control de Perímetro: Si se especifica un rol requerido, validar que corresponda
        if (request.getRol() != null && !request.getRol().trim().isEmpty()) {
            String rolSolicitado = request.getRol().trim();
            if (!usuario.getRol().equalsIgnoreCase(rolSolicitado) && !usuario.getRol().equals("ROLE_ADMIN")) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, 
                        "El rol solicitado no coincide con los permisos asignados en la base de datos");
            }
        }

        String token = jwtUtil.generarToken(usuario.getUsername(), usuario.getRol(), usuario.getNombres() + " " + usuario.getApellidos());
        UsuarioDto userDto = new UsuarioDto(
                usuario.getIdUsuario(),
                usuario.getUsername(),
                usuario.getRol(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getDni(),
                usuario.getEmail()
        );

        return new LoginResponse(token, usuario.getRol(), 86400L, userDto);
    }
}

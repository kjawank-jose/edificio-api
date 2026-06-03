package com.kjawank.edificio.controller;

import com.kjawank.edificio.dto.request.LoginRequest;
import com.kjawank.edificio.dto.response.AuthResponse;
import com.kjawank.edificio.entity.Usuario;
import com.kjawank.edificio.repository.UsuarioRepository;
import com.kjawank.edificio.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UsuarioRepository usuarioRepo;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        // Valida credenciales (lanza excepción si falla)
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );

        Usuario usuario = usuarioRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String codigoDpto = usuario.getDepartamento() != null
                ? usuario.getDepartamento().getCodigo() : null;
        String nombreDpto = usuario.getDepartamento() != null
                ? usuario.getDepartamento().getNombre() : null;

        String token = jwtUtil.generateToken(
                usuario.getUsername(),
                usuario.getRol().name(),
                codigoDpto
        );

        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .rol(usuario.getRol().name())
                .codigoDpto(codigoDpto)
                .nombreDpto(nombreDpto)
                .build());
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(Map.of("status", "ok", "timestamp", new Date()));
    }
}
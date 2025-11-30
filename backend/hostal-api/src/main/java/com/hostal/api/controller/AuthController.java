package com.hostal.api.controller;

import com.hostal.api.dto.AuthDto;
import com.hostal.api.dto.UsuarioDto;
import com.hostal.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para autenticación
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * Endpoint para login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDto.LoginRequest request) {
        try {
            UsuarioDto usuario = authService.autenticarUsuario(request.getEmail(), request.getPassword());
            
            if (usuario != null) {
                // Simular token JWT
                AuthDto.AuthResponse response = new AuthDto.AuthResponse("fake-jwt-token-" + usuario.getId(), usuario);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body("Credenciales incorrectas");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error en el login: " + e.getMessage());
        }
    }
    
    /**
     * Endpoint para registro
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthDto.RegisterRequest request) {
        try {
            UsuarioDto usuario = authService.registrarUsuario(request);
            
            // Simular token JWT
            AuthDto.AuthResponse response = new AuthDto.AuthResponse("fake-jwt-token-" + usuario.getId(), usuario);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error en el registro: " + e.getMessage());
        }
    }
}
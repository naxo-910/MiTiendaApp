package com.hostal.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTOs relacionados con autenticación y usuarios
 */
public class AuthDto {
    
    /**
     * DTO para login de usuario
     */
    public static class LoginRequest {
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Formato de email inválido")
        private String email;
        
        @NotBlank(message = "La contraseña es obligatoria")
        private String password;
        
        // Constructors
        public LoginRequest() {}
        
        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
        
        // Getters and Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    /**
     * DTO para registro de usuario
     */
    public static class RegisterRequest {
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        private String nombre;
        
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Formato de email inválido")
        private String email;
        
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        private String password;
        
        // Constructors
        public RegisterRequest() {}
        
        public RegisterRequest(String nombre, String email, String password) {
            this.nombre = nombre;
            this.email = email;
            this.password = password;
        }
        
        // Getters and Setters
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    /**
     * DTO para respuesta de autenticación
     */
    public static class AuthResponse {
        private String token;
        private String type = "Bearer";
        private UsuarioDto usuario;
        
        // Constructors
        public AuthResponse() {}
        
        public AuthResponse(String token, UsuarioDto usuario) {
            this.token = token;
            this.usuario = usuario;
        }
        
        // Getters and Setters
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public UsuarioDto getUsuario() { return usuario; }
        public void setUsuario(UsuarioDto usuario) { this.usuario = usuario; }
    }
}
package com.hostal.api.service;

import com.hostal.api.dto.AuthDto;
import com.hostal.api.dto.UsuarioDto;
import com.hostal.api.entity.Usuario;
import com.hostal.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Servicio para autenticación y gestión de usuarios
 */
@Service
@Transactional
public class AuthService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Autenticar usuario
     */
    public UsuarioDto autenticarUsuario(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (!usuario.getActivo()) {
                return null; // Usuario inactivo
            }
            if (passwordEncoder.matches(password, usuario.getPassword())) {
                return new UsuarioDto(usuario);
            }
        }
        return null;
    }
    
    /**
     * Registrar nuevo usuario
     */
    public UsuarioDto registrarUsuario(AuthDto.RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(Usuario.RolUsuario.CLIENTE);
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return new UsuarioDto(usuarioGuardado);
    }
}